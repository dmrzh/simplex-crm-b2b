package ru.dev_server.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**.*/
public class MyExceptionMapper implements ExceptionMapper<Exception> {
    Logger LOG = LoggerFactory.getLogger(MyExceptionMapper.class);
    @Override
    public Response toResponse(Exception ex) {
        LOG.warn(ex.getMessage());
        Response r = Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error").type(MediaType.TEXT_PLAIN)
                .build();
        return r;
    }
}
