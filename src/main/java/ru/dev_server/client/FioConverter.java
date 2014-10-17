package ru.dev_server.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.zk.ui.Component;
import ru.dev_server.client.model.Client;

/**.*/
public class FioConverter<X extends Component> implements Converter<String,Client,X> {
    private static final Logger LOG= LoggerFactory.getLogger(FioConverter.class);

    public FioConverter() {
    }

    @Override
    public String coerceToUi(Client beanProp, X component, BindContext ctx) {
        if(beanProp==null){
            return "";
        }
        return beanProp.getFio();
    }

    @Override
    public Client coerceToBean(String compAttr, X component, BindContext ctx) {
        return null;
    }


}
