/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 *

/**
 *
 * @author seyedaman
 */
package com.smartcampus.resources;

import com.smartcampus.data.DataStore;
import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.smartcampus.exception.SensorNotFoundException;
import java.util.logging.Logger;

@Path("api/v1/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private static final Logger logger = Logger.getLogger(SensorResource.class.getName());

    private final Map<String, Sensor> sensorStore = DataStore.getInstance().getSensors();
    private final Map<String, Room> roomStore = DataStore.getInstance().getRooms();
    private final Map<String, List<SensorReading>> readingStore = DataStore.getInstance().getReadings();

    @GET
    public Response getSensors(@QueryParam("type") String type) {
        logger.info("Request received to fetch sensors");

        List<Sensor> output = new ArrayList<>();

        if (type == null || type.trim().isEmpty()) {
            output.addAll(sensorStore.values());
        } else {
            for (Sensor sensor : sensorStore.values()) {
                if (sensor.getType() != null && sensor.getType().equalsIgnoreCase(type)) {
                    output.add(sensor);
                }
            }
        }

        return Response.ok(output).build();
    }

    @POST
    public Response addSensor(Sensor newSensor) {
        logger.info("Request received to add sensor");

        if (newSensor == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\":\"Sensor data is missing\"}")
                    .build();
        }

        if (newSensor.getId() == null || newSensor.getId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\":\"Sensor ID is required\"}")
                    .build();
        }

        if (sensorStore.containsKey(newSensor.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"message\":\"A sensor with this ID already exists\"}")
                    .build();
        }

        if (newSensor.getRoomId() == null || newSensor.getRoomId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\":\"Room ID is required for sensor creation\"}")
                    .build();
        }

        Room linkedRoom = roomStore.get(newSensor.getRoomId());

        if (linkedRoom == null) {
    return Response.status(422)
            .entity("{\"message\":\"Cannot add sensor because the room does not exist\"}")
            .build();
}

        sensorStore.put(newSensor.getId(), newSensor);
        linkedRoom.getSensorIds().add(newSensor.getId());
        readingStore.put(newSensor.getId(), new ArrayList<>());

        logger.info("Sensor added successfully: " + newSensor.getId());

        return Response.status(Response.Status.CREATED)
                .entity(newSensor)
                .build();
    }

    @GET
    @Path("{id}")
    
    public Response getSensorById(@PathParam("id") String id) {
        logger.info("Request received to fetch sensor by id: " + id);

        Sensor foundSensor = sensorStore.get(id);

        
        if (foundSensor == null) {
    throw new SensorNotFoundException("Sensor not found");
}
        

        return Response.ok(foundSensor).build();
    }
    @Path("{id}/readings")
public SensorReadingResource getReadingResource(@PathParam("id") String id) {
    return new SensorReadingResource(id);
}
}

