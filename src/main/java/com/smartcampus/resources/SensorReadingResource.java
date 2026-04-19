
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resources;

/**
 *
 * @author seyedaman
 */




import com.smartcampus.data.DataStore;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import com.smartcampus.exception.SensorNotFoundException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.smartcampus.exception.SensorUnavailableException;
import java.util.logging.Logger;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private static final Logger logger = Logger.getLogger(SensorReadingResource.class.getName());

    private final String sensorId;
    private final Map<String, Sensor> sensorStore = DataStore.getInstance().getSensors();
    private final Map<String, List<SensorReading>> readingStore = DataStore.getInstance().getReadings();

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
public Response getAllReadings() {
    logger.info("Fetching readings for sensor: " + sensorId);

    Sensor sensor = sensorStore.get(sensorId);
if (sensor == null) {
    throw new SensorNotFoundException("Sensor not found");
}

    List<SensorReading> readings = readingStore.get(sensorId);

    if (readings == null) {
        readings = new ArrayList<>();
        readingStore.put(sensorId, readings);
    }

    return Response.ok(readings).build();
}

@POST
public Response addReading(SensorReading newReading) {
    logger.info("Adding reading for sensor: " + sensorId);

    Sensor sensor = sensorStore.get(sensorId);
if (sensor == null) {
    throw new SensorNotFoundException("Sensor not found");
}
if ("MAINTENANCE".equals(sensor.getStatus())) {
        throw new SensorUnavailableException("Sensor is currently under maintenance and cannot accept new readings");
    }


    if (newReading == null) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"message\":\"Reading data is missing\"}")
                .build();
    }

    List<SensorReading> readings = readingStore.get(sensorId);

    if (readings == null) {
        readings = new ArrayList<>();
        readingStore.put(sensorId, readings);
    }

    if (newReading.getId() == null || newReading.getId().trim().length() == 0) {
        String autoId = java.util.UUID.randomUUID().toString();
        newReading.setId(autoId);
    }

    if (newReading.getTimestamp() <= 0) {
        long currentTime = System.currentTimeMillis();
        newReading.setTimestamp(currentTime);
    }

    readings.add(newReading);
    readingStore.put(sensorId, readings);

    return Response.status(Response.Status.CREATED)
            .entity(newReading)
            .build();
}
}
        
        
        
        






