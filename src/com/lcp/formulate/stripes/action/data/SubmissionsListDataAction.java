package com.lcp.formulate.stripes.action.data;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.After;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;

import com.lcp.formulate.entities.EntityManager;
import com.lcp.formulate.entities.JsonViews;
import com.lcp.formulate.entities.ormlite.Account;
import com.lcp.formulate.entities.ormlite.Filter;
import com.lcp.formulate.entities.ormlite.Submission;
import com.lcp.formulate.entities.ormlite.Value;
import com.lcp.formulate.entities.ormlite.View;
import com.lcp.formulate.stripes.action.BaseActionBean;
import com.lcp.formulate.stripes.exceptions.AjaxException;

@UrlBinding("/data/submissions/{view}")
public class SubmissionsListDataAction extends BaseActionBean implements DataAction {
	private static Logger log = org.apache.log4j.Logger.getLogger(SubmissionsListDataAction.class);

	@Validate(required=true)
	private View view;
	public void setView(View view) { this.view =view; }
	public View getView() { return view; }
	
	private Integer count;
	public void setCount(Integer count) { this.count = count; }
	public Integer getCount() { return count; }
	
	private Integer offset;
	public void setOffset(Integer offset) { this.offset = offset; }
	public Integer getOffset() { return offset; }
	
	private Integer sortBy;
	public void setSortBy(Integer sortBy) { this.sortBy = sortBy; }
	public Integer getSortBy() { return sortBy; }

	private boolean sortDir = false;
	public void setSortDir(boolean sortDir) { this.sortDir = sortDir; }
	public boolean getSortDir() { return sortDir; }
	
	private String search;
	public void setSearch(String search) { this.search = search; }
	public String getSearch() { return search; }

	private List<Integer> userFilters;
	public void setUserFilters(List<Integer> userFilters) { this.userFilters = userFilters; }
	public List<Integer> getUserFilters() { return userFilters; }
	
	@Validate(required=true, on="submissions")
	private Account account;
	public void setAccount(Account account) { this.account = account; }
	public Account getAccount() { 
		if (this.account == null)
			if (getContext().getUser() != null)
				this.account = getContext().getUser().getAccount();
		return this.account; 
	}
	
