/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.exception;

/**
 *
 * @author seyedaman
 */

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger logger = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable unexpectedError) {

        // let JAX-RS handle its own exceptions normally
        if (unexpectedError instanceof WebApplicationException) {
            return ((WebApplicationException) unexpectedError).getResponse();
        }

        // let specific mappers handle known custom exceptions
        if (unexpectedError instanceof SensorUnavailableException ||
            unexpectedError instanceof SensorNotFoundException) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"Unhandled custom exception: " + unexpectedError.getMessage() + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        logger.severe("Unexpected error: " + unexpectedError.getMessage());

        Map<String, String> error = new HashMap<>();
        error.put("error", "Something went wrong on the server");
        error.put("details", unexpectedError.getMessage());

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
