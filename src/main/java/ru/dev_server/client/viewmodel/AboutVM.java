package ru.dev_server.client.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.Init;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**.*/
public class AboutVM {
    private static final Logger LOG= LoggerFactory.getLogger(AboutVM.class);
    private Properties prop = new Properties();

    @Init
    public void init(){
        InputStream is = getClass().getClassLoader().getResourceAsStream("BUILD.properties");
        try {
            prop.load(new InputStreamReader(is));
        } catch (IOException e) {
            LOG.warn(e.getMessage(),e);
        }
    }

    public String getVersion() {
        return prop.getProperty("build-version");
    }
    public String getBuildTime(){
        return  prop.getProperty("build-tstamp");
    }

}
