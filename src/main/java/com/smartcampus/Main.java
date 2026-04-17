/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package com.smartcampus;

import com.smartcampus.resources.DiscoveryResource;
import org.glassfish.grizzly.http.server.HttpServer;
import com.smartcampus.resources.RoomResource;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import java.net.URI;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
   public static final String BASE_URI = "http://localhost:8080/";

    public static void main(String[] args) throws Exception {
        ResourceConfig config = new ResourceConfig();
        config.register(DiscoveryResource.class);
        config.register(RoomResource.class);
        config.property("jersey.config.server.tracing.type", "ALL");
        config.property("jersey.config.server.tracing.threshold", "VERBOSE");

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);

        LOGGER.info("Smart Campus API is running at " + BASE_URI);
        System.out.println("Press ENTER to stop the server...");
        System.in.read();
        server.stop();
    }
}