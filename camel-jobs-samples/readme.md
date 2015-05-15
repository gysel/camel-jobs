# camel-jobs samples

`context-samples` contains two examples of jobs.

* `job-print-sunset` loads an xml file, applies an xpatch expression and prints todays sunset time in Zurich, Switzerland.
* `job-current-position-iss` loads a json file, parses it using Jackson and prints the current position of the Inernational Space Station.

## Build and run

``` bash
mvn package
cd target
java -jar camel-jobs-samples-0.0.1-SNAPSHOT.jar -fa classpath*:META-INF/spring/*.xml
```

## Start jobs

Get a list of all jobs

   curl -X GET http://localhost:8080/jobs

Get a job status

   curl -X GET http://localhost:8080/jobs/job-current-position-iss

Start a job

   curl -X POST http://localhost:8080/jobs/job-current-position-iss