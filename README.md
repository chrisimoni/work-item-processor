# Work Item Processor.
A Spring Boot application that processes and reports work items. A work item is a number between 1 and 10.
The application is able to handle a large number of work items without blocking, items are processed asynchronously,
utilizing a producer-consumer architecture using `RabbitMQ`. The work items and their processing results are stored in a
MongoDB database. Finally, there is a web page created to display and download the report.

## Table of Contents

- [Work Item Processor](#work-item-processor)
    - [Getting Started](#getting-started)
    - [How it Works](#how-it-works)
        - [Endpoints](#endpoints)
        - [Webpage](#webpage)
        - [The Process of Queuing Items for Processing](#the-process-of-queuing-items-for-processing)
    - [Tools/Dependencies Used](#toolsdependencies-used)
- [MongoDB](#mongodb-)

## Getting Started
These instructions will get you a copy of the project up and running on your local machine.
* Clone project
```Git
git clone https://github.com/chrisimoni/work-item-processor.git
```
* Import the project to your IDE (IntelliJ was used in building this application.) it is a maven project, so wait for maven to download all dependencies
* Update the `application.properties` with your RabbitMQ and MongoDB credentials
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
* `Refresh button`: Used to refresh the page to retrieve updated data.
* `Download button`: Used to download the report (basic view) in a PDF file

<b>Note:</b> For the reporting, I used <b>Jasper Report dependency</b> and use code to generate a basic template, instead of using a tool like <b>JasperReports Studio</b>.

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

## MongoDB
MongoDB NoSQL database was used due to its unique features such as:
<ul>
<li><b>Flexibility and Scalability</b>: MongoDB is a NoSQL database that provides a flexible schema design. This allows you to easily handle evolving data structures and accommodate changes in the work item format without modifying the database schema. It also enables horizontal scalability, making it easier to handle large volumes of work items and scale your application as needed.</li>
<br>
<li><b>Document-Oriented Structure</b>: MongoDB stores data in a document-oriented format using JSON-like documents. This structure is well-suited for work items, which often have complex and nested data structures. You can store the entire work item as a single document, making it easier to retrieve and manipulate the data.
</li>
<br>
<li><b>High Performance</b>: MongoDB's document model and indexing capabilities contribute to high performance in read and write operations. It supports indexing on various fields, allowing you to optimize queries for efficient retrieval of work items and generate reports quickly.
</li>
    </ul>

The solution contains proper validations and custom exception handlers for different senerios
