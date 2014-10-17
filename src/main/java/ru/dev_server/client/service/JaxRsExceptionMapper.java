package ru.dev_server.client.service;

import org.apache.cxf.jaxrs.impl.WebApplicationExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**.*/
public class JaxRsExceptionMapper extends WebApplicationExceptionMapper {
    Logger LOG = LoggerFactory.getLogger(JaxRsExceptionMapper.class);

    public Response toResponse(WebApplicationException ex) {
        LOG.warn(ex.getMessage());
        Response r = Response
                .status(Response.Status.BAD_REQUEST)
                .entity("Error").type(MediaType.TEXT_PLAIN)
                .build();


        return r;
    }
}
