simplex-crm-b2b
===============

CRM for B2B with web interface.

##To run application in development mode

1. Install java    
2. Install maven
3. Instal postgresql database
4. correct src/profiles/dev/app.properties
5. start application: mvn jetty:run

##To make war distribution

1. Install java
2. Install maven
3. Instal postgresql database
4. correct src/profiles/dev/app.properties
5. build application: 
    
        mvn clean package    
*.war file wil be created in target/ directory

to run application in production mode deploy war in you application server (tested with jetty).



[Screenshot](https://github.com/dmrzh/simplex-crm-b2b/wiki/Screehshot)
