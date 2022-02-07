* A database stub created from Mysql8 server is included in src/main/schema (outdated right now)
* Create duyuru.properties or another with a profile name according to your setup. See 
[Properties needed to boot](#properties-needed-to-boot) for details.
* Launch the app and look at the console exceptions to see required configurations and put them in configuration table

# Spring documentations you need to read
## [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#reference)
Not needed if you have used Spring Data before but helps a lot if you read it.

## [Task Execution and Scheduling in Spring](https://docs.spring.io/spring-framework/docs/5.0.0.M1/spring-framework-reference/html/scheduling.html)
Pretty much required to understand some fundamental things in this project.

# Requirements to boot the backend
## Properties needed to boot
```properties
spring.datasource.url=(insert your db connection url)
spring.datasource.username=(insert your db userEntity)
spring.datasource.password=(insert your db password)
```
Contact the maintainer for details if you need to.

## Properties that are good for development environment
```properties
spring.jpa.properties.javax.persistence.validation.mode=AUTO
server.error.include-stacktrace=always

logging.level.root=INFO
logging.level.org.springframework.web=INFO
logging.level.org.hibernate=INFO
logging.level.aytackydln.duyuru=DEBUG
```