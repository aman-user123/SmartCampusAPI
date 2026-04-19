/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.exception;

/**
 *
 * @author seyedaman
 */

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class RoomDeleteExceptionMapper implements ExceptionMapper<RoomDeleteException> {

    @Override
    public Response toResponse(RoomDeleteException deleteException) {
        Map<String, String> error = new HashMap<>();
        error.put("error", deleteException.getMessage());

        return Response.status(Response.Status.CONFLICT)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
