FormulateAngular
================
Create interactive forms to collect data and allow for display and updating of submitted data.
<br><br>
You will need an Apache Tomcat instance and a mySQL instance.<br>
To get started, use mySQL workbench to forward engineer the model file /Planning/Formulate Database Schema v2.mwb
to your mySQL instance.  The database model contains some sample data.<br>
You will need a mySQL user with permission to read and write to the database, 
the default username and password is 'formulate' and the default server is 'localhost'.<br>
These variables, and others, are defined in the /WEB-INF/web.xml file.
<br><br>
You will need to download the jars listed in /WebContent/WEB-INF/lib/libs needed.txt
<br><br>
There is a .war you can deploy to your Tomcat instance at /Planning/formulate.war<br>
The downloaded jars will need to be placed in the /WEB-INF/lib folder of the expanded war, they are not included!
<br><br>
Formulate makes use of the following Java Libraries:<br>
Stripes 1.5.6 - http://www.stripesframework.com<br>
ORM Lite 4.4.0 - http://www.ormlite.com<br>
Jackson 1.9.5 - http://jackson.codehaus.org/<br>
GSON - http://code.google.com/p/google-gson/<br>
Apache Velocity 1.7 - http://velocity.apache.org/<br>
Apache log4j 1.2 - http://logging.apache.org/log4j/1.2/<br>
Apache Commons - beanutils, collections, fileupload, io, lang, logging, validator<br>
All of the above libraries are available under the Apache 2.0 license - http://www.apache.org/licenses/LICENSE-2.0 <br>
<br><br>
The browser interface for Formulate is built using the Google Angular JS framework - http://angularjs.org <br>
and the Twitter Bootstrap CSS framework - http://twitter.github.com/bootstrap 
<br><br>
Formulate was written by Daniel Hinds-Bond - http://007design.com
