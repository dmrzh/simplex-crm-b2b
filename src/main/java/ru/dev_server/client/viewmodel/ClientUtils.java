package ru.dev_server.client.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**.*/
public class ClientUtils {
    private static final Logger LOG= LoggerFactory.getLogger(ClientUtils.class);

    private static Pattern CLIENT_NAME= Pattern.compile("[\\p{L}\\p{Nd}]{0,128}",Pattern.CASE_INSENSITIVE);

    public String checkFilterAndReturnQuery(String searchString){
        if(searchString==null){
            return "%";
        }
        String trim= searchString.trim();
        Matcher matcher = CLIENT_NAME.matcher(trim);
        if(!matcher.matches()){
            throw new IllegalArgumentException("search string must contain only letters or number or space on end");
        }
        return  trim + (searchString.endsWith(" ")?"":"%");
    }
}
