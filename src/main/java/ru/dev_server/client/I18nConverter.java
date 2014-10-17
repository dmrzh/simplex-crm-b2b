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
public class I18nConverter<C extends Component> implements Converter<String, Enum, C> {
    private static final Logger LOG= LoggerFactory.getLogger(I18nConverter.class);
    private Properties properties;
    //private Map<Class<? extends Enum>,Properties> propertiesMap= new HashMap<Class<? extends Enum>,Properties>()

    private Class<? extends Enum> enumClass;
    public I18nConverter(Class<? extends Enum> enumClass) {
        this.enumClass=enumClass;
        InputStream stream = I18nConverter.class.getClassLoader().getResourceAsStream(enumClass.getName()+".properties");
       properties = new Properties();
        try {
            properties.load(stream);
        } catch (IOException e) {
            LOG.error(e.getMessage(),e);
        }

    }

    @Override
    public String coerceToUi(Enum beanProp, Component c, BindContext ctx) {
        if(beanProp==null){
            return null;
        }
        return properties.getProperty(beanProp.name());
    }
    @Override
    public Enum coerceToBean(String compAttr,  Component c, BindContext ctx) {
        for(Map.Entry e:properties.entrySet()){
            if (e.getValue().equals(compAttr)){
                Enum anEnum = Enum.valueOf(enumClass, (String) e.getKey());
                return anEnum;
            }
        }
        return null;
    }
}
