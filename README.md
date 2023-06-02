# Work Item Processor.
A Spring Boot application that processes and reports work items. A work item is a number between 1 and 10. 
The application is able to handle a large number of work items without blocking, items are processed asynchronously,
utilizing a producer-consumer architecture using RabbitMQ. The work items and their processing results are stored in a 
MongoDB database. Finally, there is a web page created to display and download the report.

## Getting Started
These instructions will get you a copy of the project up and running on your local machine.
* Clone the repository [https://github.com/chrisimoni/work-item-processor.git](https://github.com/chrisimoni/work-item-processor.git)
* Import the project to your IDE, it is a maven project, so wait for maven to download all dependencies
* Update the application.properties with your RabbitMQ and MongoDB credentials 
* Lunch the project either from the IDE or terminal, it runs on http://localhost:8080/ be default
* Swagger documentation for the APIs can be assessed using: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## How it works
### Endpoints (check swagger doc for full details)
* Create item: Receives the work item value and returns a unique ID for the newly created work item.
* Get item: Receives a work item ID and returns the work item value, processed status, and result value.
* Delete item: Receives a work item ID and deletes the item if it has not been processed yet.
* Generate items: When initiated generate 1000 items asynchronously.
* Get report: Returns the report data that is displayed on the report page

### Webpage
The Report page can be accessed on localhost:8080/ when the app starts, the page displays the report in a table,
with two buttons at the top of the table:
* Refresh button: Used to refresh the page to retrieve updated data.
* Download button: Used to download the report (basic view) in a PDF file

NOTE: For the reporting, I used Jasper Report dependency and use code to generate a basic template, instead of using a tool like JasperReports Studio.

### The process of queuing items for processing
* Created or generated items are stored in the MongoDB, each item has its result set to null and processed set to false by default.
* I created a scheduler that fetches up to 100 unprocessed items and published (I created a publisher class) all the items in to the queue.
* I implemented a consumer that operates on the items, processing maximum of two items concurrently.

When processing the items by the consuming, I introduced a simulated delay in the code to mimic the processing time. 
the delay is calculated as <work item value> * 10 ms. A processed item has its processed status set to true, and the value of the result is the square of its value.

### Tools/Dependencies Used
* Spring Boot 3
* Lombok
* RabbitMQ
* Spring Data Mongo
* Spring Data JPA
* Jasper Report
* JUnit & Mockito
* Swagger

The solution contains proper validations and custom exception handlers for different senerios




### Endpoints
1. GET: localhost:8080/api/v1/countries

Returned list of 32 countries to choose from when creating contact
```json
//Sample Response Body
{
    "status": 200,
    "description": "List of countries",
    "data": [
        {
            "id": 1,
            "name": "South Africa",
            "code": "ZA",
            "phoneCode": "+27"
        },
        {
            "id": 2,
            "name": "Ireland",
            "code": "IE",
            "phoneCode": "+353"
        }
    ]
 }

```


2. Post: localhost:8080/api/v1/contacts

To create new contact
```json
//Sample Request Body: where the value of countryId and nationality are valid country ID
{
    "firstName": "Phil",
    "lastName": "Jones",
    "countryId": 1,
    "nationality": 1,
    "birthday": "2003-03-21",
    "gender": "male",
    "email": "jones@gmail.com",
    "nationalId": "12345678"
}

//Sample Response Body
{
    "status": 201,
    "description": "Contacted created successfully",
    "data": {
        "id": 4,
        "firstName": "Phil",
        "lastName": "Jones",
        "email": "jones@gmail.com",
        "nationality": 1,
        "gender": "male",
        "birthday": "2003-03-21",
        "nationalId": "12345678",
        "createdAt": "2023-04-12T14:31:23.454+00:00"
    }
}
```


3. GET: localhost:8080/api/v1/contacts

To retrieve all contacts sorted in decending order
```json
//Sample Response Body
   {
    "status": 200,
    "description": "All contacts",
    "data": [
        {
            "id": 2,
            "firstName": "Chris",
            "lastName": "real",
            "email": "chris@gmail.com",
            "nationality": 25,
            "gender": "male",
            "birthday": "2001-03-21",
            "nationalId": "12345678",
            "createdAt": "2023-04-12T14:06:06.964+00:00"
        },
        {
            "id": 1,
            "firstName": "John",
            "lastName": "Doe",
            "email": "johndoe@gmail.com",
            "nationality": 25,
            "gender": "male",
            "birthday": "2001-03-21",
            "nationalId": "12345678",
            "createdAt": "2023-04-12T13:40:21.035+00:00"
        }
    ]
}
     
```


4. GET: localhost:8080/api/v1/contacts/4

To retrieve a single contact by ID
```json
//Sample Response Body
   {
    "status": 200,
    "description": "Single contact",
    "data": {
        "id": 4,
        "firstName": "Phil",
        "lastName": "Jones",
        "email": "jones@gmail.com",
        "nationality": 1,
        "gender": "male",
        "birthday": "2003-03-21",
        "nationalId": "12345678",
        "createdAt": "2023-04-12T14:31:23.454+00:00"
    }
}
     
```


5. DELETE: localhost:8080/api/v1/contacts/4

To retrieve delete contact by ID
```json
//Returns No Content
     
```


6. PATCH: localhost:8080/api/v1/contacts/4

To update a contact info exluding email, date of birtday and createdAt
```json
//Sample Request Body
{
    "firstName": "Anita",
    "lastName": "John",
    "countryId": 2,
    "nationality": 2,
    "gender": "female",
    "nationalId": "12345678"
}

//Sample Response Body
{
    "status": 200,
    "description": "Contact updated",
    "data": {
        "id": 4,
        "firstName": "Anita",
        "lastName": "John",
        "email": "jones@gmail.com",
        "nationality": 2,
        "gender": "female",
        "birthday": "2003-03-21",
        "nationalId": "12345678",
        "createdAt": "2023-04-12T14:31:23.454+00:00"
    }
}
     
```
