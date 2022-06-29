# Store Manager
Home assignment for ING.

---
Requirements:
Create an API that acts as a store management tool
- Create a Github profile if you don't have one
- Use git in a verbose manner, push even if you wrote only one class
- Create a Java, maven based project, Springboot for the web part
- No front-end, you can focus on backend, no need to overcomplicate the structure
- Implement basic functions, for example: add-product, find-product, change-price or others
- Optional: Implement a basic authentication mechanism and role based endpoint access
- Design error mechanism and handling plus logging
- Write unit tests, at least for one class
- Use Java 9+ features
- Add a small Readme to document the project
---
## How to run

Download the project and go to the location where it was unpacked.

Open a terminal/command line and type the following two commands:
```
mvn clean install
mvn spring-boot:run
```

Tests can be run by entering the following command:
```mvn test```

---
## API Documentation

API Documentation for user registration and authentication can be found [here](https://documenter.getpostman.com/view/10048561/UzBvF32x)

API Documentation for store operations can be found [here](https://documenter.getpostman.com/view/10048561/UzBvF37J)

---
## Implementation details

Authorization is implemented with the help of Spring Security.

Technology stack:
- Java 11
- Spring boot
- Spring security
- Maven
- Lombok
- Mapstruct
