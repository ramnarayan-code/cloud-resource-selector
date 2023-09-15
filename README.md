# Getting Started
## Tech Stack
- Java 17
- Spring boot v3.1.3

## Features
- AWS Region IP-Range info persisted in the memory
- Scheduled update of AWS Region IP-Range information
- Extensible codebase to include GCP, Azure and other 
cloud platform based IP Range selectors
- Externally configurable AWS Regions and Datasource URL (in application.properties file)
- Dockerized application. The below script builds the image and runs the container in the port of 8080,
 ```
  ./build_run.sh
  ```
- Github action to run unit test and the report is published.

  Example:

  Unit test report link:
  https://github.com/ramnarayan-code/cloud-resource-selector/actions/runs/6198694617/job/16829815842
- Postman collection to find all the REST API endpoints

https://github.com/ramnarayan-code/cloud-resource-selector/blob/main/Postman-Collection.postman_collection.json


