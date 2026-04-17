/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template

*/

package com.smartcampus.resources;

import com.smartcampus.data.DataStore;
import com.smartcampus.model.Room;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Path("api/v1/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    private static final Logger logger = Logger.getLogger(RoomResource.class.getName());
    private final Map<String, Room> roomStorage = DataStore.getInstance().getRooms();

    // returns the full list of rooms available in the system
    @GET
    public Response fetchAllRooms() {
        logger.info("Incoming request - GET all rooms");
        List<Room> allRooms = new ArrayList<>(roomStorage.values());
        return Response.ok(allRooms).build();
    }

    // adds a new room, but blocks duplicates
    @POST
    public Response addNewRoom(Room newRoom) {
        logger.info("Incoming request - POST new room with id: " + newRoom.getId());

        if (newRoom.getId() == null || newRoom.getId().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\":\"Room ID cannot be empty\"}")
                    .build();
        }

        if (roomStorage.containsKey(newRoom.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"message\":\"A room with this ID already exists\"}")
                    .build();
        }

        roomStorage.put(newRoom.getId(), newRoom);
        logger.info("Room created successfully: " + newRoom.getId());

        return Response.status(Response.Status.CREATED)
                .entity(newRoom)
                .build();
    }

    // fetch a single room using its ID
    @GET
    @Path("{id}")
    public Response fetchRoomById(@PathParam("id") String id) {
        logger.info("Incoming request - GET room by id: " + id);

        Room foundRoom = roomStorage.get(id);

        if (foundRoom == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"No room found with id: " + id + "\"}")
                    .build();
        }

        return Response.ok(foundRoom).build();
    }

    // deletes a room only if no sensors are linked to it
    @DELETE
    @Path("{id}")
    public Response removeRoom(@PathParam("id") String id) {
        logger.info("Incoming request - DELETE room: " + id);

        Room targetRoom = roomStorage.get(id);

        if (targetRoom == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"Room not found, nothing to delete\"}")
                    .build();
        }

        // safety check - can't remove a room that still has sensors inside
        if (!targetRoom.getSensorIds().isEmpty()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"message\":\"This room still has sensors attached. Remove them first before deleting the room.\"}")
                    .build();
        }

        roomStorage.remove(id);
        logger.info("Room removed successfully: " + id);
        return Response.noContent().build();
    }
}
