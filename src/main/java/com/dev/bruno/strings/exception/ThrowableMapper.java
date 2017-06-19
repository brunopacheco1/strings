package com.dev.bruno.strings.exception;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.dev.bruno.strings.model.Response;

@Provider
public class ThrowableMapper implements ExceptionMapper<Throwable> {

	protected Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Override
	public javax.ws.rs.core.Response toResponse(Throwable t) {
		logger.log(Level.SEVERE, t.getMessage(), t);
		
		Response response = new Response("Not expected error: " + t.getMessage());
		
		return javax.ws.rs.core.Response.status(Status.CONFLICT).entity(response).type(MediaType.APPLICATION_JSON).build();

	}
}