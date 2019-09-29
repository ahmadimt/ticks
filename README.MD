# Ticks

## Building the application

1. Navigate to the root directory `solactive`.
2. On Linux or Mac, run `./mvnw clean install`. For windows `mvnw.cmd clean install`. This will build the application.
3. Once the build is done, a uber jar named as `solactive-0.0.1-SNAPSHOT.jar` will be created in `target` folder.

## Running the application

We can run the application by using `java -jar target/solactive-0.0.1-SNAPSHOT.jar`.

It will start the embedded Tomcat server on port `8080`.

The APIs can be quickly tested using Swagger UI which is available at `http://localhost:8080/swagger-ui.html`.

## Future Improvements
1. Could have written more test cases.

## Whether you liked the challenge or not
I liked the challenge as it forced me to think and implement something in memory(without database) after a long time