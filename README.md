FormulateAngular
================

Create interactive forms to collect data and allow for display and updating of submitted data.

You will need an Apache Tomcat instance and a mySQL instance.
To get started, use mySQL workbench to forward engineer the model file /Planning/Formulate Database Schema v2.mwb
to your mySQL instance.  The database model contains some sample data.
You will need a mySQL user with permission to read and write to the database, 
the default username and password is 'formulate' and the default server is 'localhost'.
These variables, and others, are defined in the /WEB-INF/web.xml file.

You will need to download the jars listed in /WebContent/WEB-INF/lib/libs needed.txt

There is a .war you can deploy to your Tomcat instance at /Planning/formulate.war
The downloaded jars will need to be placed in the /WEB-INF/lib folder of the expanded war, they are not included!

Formulate makes use of the following Java Libraries:
Stripes 1.5.6 - http://www.stripesframework.com
ORM Lite 4.4.0 - http://www.ormlite.com
Jackson 1.9.5 - http://jackson.codehaus.org/
GSON - http://code.google.com/p/google-gson/
Apache Velocity 1.7 - http://velocity.apache.org/
Apache log4j 1.2 - http://logging.apache.org/log4j/1.2/
Apache Commons - beanutils, collections, fileupload, io, lang, logging, validator
All of the above libraries are available under the Apache 2.0 license - http://www.apache.org/licenses/LICENSE-2.0 

The browser interface for Formulate is built using the Google Angular JS framework - http://angularjs.org 
and the Twitter Bootstrap CSS framework - http://twitter.github.com/bootstrap 

Formulate was written by Daniel Hinds-Bond - http://007design.com
