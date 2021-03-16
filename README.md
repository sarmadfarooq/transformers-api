# Transformers Exercise
This project implements the requirements given for transformers project.  

## Build
It uses maven to build tha project.
 
`mvn clean install`
### Running Unit Tests only
`mvn test`

## Run
Run as springboot application 

`mvn spring-boot:run`


# Assumptions
* Autobots win the fight even if they win equal number of fights as Decepticons

# Swagger Documentation
The swagger doc can be found at:
http://localhost:8080/v2/api-docs

An HTML page outlining the endpoints and resource structure can be found at:
http://localhost:8080/swagger-ui/#/

#How to use
### Get all transformers
Use endpoint below to fetch a list of all transformers
http://localhost:8080/game/transformers

### CURL 
`curl -X GET "http://localhost:8080/game/transformers" -H "accept: */*"`

### Create a transformer
Use endpoint below to create a new transformer
http://localhost:8080/game/transformer
### JSON Payload
`{
  "courage": 10,
  "endurance": 20,
  "firepower": 30,
  "intelligence": 0,
  "name": "TestAuto",
  "rank": 20,
  "skill": 30,
  "speed": 40,
  "strength": 20,
  "type": "A"
}`
### CURL 
`curl -X POST "http://localhost:8080/game/transformer" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"courage\": 10, \"endurance\": 20, \"firepower\": 30, \"intelligence\": 0, \"name\": \"TestAuto\", \"rank\": 20, \"skill\": 30, \"speed\": 40, \"strength\": 20, \"type\": \"A\"}"`

### Update a transformer
Use endpoint below to update a transformer
http://localhost:8080/game/transformer/2
### JSON Payload
`{
   "courage": 20,
   "endurance": 220,
   "firepower": 30,
   "id": 2,
   "intelligence": 30,
   "name": "UpdatedName",
   "rank": 0,
   "skill": 10,
   "speed": 10,
   "strength": 10,
   "type": "A"
 }`
### CURL 
`curl -X PUT "http://localhost:8080/game/transformer/2" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"courage\": 20, \"endurance\": 220, \"firepower\": 30, \"id\": 2, \"intelligence\": 30, \"name\": \"UpdatedName\", \"rank\": 0, \"skill\": 10, \"speed\": 10, \"strength\": 10, \"type\": \"A\"}"`

### Delete a transformer
Use endpoint below to delete a transformer
http://localhost:8080/game/transformer/1

### CURL 
`curl -X DELETE "http://localhost:8080/game/transformer/1" -H "accept: */*"`

### Initiate Transformer fight
Use endpoint below to initiate a transformer's fight between transformers of id 8 and id 6
http://localhost:8080/game/fight

### CURL 
`curl -X POST "http://localhost:8080/game/fight" -H "accept: */*" -H "Content-Type: application/json" -d "[ 8,6]"`

### Fight Response
Fight details are sent back in JSON format.  

### Example JSON fight response 
`{
  "battleCount": 1,
  "winningTeam": "A",
  "winners": [
    "TestAuto"
  ],
  "survivors": []
}`