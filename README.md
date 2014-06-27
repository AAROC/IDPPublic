IDPPublic
=========

This project provides code for the front-end of a Shibboleth Identity Provider. The IdP is particlarly adapted for *homeless* users and includes membership request workflow and annual expiration of membership. 

## Prerequisites
In order to build this, you will need :
  1. OpenJDK 
  1. Tomcat6 application server
  2. mail.jar in the classpath

Overview of functionality
=========================
This is a web frontend to the Identity Provider service. The IdP is based on Shibboleth - it is assumed that you already have a working Shibboleth web service running. This frontend is simply an easy way to present a public face to the IdP, but also provide a registration page for potential users. 

There is a predefined workflow for accepting new users: 
  1. User fills in a form on the registration page
  2. An email is sent to the user with the email provided, by the service, which prompts the user to confirm their request.
  3. The request confirmation is sent to the IdP administrator
  4. The IdP administrator logs into the IdP and is presented with a table of requests describing the pending requests and a button to approve or deny the requests
  5. Once a request is denied or accepted, a final email is sent to the user; If a user is accepted, the identity is automatically inserted into the LDAP backend (provided this has been correctly configured)
  6. Every year the IdP will send an e-mail to the user asking to extend the account for another year. If the user extend the account it can be used otherwise the account is suspended. The user can ask to enable the account again but this requires the administrator to accept the request

Customisation
=============

This code has been modified from the IdPOpen project. In order to customise it for your IdP, you need to take into account a couple of technical details regarding the ldap directory backend, tomcat configuration and firewall, apart from the changes to the jsp pages and images.

The `master` branch is a generic template, which can be customised. We suggest that you fork the master branch into your own repository and create a new branch with your project name : *e.g.* `sagrid`, `gridp`, etc. Stable versions are tagged with version numbers.

There are also a few specific files which need careful attention, to correspond with the LDAP and other local settings of your IdP : 

  1. ldap.properties: the ldap.properties file is necessary to configure the communication with the LDAP directory backend which contains the identities of the IdP. You should configure this file according to your particular directory location, structure and permissions. 
  2. cleaner.properties: the cleaner.properties file contain the information about when and how notify the users for the account expiration
  3. hibernate.cfg.xml: edit the path to the hibernate persistent storage :  
```xml 
  <property name="hibernate.connection.url">jdbc:hsqldb:file:/tmp/test/</property>
``` 
  4. struts.xml: this file contains the confguration of the controller part of the MVC application. Edit it to reflect your particular taste, with relevant information regarding the contact details, etc. 
  5. context.xml: put your mail server into the relevant section
  6. web / img: put your institute's logo and other artwork you would like into this directory
  7. web / WEB-INF: edit header.jspf and footer.jspf accordingly 
  8. various content in jsp files: you can edit the jsp files in web / according to your taste.

Compilation
===========

By importing the project into Netbeans, you can compile the project, which will generate the war and deploy it. This can be tested on your laptop before deploying the war to the production idp machine - you can check layout, content, design, etc.

[![Travis Build Status](https://travis-ci.org/AAROC/IDPPublic.svg?branch=sagrid-1.1.0rc)](https://travis-ci.org/AAROC/IDPPublic)

This project is continually tested using [Travis](http://travis-ci.org). Clean builds on Travis mean that the master branch will build in principle, but provide no guarantee of the suitability or stablity of the code. 

Tomcat configuration
====================

The applications is designed to run under the tomcat6 webserver. Be sure to have the mail.jar in the common classpath in order to make the mail working
