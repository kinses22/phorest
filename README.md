# PHOREST ASSESSMENT

The below are links to each section of this readme.

[Phorest Application](#phorest-application)  
[How To Run](#how-to-run)  
[Requirements](#requirements)  
[Assumptions and Things to Note](#Assumptions)
[Things I unfortunately didn't get to finish](#things-i-unfortunately-didnt-get-to-finish)
[Future Considerations and Development](#future-considerations)  

#Phorest Application
This application is to help onboard new customers to our platform. 
The main functionality is to parse csv files provided by the customer
and also to supply the customer with a list of their most loyal customers
on demand. The application has been designed in a scalable way using generics
so to adhere to the DRY principle. Many customers will have their own csv files
and we will be able to handle them using customer specific DTOs that map to our entities. 


##How To Run
##### Maven Wrapper
./mvnw spring-boot:run

##### IDE
- Some IDEs will autoconfigure a run function in the main class.

##How to Use

####Swagger or Postman for API interactions 
- http://localhost:8080/swagger-ui/index.html
####H2 for Database interactions
Username - sa  
password - password  
jdbc url - jdbc:h2:mem:testdb;MODE=PostgreSQL (sometimes it doesnt show)

- http://localhost:8080/h2-console  

##Requirements
- Minimum Version - Java 17

##Assumptions and Things to Note

- I assumed all future clients will be asked to adhere to our db models and provide csv 
files labeled with clients, services, appointment and purchases with the same column 
names as was provided. Obviously we could expand that with a factory method to get column names
based on the client (through a tenant id etc) and load in specific DTOs. This can easily be
handled by the generic service. 
- I used a H2 database in postgres mode for quick development and built in SQL IDE
in order to do quick validations. In future, I would probably swap it out with a postgres
docker image.
- I took the decision to cascade the deletion results to get rid of all appointments, services etc of a client.
This is a defensive strategy towards GDPR guidelines to make sure we are not vulnerable. 
Better safe than sorry, but can always revisit and decide on a strategy to keep certain 
data for audit / analysis purpose / accounting purposes (Store in S3/glacier , redshift etc ).
I decided to put an index on the banned property (JPA) to improve query performance.
The csv upload endpoint can handle one or many files. This is to allow migration to be a bit more flexible. 
The files will be processed in order by parent to child regardless of how you upload the 
files. If you upload a file without the parent records you will get back a detailed dto
with errors explaining theres a constraints issue for each specific record. If some of the records have parent entries,
the file will be partially processed. You can upload files in isolation aswell. Only files with the exact
4 given names and type (csv) will be processed.
- Time is stored in UTC in DB taking in to consideration the offset. These can be converted back depending 
on the time zone of the user. This was outside the scope of the task.
- As previously mentioned, Comb as You are could have its own dto as I know many other saloons would have different files,
so if we werent asking them to standarise their csv files, we could cater to their files with custom dtos 
per client/tenant and map them to our entity.
- Stream vs Parsing the files. I decided to parse the files as they were not large and I dont envision 
the files being 100’s of MBs, however it would be easy to change the implementation
to stream the files so it could partially load bits of the file in to memory to process.
- The method in the parser that set header originally got the headers from the incoming file, 
however that method is deprecated and we have to manage setting the headers in our application on the fly. 
This makes sense as we ultimately have to map the headers to DTOs
- 204 returned for delete as its idempotent. 404 is not used as its not idempotent as there could theoretically
speaking be a record found after a 404 was first discovered.
- 400 badrequest if no file attached.
- Validations are handled but could be improved upon if allowed more time (gender could be more robust).
- Top client endpoint brings back whatever the limit is set at even if there is 0 points ( if there are no purchases or
services in the db ). I didnt have time to change this but I assume the user of the endpoint
would know why and would know there are no purchases or services in the db.

## Things I unfortunately didn't get to finish
- Update: I didnt finish the update fully. It's working but not as well as I would like and the code
is clunky. I wanted to use reflection so all resources could reuse it.
- Rest tests: I developed this using spring boot version 3, a version I am not familiar with and could not get WebMVC tests to work
at the end of the development process and unfortunately did not have time to write rest tests. If time permitted I would
have written rest assured tests with groovy/spock with the h2 in memory db. However I did write unit tests and the code 
coverage is around 90-95% in the main classes.

##Future Considerations and Development
- In future, I would put a limit on file size and amount of files to prevent massive files being dumped and
overloading the system. This would be configurable through the application.properties file.
- Phone numbers: I would strip any chars out and normalise them. There could be different country codes
etc so would have to spend some time thinking of how to handle that.
- Purchases and Services entities/dtos should share super classes called offering, 
requires some configuration with jackson and I didnt have the time to spend on it.
- Consider returning a job id in future if files are really big and return a 202 
and separate endpoint to find the status etc of the job.
- I’d describe the API a bit more with open api annotations.
- I didnt have time to handle some edge cases on potential duplicate primary keys 
( spring jpa repo does an update with its save method if it finds the same pk). 
I assumed it being a UUID this would not be an issue but can revisit if needs be. 
We could always look at making another important column a composite key to safe guard against
unwanted updates.
- Addding a database table with the name of the file and its checksum after its fully processed
could be a way to prevent it being processed again by doing a check to see if the checksum has 
changed on the file.
