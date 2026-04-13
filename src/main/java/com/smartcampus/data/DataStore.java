/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.smartcampus.data;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataStore{
    private static final DataStore instance= new DataStore();
    
    private final Map<String, Room> rooms= new LinkedHashMap<>();
    private final Map<String,Sensor> sensors= new LinkedHashMap<>();
    private final Map<String, List<SensorReading>> readings = new LinkedHashMap<>();
    
    private DataStore(){
        Room r1= new Room("LIB-301","Library Quiet Study",50);
        rooms.put(r1.getId(),s1);
        
        Sensor s1= new Sensor("Temp-001","Temperature","ACTIVE",22.5,"LIB-301");
        sensors.put(s1.getId(),s1);
        r1.getSensorIds().add(s1.getId());

        readings.put(s1.getId(), new ArrayList<>());
    }
    public static DataStore getInstance() {
        return instance;
    }
    
    public Map<String, Room> getRooms() { return rooms; }
    public Map<String, Sensor> getSensors() { return sensors; }
    public Map<String, List<SensorReading>> getReadings() { return readings; }
    
    
}
