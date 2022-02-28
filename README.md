# JWT Login 

Test project with a simple login api using Java and JWT (JJWT)


## Maven wrapper build

To start the application, run the following command from the project root directory:
```
./mvnw spring-boot:run
```

## API

Login API
```
curl -X POST http://localhost:8080/login \
    -H "Content-Type:application/json" \
    -d'{"username":"johndoe", "password":"1234"}'

Success example:
{
    "token":"<jwt token>"
}

Error example:
{
    "timestamp":"<timestamp>",
    "status":400,
    "error":"Bad Request",
    "message":"Invalid creadentials. Try again.",
    "path":"/login"
}
```

Logged API
```
curl -X GET http://localhost:8080/logged \
    -H "Authorization:Bearer <token>,Content-Type:application/json"

Success example:
You're logged!

Error example:
{
    "timestamp":"<timestamp>",
    "status":400,
    "error":"Bad Request",
    "message":"Not logged. Invalid token.",
    "path":"/logged"
}
```

## Tests

To run tests, run the following command:

```
./mvnw test
```