	@After(stages=LifecycleStage.BindingAndValidation)
	public void filterInterceptor() {
		String filterString = getContext().getRequest().getParameter("userFilters");
		if (filterString != null) {
			for (String f : filterString.split(",")) {
				try {
					Integer i = Integer.parseInt(f);
					userFilters.add(i);
				} catch (NumberFormatException x) {}
			}
		}
	}
	/**
	 * Submissions
	 */
	public Resolution submissions() throws AjaxException {
		log.info("SubmissionsDataAction");
		log.info("    view: "+getView().getId());
		log.info("    count: "+getCount());
		log.info("    offset: "+getOffset());
		log.info("    sortBy: "+getSortBy());
		log.info("    sortDir: "+getSortDir());
		log.info("    search: "+getSearch());
		log.info("    userFilters: "+getUserFilters());
					
		try {			
			log.info("    ViewID: "+getView().getId()+" Filter Count: "+getView().getFilters().size());
			
			if (getCount() == null)
				setCount(10);
			if (getOffset() == null)
				setOffset(0);
						
			/**
			 * Get the submissions filtered by search term
			 */
			List<Submission> submissions = EntityManager.getSubmissions(getAccount(), getView(), getSearch());
		
			log.info("    Raw submissions retreived: "+submissions.size());
									
			/**
			 *  Process the filters
			 */
			List<Submission> filteredSubmissions = new ArrayList<Submission>();
			
			/**
			 * Parse each submission
			 */
			for (int a=0; a<submissions.size(); a++) {
				log.info("    Parsing submission: "+submissions.get(a).getId());
				
				/**
				 * View has no filters, just add the submission
				 */
				if (getView().getFilters().size()<1)
					filteredSubmissions.add(submissions.get(a));
				
				else {
					boolean noFilters = true;
					/**
					 * Parse each filter
					 */
					for (Filter f : getView().getFilters()) {
						log.info("        Parsing filter: "+f.getId()+"-"+f.getLabel());
						boolean applyFilter = false;
						
						/**
						 * Determine if the filter is static or enabled by the user
						 */
						if (f.getStaticFlag())
							applyFilter = true;
						else {
							if (getUserFilters() != null)
								for (Integer i : getUserFilters()) {
									if (f.getId().equals(i)) {
										applyFilter = true;
										break;
									}
								}
						}
						
						if (applyFilter)
							log.info("        applyFilter is true");
						
						/**
						 * Apply the filter
						 */
						if (applyFilter) {
							noFilters = false;
							
							log.info("        filter target: "+f.getTarget().getId());
							try {
								boolean keep = false;
								boolean valueExists = false;
					
								/**
								 * Parse the values in the submission's valueset
								 */
								for (Value val : submissions.get(a).getValues()) {
						
									/**
									 * If the value is linked to the same field as the filter
									 * Check its value against the filter value and comparison
									 */
									if (val.getField().equals(f.getTarget())) {
										valueExists = true;
										
										log.info("        Found: "+val.getViewField().getField().getName());
										log.info("        Comparison: "+f.getComparison());
										log.info("        Values: "+f.getValue()+"/"+val.getValue());
		
										// Determine if this submission satisfies the filter
										switch (f.getComparison()) {
											case 1:
												// Equal
												keep = val.getValue().toLowerCase().equals(f.getValue().toLowerCase());
												break;
											case 2: 
												// Not Equal
												keep = !val.getValue().toLowerCase().equals(f.getValue().toLowerCase());
												break;
											case 3:
												// Greater than
												// keep if the comparison fails because the value isn't a number
												try {
													keep = Integer.valueOf(val.getValue()) > Integer.valueOf(f.getValue().toLowerCase());
												} catch (NumberFormatException x) {
													keep = true;
												}
												break;
											case 4:
												// Less than
												// keep if the comparison fails because the value isn't a number
												try {
													keep = Integer.valueOf(val.getValue()) < Integer.valueOf(f.getValue().toLowerCase());
												} catch (NumberFormatException x) {
													keep = true;
												}
												break;
										}
									}
								}
					
								log.info("    valueExists: "+valueExists+" / keep:"+keep);
	
								// Submission should be kept
								if (valueExists && keep) {
									filteredSubmissions.add(submissions.get(a));
									break;
								} 
							} catch (Exception x) {
								x.printStackTrace();
							}
						}
					}
					
					if (noFilters)
						filteredSubmissions.add(submissions.get(a));
				}
			}

			/**
			 * Sort the filtered submissions
			 */
			if (getSortBy() != null) {
				log.info("    Sorting submissions");
				
				List<Submission> sortedSubmissions = new ArrayList<Submission>();
				// Parse each of the filtered submissions
				for (Submission submission : filteredSubmissions) {
					int index = 0;
					String val = "";
					
					for (Value v : submission.getValues()) {
						if (v.getField().getId().equals(getSortBy())) {
							val = v.getValue();
							break;
						}
					}	
					
					// Compare the submission to each already sorted submission
					for (int a=0; a<sortedSubmissions.size(); a++) {
						index = a;

						String sortedVal = "";
						
						for (Value v : sortedSubmissions.get(a).getValues()) {
							if (v.getField().getId().equals(getSortBy())) {
								sortedVal = v.getValue();
								break;
							}
						}	
						
						// Compare the nth (getSortBy) value in the submissions
//						String val = new ArrayList<Value>(submission.getValues()).get(getSortBy()).getValue();
//						String sortedVal = new ArrayList<Value>(sortedSubmissions.get(a).getValues()).get(getSortBy()).getValue();
						
						log.info("    ["+submission.getId()+"] "+val +" "+ val.compareTo(sortedVal) +" ["+ sortedSubmissions.get(a).getId() +"]"+sortedVal);
						
						if (getSortDir()) {
							if (val.compareTo(sortedVal) < 0) {
								break;
							}
						} else {						
							// When the comparison of this value to the one in the sorted list is 
							// greater than 0, stop and insert the submission at this point in the sorted list
							if (val.compareTo(sortedVal) > 0) {
								break;
							}		
						}
					}
					log.info("  Adding ["+submission.getId()+"] "+val+" at "+index);
					
					sortedSubmissions.add(index, submission);
				}
				
				filteredSubmissions = sortedSubmissions;
			}
			
			/**
			 * Determine end point for the offset
			 */
			int end = getOffset()+getCount();
			if (end>filteredSubmissions.size())
				end = filteredSubmissions.size();
			
			log.info("    Start: "+getOffset()+" end: "+end+" count: "+getCount());
			
			String submissionsJson = new ObjectMapper().configure(Feature.DEFAULT_VIEW_INCLUSION, false)
				.writerWithView(JsonViews.Submissions.class)
				.writeValueAsString(filteredSubmissions.subList(getOffset(), end));
			
			String responseString = "{\"total\":\""+filteredSubmissions.size()+"\",\"data\":"+submissionsJson+"}";
			
			log.info(responseString);
			
			return new StreamingResolution("text/json", responseString);
		} catch (Exception e) {
			e.printStackTrace();
			getContext().getValidationErrors().add("entity", new SimpleError("unknown exception ["+e.getMessage()+"]"));			
			throw new AjaxException(this, "submissions", "data error");
		}
	}
}