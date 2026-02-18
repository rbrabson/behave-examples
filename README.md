# Behave Examples Java Project

This is a simple Java project scaffolded with Maven.

## Structure

- `src/main/java` - Application source code
- `src/test/java` - Test code
- `pom.xml` - Maven build file

## Build and Run

To build the project:

``` bash
mvn clean package
```

To run the application:

``` bash
mvn exec:java -Dexec.mainClass="com.example.App"
```

To run tests:

``` bash
mvn test
```
