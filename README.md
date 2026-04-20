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


---

## Report Answers

### Part 1.1 — JAX-RS Lifecycle
By default, JAX-RS creates a new instance of a resource class for every incoming request. This is called request-scoped lifecycle. This means each request gets its own fresh object, which avoids threading issues between simultaneous requests.

This also implies that we have to exercise caution in the sense that should there be multiple requests trying to update a certain HashMap simultaneously, there would be potential data corruption. This problem could be resolved using ConcurrentHashMap, as opposed to HashMap which is not thread-safe by design.



### Part 1.2 — HATEOAS
HATEOAS (Hypermedia as the Engine of Application State) means that API responses include links to related resources, so clients can navigate the API without needing to hardcode URLs. For example, when a room is returned, the response could include a link to its sensors.

This is advantageous to client developers since they do not have to memorise or guess endpoint URLs, the API itself informs them of the next step.It also makes the API more flexible, since if URLs change, clients that follow links won't break.



### Part 2.1 — IDs vs Full Objects

Using just ID numbers for the results is lighter because it consumes fewer resources and time but requires clients to query for each one individually to get its information.
Returning full objects is more convenient for the client since all data is available in one request, but it increases response size significantly, especially when there are thousands of rooms. 

The best approach depends on the use case — for large datasets, returning IDs is better, but for smaller collections, returning full objects saves round trips.


### Part 2.2 — Is DELETE Idempotent?
Yes, DELETE is idempotent in our implementation. Idempotent means sending the same request multiple times produces the same result as sending it once.

In our case, if a client sends DELETE /rooms/CS-101 and the room is successfully deleted, sending the exact same request again will return a 404 Not Found — because the room no longer exists. The server state doesn't change on the second call, which satisfies the definition of idempotency


### Part 3.1 — Content-Type Mismatch
The @Consumes(MediaType.APPLICATION_JSON) annotation tells JAX-RS that the endpoint only accepts JSON. When a client submits a request with another format such as text/plain or application/xml, JAX-RS will automatically reject the request and send a 415 Unsupported Media Type response without even going to our method code.

### Part 3.2 — QueryParam vs PathParam
Using @QueryParam for filtering (e.g. /sensors?type=CO2) is better than putting it in the path (e.g. /sensors/type/CO2) because query parameters are optional by nature — if no type is provided, the endpoint simply returns all sensors without breaking.

Path parameters are better suited for identifying a specific resource (like /sensors/TEMP-001), not for filtering a collection. Using query parameters also follows REST conventions and makes the API more intuitive for client developers.


### Part 4.1 — Sub-Resource Locator Benefits
The Sub-Resource Locator pattern lets us assign the handling of nested paths to a different class.Instead of placing all the logic for /sensors/{id}/readings inside SensorResource, we give it to a separate SensorReadingResource class

This keeps each class focused on one responsibility, making the code easier to read, maintain and test. In large APIs with many nested resources, this pattern keeps resource classes from becoming too big and unmanageable


### Part 5.2 — Why 422 over 404
A 404 Not Found suggests that the URL itself doesn't exist, which is misleading — the endpoint /sensors is perfectly valid. The problem is that the roomId referenced inside the JSON body doesn't exist in the system.

A 422 Unprocessable Entity is more accurate because it tells the client that the request was received and understood, but the data inside it is semantically invalid — specifically, it references a resource that doesn't exist.


### Part 5.4 — Stack Trace Security Risks
The disclosure of java stack traces to outside clients poses a significant security threat due to the fact that this will give the attackers information regarding the internal implementation of the system. This includes the names of classes, packages, methods, file line numbers, and library versioning.

 The attacker will be able to see the exact framework versions that are installed and then search for the respective vulnerability.
This is why our GlobalExceptionMapper returns a clean generic error message instead of the raw stack trace.


### Part 5.5 — Why Filters over Manual Logging
Using JAX-RS filters for logging is better than manually adding Logger.info() calls inside every resource method because filters apply automatically to every request and response without touching individual methods.

This follows the DRY principle (Don't Repeat Yourself) — if we need to change the logging format, we only change it in one place. It also keeps resource methods clean and focused on business logic rather than cross-cutting concerns like logging.



