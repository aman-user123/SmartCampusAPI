# SmartCampusAPI

A RESTful API built with JAX-RS (Jersey) and Grizzly HTTP server for managing rooms and sensors across a university smart campus system.

---

## Tech Stack

- Java 11
- JAX-RS (Jersey 2.41)
- Grizzly HTTP Server
- Maven
- In-memory data storage (HashMap)

---

## How to Build and Run

### Prerequisites
- Java 11 or higher
- Maven installed

### Steps

1. Clone the repository:
```bash
git clone https://github.com/seyedaman@Ajmals-MacBook-Pro/SmartCampusAPI.git
cd SmartCampusAPI
```

2. Build the project:
```bash
mvn clean install
```

3. Run the server:
```bash
mvn exec:java -Dexec.mainClass="com.smartcampus.Main"
```

4. Server will start at:
http://localhost:8080/

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/v1 | Discovery endpoint |
| GET | /api/v1/rooms | Get all rooms |
| POST | /api/v1/rooms | Create a room |
| GET | /api/v1/rooms/{id} | Get room by ID |
| DELETE | /api/v1/rooms/{id} | Delete a room |
| GET | /api/v1/sensors | Get all sensors |
| POST | /api/v1/sensors | Create a sensor |
| GET | /api/v1/sensors/{id} | Get sensor by ID |
| GET | /api/v1/sensors?type=CO2 | Filter sensors by type |
| GET | /api/v1/sensors/{id}/readings | Get readings for a sensor |
| POST | /api/v1/sensors/{id}/readings | Add a reading |

---

## Sample curl Commands

**1. Get API info:**
```bash
curl http://localhost:8080/api/v1
```

**2. Get all rooms:**
```bash
curl http://localhost:8080/api/v1/rooms
```

**3. Create a new room:**
```bash
curl -X POST http://localhost:8080/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"id":"ENG-101","name":"Engineering Lab","capacity":40}'
```

**4. Create a sensor:**
```bash
curl -X POST http://localhost:8080/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"CO2-001","type":"CO2","status":"ACTIVE","currentValue":0.0,"roomId":"LIB-301"}'
```

**5. Add a sensor reading:**
```bash
curl -X POST http://localhost:8080/api/v1/sensors/CO2-001/readings \
  -H "Content-Type: application/json" \
  -d '{"value":450.0}'
```

---

## Project Structure
SmartCampusAPI/
├── src/main/java/com/smartcampus/
│   ├── Main.java
│   ├── AppConfig.java
│   ├── data/
│   │   └── DataStore.java
│   ├── model/
│   │   ├── Room.java
│   │   ├── Sensor.java
│   │   └── SensorReading.java
│   ├── resources/
│   │   ├── DiscoveryResource.java
│   │   ├── RoomResource.java
│   │   ├── SensorResource.java
│   │   └── SensorReadingResource.java
│   ├── exception/
│   │   ├── GlobalExceptionMapper.java
│   │   ├── RoomDeleteExceptionMapper.java
│   │   ├── RoomNotFoundException.java
│   │   ├── RoomNotFoundExceptionMapper.java
│   │   ├── SensorNotFoundException.java
│   │   ├── SensorNotFoundExceptionMapper.java
│   │   ├── SensorUnavailableException.java
│   │   └── SensorUnavailableExceptionMapper.java
│   └── filter/
│       └── RequestLoggingFilter.java
└── pom.xml



