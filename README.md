## Description

Java server, for executing scripts, the server support just python 2.7 right now
other script could be added easily by implementing the `Interpreter` interface.

## Requirements

- java JDK 1.8
- maven

## Configurations
You can change configuration in the file application.propreties, by passing
environment variable or by using java CLI parameters.

`notebook.timeout` : Maximum time for script execution in ***ms***.

## Running

Using CLI:
```
$ mvn spring-boot:run
```

With docker:
```
$ docker build . -t challenge
$ docker run -p 8080:8080 challenge
```

## Usage

```
$ curl http://localhost:8080/execute -d '{"code": "%python a = 42"}' -H 'content-type: application/json'
{"result":"","sessionId":"fe2f9727-6642-424c-b954-a30cbcb0b067"}
$ curl http://localhost:8080/execute?sessionId=fe2f9727-6642-424c-b954-a30cbcb0b067 -d '{"code": "%python print a + 1"}' -H 'content-type: application/json'
{"result":"43\n","sessionId":"fe2f9727-6642-424c-b954-a30cbcb0b067"}
```
