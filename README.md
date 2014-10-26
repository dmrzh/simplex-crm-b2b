simplex-crm-b2b
===============

CRM for B2B with web interface.

##To run application in development mode

1. Install java    
2. Install maven
3. Instal postgresql database
4. correct src/profiles/dev/META-INF/context.xml
5. start application:  
 
        mvn org.apache.tomcat.maven:tomcat7-maven-plugin:run
6. open browser at [http://localhost:8080/simplex-crm-b2b/](http://localhost:8080/simplex-crm-b2b/)

##To make war distribution

1. Install java
2. Install maven
3. Instal postgresql database
4. correct src/profiles/prod/META-INF/context.xml
5. build application:  
   
        mvn -Dmaven.test.skip=true clean package -P prod     
*.war file wil be created in target/ directory

To run application in production mode deploy war in you application server (tested with jetty).



[Screenshot](https://github.com/dmrzh/simplex-crm-b2b/wiki/Screehshot)

[Demo](http://b2b.simplex-crm.ru/)
