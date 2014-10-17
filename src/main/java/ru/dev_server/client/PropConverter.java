package ru.dev_server.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.zk.ui.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**.*/
public class PropConverter implements Converter {
    private static final Logger LOG= LoggerFactory.getLogger(PropConverter.class);
    private Properties properties;

    private Class<? extends Enum> enumClass;
    public PropConverter(String path) {
        InputStream stream = PropConverter.class.getClassLoader().getResourceAsStream(path);
       properties = new Properties();
        try {
            properties.load(stream);
        } catch (IOException e) {
            LOG.error(e.getMessage(),e);
        }

    }

    @Override
    public Object coerceToUi(Object beanProp, Component c, BindContext ctx) {
        if(beanProp==null){
            return null;
        }

        return properties.getProperty((String)beanProp);

    }

    @Override
    public String coerceToBean(Object compAttr,  Component c, BindContext ctx) {
        for(Map.Entry e:properties.entrySet()){
            if (e.getValue().equals(compAttr)){
                 return (String) e.getKey();
            }
        }
        return null;
    }
}
