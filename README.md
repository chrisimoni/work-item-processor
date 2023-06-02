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
