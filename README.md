# Spting Boot Problem Handler

**A Generic library for handling exceptions in Spring Boot applications**, 
implementing specification [**`Problem Details (RFC7807) for HTTP APIs`**](https://datatracker.ietf.org/doc/html/rfc7807).
Requires Java 17+, Spring boot 3+ and Jakarta EE 10

![Exception Handling](https://miro.medium.com/v2/resize:fit:1400/format:webp/1*0s2E6-iNFqr_xptwrmJTdg.jpeg)

## Introduction

Exception handling is a cross-cutting concern, should be kept separate from business logic and applied declaratively. 

A common practice is to create some custom exception classes like some ServiceException and errors code enums, 
wherein each instance of error code enum represents an error scenario.
An exception class could be either checked or unchecked, but handling of exception is no different. 
For almost all error scenarios unchecked exception can serve the purpose really well, 
saving developers from explicitly writing `try` `catch` blocks and `throws` clauses. Though not recommended but 
limited checked exceptions can be created and thrown from methods where calling programs can take some recovery measures.

Standard way of handling exceptions in Spring is `@ControllerAdvice` using AOP, 
following the same principles **spring-boot-problem-handler** makes available everything related to exception handling 
for both **Spring Web** (Servlet) and **Spring Webflux** (Reactive) Rest applications, 
so there is no need to define any custom exceptions or custom `ControllerAdvice` advices into consumer application, 
all can be done with zero custom code but by specifying error details in `properties` file.

## Installation

> **Current version: 1.0**

Add the `spring-boot-problem-handler` jar to application dependencies. That is all it takes to get a default working 
exception handling mechanism in a Spring boot application.

```xml
<properties>
    <spring-boot-problem-handler.version>1.0</spring-boot-problem-handler.version>
</properties>
```

```xml
<dependency>
    <groupId>io.github.officiallysingh</groupId>
    <artifactId>spring-boot-problem-handler</artifactId>
    <version>${spring-boot-problem-handler.version}</version>
</dependency>
```

It does all hard part, A lot of advices are out of box available which are autoconfigured as `ControllerAdvice`s 
depending on the jars in classpath of consumer application. 
**Even for exceptions for which no advices are defined**, respective error response can be specified by 
messages in `properties` file, elaborated in [*Usage*](https://github.com/officiallysingh/spring-boot-problem-handler#usage) section.
New custom advices could be required only in cases where it is required to take some data from exception instance 
to dynamically derive [*Error key*](https://github.com/officiallysingh/spring-boot-problem-handler#error-key) 
or to use this data to resolve any placeholders in error message. In such cases consumer application can define 
their own custom `ControllerAdvice`'s,
Any existing advice can be referred to weave the custom advice into the framework.

> A default set of `ControllerAdvice`s are always configured irrespective of the fact that whether 
the application is Spring Web or Spring Webflux, but few advices are conditional 
such as for Handling Security, OpenAPI and Dao related exceptions, which are elaborated in their respective sections.

## Features

* A lot of inbuilt `ControllerAdvice`'s out of box available to handle most common exceptions.
* Extendable to add more advices or override existing advices in consumer applications, weaving them into aligned framework for exception handling.
* Customizable Error response structure.
* Provides mechanism to specify error response for any kind of exception without defining any `ControllerAdvice`.
* Works with both Spring Web and Spring Webflux applications.
* Customizable to override the default attributes in error response by overriding the same in `properties` file.
* The autoconfigured advices can be disabled or overridden or extended as per needs.

## Controller Advices

#### General advices recommended for all Spring Rest services

These advices are autoconfigured as either bean of type [**`ProblemHandlingWeb`**](src/main/java/com/ksoot/problem/spring/advice/web/ProblemHandlingWeb.java) 
or [**`ProblemHandlingWebflux`**](src/main/java/com/ksoot/problem/spring/advice/webflux/ProblemHandlingWebflux.java) depending on whether application is type **Spring Web** or **Spring Webflux** respectively.

| General Advice Traits                                                                                                                                         | Produces                                                    | Error Key                                                           |
|---------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------|---------------------------------------------------------------------|
| [**`ApplicationAdviceTraits`**](src/main/java/com/ksoot/problem/spring/advice/application/ApplicationAdviceTraits.java)                                       |                                                             |                                                                     |
| `├──`[`ApplicationProblemAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/application/ApplicationProblemAdviceTrait.java)                          | *depends*                                                   | Provided by application while throwing exception                    |
| `├──`[`ApplicationExceptionAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/application/ApplicationExceptionAdviceTrait.java)                      | *depends*                                                   | Provided by application while throwing exception                    |
| `└──`[ `ApplicationMultiProblemAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/application/ApplicationMultiProblemAdviceTrait.java)               | *depends*                                                   | Provided by application while throwing exception                    |
| [**`GeneralAdviceTraits`**](src/main/java/com/ksoot/problem/spring/advice/general/GeneralAdviceTraits.java)                                                   |                                                             |                                                                     |
| `├──`[`ProblemAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/general/ProblemAdviceTrait.java)                                                    | [`500 Internal Server Error`](https://httpstatus.es/500)    | internal.server.error                                               |
| `├──`[`ThrowableAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/general/ThrowableAdviceTrait.java)                                                | [`500 Internal Server Error`](https://httpstatus.es/500)    | internal.server.error                                               |
| `└──`[ `UnsupportedOperationAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/general/UnsupportedOperationAdviceTrait.java)                         | [`501 Not Implemented`](https://httpstatus.es/501)          | java.lang.UnsupportedOperationException                             |
| [**`HttpAdviceTraits`**](src/main/java/com/ksoot/problem/spring/advice/http/HttpAdviceTraits.java)                                                            |                                                             |                                                                     |
| `├──`[`HttpMediaTypeNotAcceptableAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/http/HttpMediaTypeNotAcceptableAdviceTrait.java)                 | [`415 Unsupported Media Type`](https://httpstatus.es/415)   | media.type.not.acceptable                                           |
| `├──`[`HttpMediaTypeNotSupportedExceptionAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/http/HttpMediaTypeNotSupportedExceptionAdviceTrait.java) | [`415 Unsupported Media Type`](https://httpstatus.es/415)   | media.type.not.supported                                            |
| `├──`[`UnsupportedMediaTypeStatusAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/http/UnsupportedMediaTypeStatusAdviceTrait.java)                 | [`415 Unsupported Media Type`](https://httpstatus.es/415)   | media.type.not.supported                                            |
| `├──`[`HttpRequestMethodNotSupportedAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/http/http/HttpRequestMethodNotSupportedAdviceTrait.java)      | [`405 Method Not Allowed`](https://httpstatus.es/405)       | request.method.not.supported                                        |
| `├──`[`MethodNotAllowedAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/http/MethodNotAllowedAdviceTrait.java)                                     | [`405 Method Not Allowed`](https://httpstatus.es/405)       | method.not.allowed                                                  |
| `├──`[`NotAcceptableStatusAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/http/NotAcceptableStatusAdviceTrait.java)                               | [`406 Not Acceptable`](https://httpstatus.es/406)           | org.springframework.web.server.NotAcceptableStatusException         |
| `└──`[`ResponseStatusAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/http/ResponseStatusAdviceTrait.java)                                         | *depends*                                                   |                                                                     |
| [**`IOAdviceTraits`**](src/main/java/com/ksoot/problem/spring/advice/io/IOAdviceTraits.java)                                                                  |                                                             |                                                                     |
| `├──`[`MessageNotReadableAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/io/MessageNotReadableAdviceTrait.java)                                   | [`400 Bad Request`](https://httpstatus.es/400)              | *Derived* from exception                                            |
| `├──`[`MultipartAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/io/MultipartAdviceTrait.java)                                                     | [`400 Bad Request`](https://httpstatus.es/400)              | org.springframework.web.multipart.MultipartException                |
| `└──`[`MultipartAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/io/MaxUploadSizeExceededExceptionAdviceTrait.java)                                | [`400 Bad Request`](https://httpstatus.es/400)              | org.springframework.web.multipart.MaxUploadSizeExceededException    |
| [**`RoutingAdviceTraits`**](src/main/java/com/ksoot/problem/spring/advice/routing/RoutingAdviceTraits.java)                                                   |                                                             |                                                                     |
| `├──`[`MissingRequestHeaderAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/routing/MissingRequestHeaderAdviceTrait.java)                          | [`400 Bad Request`](https://httpstatus.es/400)              | *Derived* from exception                                            |
| `├──`[`MissingServletRequestParameterAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/routing/MissingServletRequestParameterAdviceTrait.java)      | [`400 Bad Request`](https://httpstatus.es/400)              | *Derived* from exception                                            |
| `├──`[`MissingServletRequestPartAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/routing/MissingServletRequestPartAdviceTrait.java)                | [`400 Bad Request`](https://httpstatus.es/400)              | *Derived* from exception                                            |
| `├──`[`NoHandlerFoundAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/routing/NoHandlerFoundAdviceTrait.java)                                      | [`404 Not Found`](https://httpstatus.es/404)                | no.handler.found                                                    |
| `└──`[`ServletRequestBindingAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/routing/ServletRequestBindingAdviceTrait.java)                        | [`400 Bad Request`](https://httpstatus.es/400)              | org.springframework.web.bind.ServletRequestBindingException         |
| [**`ValidationAdviceTraits`**](src/main/java/com/ksoot/problem/spring/advice/validation/ValidationAdviceTraits.java)                                          |                                                             |                                                                     |
| `├──`[`ConstraintViolationAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/validation/ConstraintViolationAdviceTrait.java)                         | [`400 Bad Request`](https://httpstatus.es/400)              | *Derived* from exception                                            |
| `├──`[`BindAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/validation/BindAdviceTrait.java)                                                       | [`400 Bad Request`](https://httpstatus.es/400)              | *Derived* from exception                                            |
| `├──`[`MethodArgumentNotValidAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/validation/MethodArgumentNotValidAdviceTrait.java)                   | [`400 Bad Request`](https://httpstatus.es/400)              | *Derived* from exception                                            |
| `├──`[`MethodArgumentTypeMismatchAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/validation/MethodArgumentTypeMismatchAdviceTrait.java)           | [`400 Bad Request`](https://httpstatus.es/400)              | *Derived* from exception                                            |
| `└──`[`TypeMismatchAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/validation/TypeMismatchAdviceTrait.java)                                       | [`400 Bad Request`](https://httpstatus.es/400)              | *Derived* from exception                                            |
| [`WebExchangeBindAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/webflux/WebExchangeBindAdviceTrait.java)                                         | [`400 Bad Request`](https://httpstatus.es/400)              | *Derived* from exception                                            |


**Composite advice traits**

| Spring Web Advice Traits                                                                                                  | Spring Webflux Advice Traits                                                                                   |
|---------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------|
| [**`ProblemHandlingWeb`**](src/main/java/com/ksoot/problem/spring/advice/web/ProblemHandlingWeb.java)                     | [**`ProblemHandlingWebflux`**](src/main/java/com/ksoot/problem/spring/advice/webflux/ProblemHandlingWebflux.java)   |
| `├──`[`GeneralAdviceTraits`](src/main/java/com/ksoot/problem/spring/advice/general/GeneralAdviceTraits.java)              | `├──`[`GeneralAdviceTraits`](src/main/java/com/ksoot/problem/spring/advice/general/GeneralAdviceTraits.java)              |
| `├──`[`HttpAdviceTraits`](src/main/java/com/ksoot/problem/spring/advice/http/HttpAdviceTraits.java)                       | `├──`[`HttpAdviceTraits`](src/main/java/com/ksoot/problem/spring/advice/http/HttpAdviceTraits.java)                       |
| `├──`[`IOAdviceTraits`](src/main/java/com/ksoot/problem/spring/advice/io/IOAdviceTraits.java)                             | `├──`[`IOAdviceTraits`](src/main/java/com/ksoot/problem/spring/advice/io/IOAdviceTraits.java)                             |
| `├──`[ `RoutingAdviceTraits`](src/main/java/com/ksoot/problem/spring/advice/routing/RoutingAdviceTraits.java)             | `├──`[`WebExchangeBindAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/webflux/WebExchangeBindAdviceTrait.java) |
| `├──`[ `ValidationAdviceTraits`](src/main/java/com/ksoot/problem/spring/advice/validation/ValidationAdviceTraits.java)    | `├──`[`ValidationAdviceTraits`](src/main/java/com/ksoot/problem/spring/advice/validation/ValidationAdviceTraits.java)     |
| `└──`[ `ApplicationAdviceTraits`](src/main/java/com/ksoot/problem/spring/advice/application/ApplicationAdviceTraits.java) | `└──`[`ApplicationAdviceTraits`](src/main/java/com/ksoot/problem/spring/advice/application/ApplicationAdviceTraits.java)  |


#### Dao advices

| Dao Advice Traits                                                                                                                        | Produces                                                  | Error Key                                              |
|------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------|--------------------------------------------------------|
| [**`DaoAdviceTraits`**](src/main/java/com/ksoot/problem/spring/advice/dao/DaoAdviceTraits.java)                                          |                                                           |                                                        | 
| `├──`[**`DataIntegrityViolationAdviceTrait`**](src/main/java/com/ksoot/problem/spring/advice/dao/DataIntegrityViolationAdviceTrait.java) | [`500 Internal Server Error`](https://httpstatus.es/500)  | data.integrity.violation.\<Failed DB constraint name\> |
| `└──`[**`DuplicateKeyExceptionAdviceTrait`**](src/main/java/com/ksoot/problem/spring/advice/dao/DuplicateKeyExceptionAdviceTrait.java)   | [`500 Internal Server Error`](https://httpstatus.es/500)  | data.integrity.violation.\<Failed DB constraint name\> |

These advices are autoconfigured as a bean `DaoExceptionHandler` if following conditions are true
* `problem.dao-advice-enabled` is not set to `false`. Its default value is `true`
* If using relation databases then `spring-data-jpa` jar is detected in classpath and either `spring.datasource.url` or `spring.r2dbc.url` is configured
* If using MongoDB then `spring-data-mongodb` jar is detected in classpath and `spring.data.mongodb.uri` is configured

> [!NOTE]
> Database type must be specified in `application.properties` in case application is using some relational database, 
it is used to autoconfigure [**`ConstraintNameResolver`**](src/main/java/com/ksoot/problem/spring/advice/dao/ConstraintNameResolver.java) to extract database constraint name from exception message to derive [*Error key*](https://github.com/officiallysingh/spring-boot-problem-handler#error-key) 
when database constraint violation exceptions are thrown.

#### Security advices

| Security Advice Traits                                                                                                                                | Produces                                        | Error Key              |
|-------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------|------------------------|
| [**`SecurityAdviceTraits`**](src/main/java/com/ksoot/problem/spring/advice/security/SecurityAdviceTraits.java)                                        |                                                 |                        |
| `├──`[**`AuthenticationAdviceTrait`**](src/main/java/com/ksoot/problem/spring/advice/security/AuthenticationAdviceTrait.java)                         | [`401 Unauthorized`](https://httpstatus.es/401) | security.unauthorized  |
| `├──`[**`InsufficientAuthenticationAdviceTrait`**](src/main/java/com/ksoot/problem/spring/advice/security/InsufficientAuthenticationAdviceTrait.java) | [`401 Unauthorized`](https://httpstatus.es/401) | security.unauthorized  |
| `└──`[**`AccessDeniedAdviceTrait`**](src/main/java/com/ksoot/problem/spring/advice/security/AccessDeniedAdviceTrait.java)                             | [`403 Forbidden`](https://httpstatus.es/403)    | security.access.denied |

These advices are autoconfigured as a bean `SecurityExceptionHandler` if following conditions are true
* `spring-security-config` jar is detected in classpath
* `problem.security-advice-enabled` is not set to `false`. Its default value is `true`

> **For Spring Web applications** 
[**`ProblemAuthenticationEntryPoint`**](src/main/java/com/ksoot/problem/spring/advice/security/ProblemAuthenticationEntryPoint.java)
and [**`ProblemAccessDeniedHandler`**](src/main/java/com/ksoot/problem/spring/advice/security/ProblemAccessDeniedHandler.java) 
are autoconfigured as `authenticationEntryPoint` and `accessDeniedHandler` beans respectively. 

But to make it work following needs to be done in application's Spring Security configuration. 
Refer to example [**`SecurityConfiguration`**](https://github.com/officiallysingh/problem-handler-web-demo/blob/main/src/main/java/com/ksoot/problem/demo/config/SecurityConfiguration.java)
```java
@Autowired
private AuthenticationEntryPoint authenticationEntryPoint;

@Autowired
private AccessDeniedHandler accessDeniedHandler

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // Your security configurations
    //http.csrf().disable()
    //.authorizeHttpRequests....
    // Security configurations......

    if(this.authenticationEntryPoint != null) {
        http.exceptionHandling().authenticationEntryPoint(this.authenticationEntryPoint);
    }
    if(this.accessDeniedHandler != null) {
        http.exceptionHandling().accessDeniedHandler(this.accessDeniedHandler);
    }
        
    return http.build();
}
```

> **For Spring Webflux applications** 
[**`ProblemServerAuthenticationEntryPoint`**](src/main/java/com/ksoot/problem/spring/advice/security/ProblemServerAuthenticationEntryPoint.java)
and [**`ProblemServerAccessDeniedHandler`**](src/main/java/com/ksoot/problem/spring/advice/security/ProblemServerAccessDeniedHandler.java)
are autoconfigured as `authenticationEntryPoint` and `accessDeniedHandler` beans respectively.

But to make it work following needs to be done in application Spring Security configuration. 
Refer to example [**`SecurityConfiguration`**](https://github.com/officiallysingh/problem-handler-webflux-demo/blob/main/src/main/java/com/ksoot/problem/demo/config/SecurityConfiguration.java)
```java
@Autowired
private ServerAuthenticationEntryPoint authenticationEntryPoint;

@Autowired
private ServerAccessDeniedHandler accessDeniedHandler

@Bean
SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) {
    // Your security configurations
    //http.csrf().disable().authorizeExchange()
    //.pathMatchers....

    if(this.authenticationEntryPoint != null) {
        http.exceptionHandling().authenticationEntryPoint(this.authenticationEntryPoint);
    }
    if(this.accessDeniedHandler != null) {
        http.exceptionHandling().accessDeniedHandler(this.accessDeniedHandler);
    }
    return http.build();
}
```

#### OpenAPI validation advice

| OpenAPI Validation Advice Traits                                                                                                   | Produces                                        | Error Key                  |
|------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------|----------------------------|
| [**`OpenApiValidationAdviceTrait`**](src/main/java/com/ksoot/problem/spring/advice/validation/OpenApiValidationAdviceTrait.java)   | [`400 Bad Request`](https://httpstatus.es/400)  | *Derived* from exception   |

These advices are autoconfigured as bean 
[**`OpenApiValidationExceptionHandler`**](src/main/java/com/ksoot/problem/spring/boot/autoconfigure/web/OpenApiValidationExceptionHandler.java) if following conditions are true
* `swagger-request-validator-spring-webmvc-2.34.x.jar` is detected in classpath 
* At least one of `problem.open-api.req-validation-enabled` or `problem.open-api.res-validation-enabled` is set as `true`
* A valid OpenAPI Spec is provided as config `problem.open-api.path`

> [!NOTE]
> It is available for Spring Web applications only, not for Spring Webflux application

## Configurations

The [`NoHandlerFoundAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/routing/NoHandlerFoundAdviceTrait.java)
in addition also requires the following configuration:

```properties
spring.mvc.throw-exception-if-no-handler-found=true
```

While using Dao advices, set database platform as follows, set value as per the database being used.
```properties
spring.jpa.database=POSTGRESQL
```
Refer to [`Database`](src/main/java/com/ksoot/problem/spring/advice/dao/Database.java) for the list of database vendors such as 
`DB2`, `DERBY`, `H2`, `HANA`, `HSQL`, `INFORMIX`, `MYSQL`, `ORACLE`, `POSTGRESQL`, `SQL_SERVER`, `SYBASE`

> [!NOTE]
> [**`ConstraintNameResolver`**](src/main/java/com/ksoot/problem/spring/advice/dao/ConstraintNameResolver.java) is implemented for Postgres, SQL Server and MongoDB only as of now.
If any other relational database is used then respective [**`ConstraintNameResolver`**](src/main/java/com/ksoot/problem/spring/advice/dao/ConstraintNameResolver.java) need to be implemented and defined as a bean.

Make sure to disable the `ErrorMvcAutoConfiguration` as follows

```java
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
```
or in `application.properties` as follows
```properties
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
```

Enable Spring boot Problem details support to return similar error response in case `spring-boot-problem-handler` 
throws some exception while handling an exception
```properties
spring.mvc.problemdetails.enabled=true
```

Specify message source bundles as follows. Make sure to include `i18/problems` bundled in the library, as it 
has default messages for certain exception. And it should be last in the list of `basenames`, 
so that it has lowest priority and any default messages coming from `problems.properties` can be overridden 
by specifying the property with different value in application's `errors.properties`
```properties
spring.messages.basename=i18n/errors,i18/problems
spring.messages.use-code-as-default-message=true
```
if `use-code-as-default-message` is set to `false` and the message is not found in any of the `properties` file 
then it will throw `NoSuchMessageException` complaining that no message is found for given code. 
So if it is intended to enforce all messages for exceptions to be specified in `properties` file, set it to `false`, 
but not recommended.
To be on safer side, it's recommended to keep it `true`, in that case if some message is not found, 
the message key is taken as its value, which can be updated later into `properties` file, once noticed.

## Problem Properties
**Following are the configurations** to customize default behaviour of `spring-boot-problem-handler`.
```properties
problem.enabled=true
problem.type-url=http://localhost:8080/problems/help.html
problem.debug-enabled=false
problem.stacktrace-enabled=false
problem.cause-chains-enabled=false
#problem.jackson-module-enabled=false
#problem.dao-advice-enabled=false
#problem.security-advice-enabled=false
problem.open-api.path=/oas/api.json
problem.open-api.exclude-patterns=/api/states,/api/states/**,/api/employees,/api/employees/**,/problems/**
problem.open-api.req-validation-enabled=true
problem.open-api.res-validation-enabled=false
```

* `problem.enabled`:- To enable or disable autoconfigurations, default is `true`. 
  In case consumer applications are interested to avail advices but want full control over configurations, 
  then it can be set to `false` and required advices can be configured as Spring beans similar to how they are autoconfigured.
* `problem.type-url`:- The base `URL` for **Help page** describing errors. For different exceptions respective code for exception is appended to it followed by a `#`
* `problem.debug-enabled`:- To enable or disable debugging i.e. to get the message resolvers to specify the error messages in `properties` files. 
  Elaborated in [*Usage*](https://github.com/officiallysingh/spring-boot-problem-handler#usage) section. Default is `false`.
* `problem.stacktrace-enabled`:- To enable or disable Stacktraces, default is `false`. 
  Should only be set to `true` for debugging purposes only on local or lower environments, otherwise the application internals may be exposed.
* `problem.cause-chains-enabled`:- To enable or disable cause chains, default is `false`. 
  Elaborated in [*Usage*](https://github.com/officiallysingh/spring-boot-problem-handler#usage) section.
* `problem.jackson-module-enabled`:- To enable or disable Jackson Problem Module autoconfiguration, default is `true`.
  Set it to `false` in case consumer application need to define Serialization/Deserialization explicitly. 
  Or if `Gson` is to be used instead of `Jackson`. If disabled the required serializers need to be defined by consumer application.
* `problem.dao-advice-enabled`:- To enable or disable Dao advice autoconfiguration, default is `true`. 
  Set it to `false` in case consumer application need to define Dao advice configurations explicitly.
* `problem.security-advice-enabled`:- To enable or disable Security advice autoconfiguration, default is `true`.
  Set it to `false` in case consumer application need to define Security advice configurations explicitly.
* `problem.open-api.path`:- OpenAPI Specification path. Ideally should be in classpath and start with`/`.
  If not specified, OpenAPI Specification validation is not enabled.
* `problem.open-api.exclude-patterns`:- List of `URI` Ant patterns to be excluded from OpenAPI specification validation. Default is empty.
* `problem.open-api.req-validation-enabled`:- To enable or disable OpenAPI specification validation for request, default is `false`.
* `problem.open-api.res-validation-enabled`:- To enable or disable OpenAPI specification validation for response, default is `false`.


## Error Key
The main concept behind specifying the error attributes in `properties` file is **Error key**, which is mandatory to be unique for each error scenario.
**It is either derived or specified by application** while throwing exception and used to externalize the error attributes in `properties` file. 

For example if error key for some exception is `some.error.key`, then error response attributes can be specified in `properties` file as follows.
```properties
code.some.error.key=some-error
title.some.error.key=Some Error
detail.some.error.key=Something has gone wrong, please look into the logs for details
```
In case of exceptions for which advices are not defined, status also need to be specified in `properties` file as follows. It is elaborated in below sections.
```properties
status.some.error.key=400
```

> [!WARNING]
> The derived Error keys may change in cases of code refactoring.
> Because derived Error keys may contain the class names, method names and class property names or database constraint or index name.
> So in such case verify and do necessary updates in error message `properties` files.

* When OpenAPI Spec is changed, the error keys for OpenAPI spec validation errors may change.
* When controller method name changes or controller argument Object class name or any of its property name changes then `jakarta.validation.*` violation error keys may change.
* When database constraint name or index name changes then any `DuplicateKeyException` or `DataIntegrityViolationException` error key may change.

## Error response
Following is an example response body for an error.
```json
{
  "type":"http://localhost:8080/problems/help.html#XYZ-001",
  "title":"Internal Server Error",
  "status":500,
  "detail":"A job instance already exists and is complete for parameters={'date':'{value=2023-08-13, type=class java.time.LocalDate, identifying=true}'}.  If you want to run this job again, change the parameters.",
  "instance":"/api/myjob",
  "method":"PUT",
  "timestamp":"2023-08-14T20:45:45.737227+05:30",
  "code":"XYZ-001"
}
```
Response Header when service is configured for Json `HttpMessageConverters`
```json
content-type: application/problem+json
```
Response Header when service is configured for XML `HttpMessageConverters`
```json
content-type: application/problem+xml
```

**Description**
* `type`:- A `URI` reference that identifies the problem type.  When dereferenced, it provides human-readable documentation for this error.
  If not set `about:blank` is taken as default.
* `title`:- A short, human-readable summary of the error such as `Bad Request`.
* `status`:- The HTTP status code, int value such as `500`.
* `detail`:- A human-readable explanation specific to this occurrence of error.
* `instance`:- The API `URI` reference where this error has occurred.
* `method`:- `HttpMethod` for given `instance` where this error has occurred.
* `timestamp`:- `OffsetDateTime` of occurrence of this error.
* `code`:- Unique `String` code for this error, should not contain spaces or special characters except '_' and '-'. 
  Used in `type`. Commonly used to set unique codes for different business error scenarios.


## Message resolvers
To know how to define the error attributes in properties file, enable debugging as follows.
```properties
problem.debug-enabled=true
```
Now the error response itself would contain the resolvers for respective attributes, as follows.
`codes` in the resolvers could be one or multiple. 
For example in case of `ConstraintViolationException` `codes` would be multiple in order of most specific towards least specific.
```json
{
  "type":"http://localhost:8080/problems/help.html#XYZ-001",
  "title":"Internal Server Error",
  "status":500,
  "detail":"A job instance already exists and is complete for parameters={'date':'{value=2023-08-13, type=class java.time.LocalDate, identifying=true}'}.  If you want to run this job again, change the parameters.",
  "instance":"/api/myjob",
  "method":"PUT",
  "timestamp":"2023-08-14T20:51:43.993249+05:30",
  "code":"XYZ-001",
  "codeResolver":{
    "codes":[
      "code.org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException"
    ],
    "defaultMessage":"500",
    "arguments":null
  },
  "titleResolver":{
    "codes":[
      "title.org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException"
    ],
    "defaultMessage":"Internal Server Error",
    "arguments":null
  },
  "detailResolver":{
    "codes":[
      "detail.org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException"
    ],
    "defaultMessage":"A job instance already exists and is complete for parameters={'date':'{value=2023-08-13, type=class java.time.LocalDate, identifying=true}'}.  If you want to run this job again, change the parameters.",
    "arguments":null
  },
  "statusResolver":{
    "codes":[
      "status.org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException"
    ],
    "defaultMessage":"500",
    "arguments":null
  }
}
```
Respective codes for corresponding attribute can be copied and message can be specified for same in `properties` file.

> [!NOTE]
> `org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException` i.e. fully qualified name of exception is the [*Error key*](https://github.com/officiallysingh/spring-boot-problem-handler#error-key) in above case.
 **This scenario also covers all the exceptions for which advices are not defined**.
But additionally `HttpStatus` need to be specified in `properties` file as it has not been specified anywhere in code because `ControllerAdvice` is not defined,
if status not given even in `properties` file `HttpStatus.INTERNAL_SERVER_ERROR` is taken as default.
Hence the error response can be specified as follows.

```properties
status.org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException=409
code.org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException=Some code
title.org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException=Some title
detail.org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException=Some message details
```

To minimize the number of properties following defaults are taken if `HttpStatus` is specified as `status.`(error key) property.
* **Code** is taken as specified `HttpStatus`'s int code e.g. if `HttpStatus` is given as `EXPECTATION_FAILED` then the Code default would be `417`
* **Title** is taken as specified `HttpStatus`'s reason phrase e.g. if `HttpStatus` is given as `EXPECTATION_FAILED` then the Title default would be `Expectation Failed`
* **Detail** default is taken from thrown exception's `exception.getMessage()`.

> [!NOTE]
> `status.`(error key) property is considered only for exceptions where no explicit advice is defined, 
otherwise `HttpStatus` is specified in the java code.

## Creating and throwing exceptions

Apart from exceptions thrown by frameworks or java, every application need to throw custom exceptions.
[**`ApplicationProblem`**](src/main/java/com/ksoot/problem/core/ApplicationProblem.java) and
[**`ApplicationException`**](src/main/java/com/ksoot/problem/core/ApplicationException.java) 
classes are available in the library to throw an unchecked or checked exception respectively.

> [**`Problems`**](src/main/java/com/ksoot/problem/Problems.java) **is the central static helper class to create 
Problem instances and throw either checked or unchecked exceptions**, as demonstrated below.
It provides multiple fluent methods to build and throw exceptions.

The simplistic way is to just specify a unique error key and `HttpStatus`.
```java
throw Problems.newInstance("sample.problem").throwAble(HttpStatus.EXPECTATION_FAILED);
```
Error response attributes `code`, `title` and `detail` are expected from the message source (`properties` file) available as follows.
Notice the [*Error key*](https://github.com/officiallysingh/spring-boot-problem-handler#error-key) **sample.problem** in following properties

```properties
code.sample.problem=AYX123
title.sample.problem=Some title
detail.sample.problem=Some message details
```

But exceptions come with some default attributes as follows, to minimize the number of properties required to be defined in `properties` file

If the messages are not found in `properties` files, defaults are taken as follows.
* **Code** is taken as specified `HttpStatus`'s int code e.g. if `HttpStatus` is given as `EXPECTATION_FAILED` then the Code default would be `417`
* **Title** is taken as specified `HttpStatus`'s reason phrase e.g. if `HttpStatus` is given as `EXPECTATION_FAILED` then the Title default would be `Expectation Failed`
* **Detail** default is taken as thrown exception's `exception.getMessage()`

There are multiple other methods available while creating and throwing exceptions in [**`Problems`**](src/main/java/com/ksoot/problem/Problems.java), 
for details refers to its source code and java docs. 
```java
throw Problems.newInstance("sample.problem")
    .defaultDetail("Default details if not found in properties file with parma1: {0} and param2: {1}")
    .detailArgs("P1", "P2")
    .cause(new IllegalStateException("Artificially induced illegal state"))
    .throwAble(HttpStatus.EXPECTATION_FAILED); // .throwAbleChecked(HttpStatus.EXPECTATION_FAILED)
```
The above code snippet would throw unchecked exception, though not recommended but to throw checked exception,
use `throwAbleChecked` as terminal operation as highlighted in java comment above.

The attributes corresponding to error key `sample.problem` can be provided in `properties` file as follows.
```properties
code.sample.problem=404
title.sample.problem=Some title
detail.sample.problem=Some details with param one: {0} and param other: {1}
```

Sometimes it is not desirable to throw exceptions as they occur, but to collect them to throw at a later point in execution.
Or to throw multiple exceptions together.That can be done as follows.
```java
Problem problemOne = Problems.newInstance("sample.problem.one").get();
Problem problemTwo = Problems.newInstance("sample.problem.two").get();
throw Problems.throwAble(HttpStatus.MULTI_STATUS, problemOne, problemTwo);
```

`HttpStatus` can also be set over custom exception as follows, the same would reflect in error response and 
other error attributes default would be derived by given `HttpStatus` attribute in `@ResponseStatus`
```java
@ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
private static final class MyException extends RuntimeException {
    public MyException() {
    }

    public MyException(final Throwable cause) {
        super(cause);
    }
}
```

## Stack traces
Set following property to `true` to get the `stacktrace` in error response, 
should only be used on local for debugging purpose and strictly prohibited elsewhere as it may expose application internals.
```properties
problem.stacktrace-enabled=true
```
Example response
```json
{
  "type":"http://localhost:8080/problems/help.html#XYZ-001",
  "title":"Internal Server Error",
  "status":500,
  "detail":"A job instance already exists and is complete for parameters={'date':'{value=2023-08-13, type=class java.time.LocalDate, identifying=true}'}.  If you want to run this job again, change the parameters.",
  "instance":"/api/myjob",
  "method":"PUT",
  "timestamp":"2023-08-14T21:01:56.378749+05:30",
  "code":"XYZ-001",
  "statcktrace":[
    "org.springframework.batch.core.repository.support.SimpleJobRepository.createJobExecution(SimpleJobRepository.java:159)",
    "java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)",
    "java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)",
    "java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)",
    "java.base/java.lang.reflect.Method.invoke(Method.java:568)",
    ".......",
    "..............",
    "org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)",
    "org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)",
    "java.base/java.lang.Thread.run(Thread.java:833)"
  ]
}
```

## Cause chains
An exception may have a cause, which in tern may also have another and so on.
The complete cause chain can also be viewed in error response, again it should just be used for local debugging purposes only.

```properties
problem.cause-chains-enabled=true
```
Example response
```json
{
  "type":"http://localhost:8080/problems/help.html#XYZ-001",
  "title":"Not Implemented",
  "status":501,
  "detail":"expected",
  "instance":"/problems/handler-throwable-annotated-cause",
  "method":"GET",
  "timestamp":"2023-08-14T22:09:56.284473+05:30",
  "code":"XYZ-001",
  "cause":{
    "code":"501",
    "title":"Not Implemented",
    "detail":"Something has gone wrong",
    "cause":{
      "code":"501",
      "title":"Not Implemented"
    }
  }
}
```

## Customizations
### Customize error response
The error response is totally customizable by defining a bean of type [**`ErrorResponseBuilder`**](src/main/java/com/ksoot/problem/core/ErrorResponseBuilder.java) demonstrated as follows.
* If it is required to customize the error response attribute names, it can be done by implementing custom serialization for `ProblemDetail` using Jackson Mixin.
* Or define custom error response class as follows.
```java
@Getter
@AllArgsConstructor(staticName = "of")
public class CustomErrorResponse {
    private HttpStatus status;
    private String message;
}
```
* And define custom error response builder class bean to return the custom error response as follows.

> For Spring Web applications
```java
@Component
class CustomErrorResponseBuilder implements ErrorResponseBuilder<NativeWebRequest, ResponseEntity<CustomErrorResponse>> {

    @Override
    public ResponseEntity<CustomErrorResponse> buildResponse(final Throwable throwable, final NativeWebRequest request,
                                                           final HttpStatus status, final HttpHeaders headers, final Problem problem) {
        CustomErrorResponse errorResponse = CustomErrorResponse.of(status, problem.getDetail());
        ResponseEntity<CustomErrorResponse> responseEntity = ResponseEntity
            .status(status).headers(headers).contentType(MediaTypes.PROBLEM).body(errorResponse);
        return responseEntity;
    }
}
```

> For Spring Webflux applications
```java
@Component
class CustomErrorResponseBuilder implements ErrorResponseBuilder<ServerWebExchange, Mono<ResponseEntity<CustomErrorResponse>>> {

    @Override
    public Mono<ResponseEntity<CustomErrorResponse>> buildResponse(final Throwable throwable, final ServerWebExchange request,
                                                           final HttpStatus status, final HttpHeaders headers, final Problem problem) {
        CustomErrorResponse errorResponse = CustomErrorResponse.of(status, problem.getDetail());
        ResponseEntity<CustomErrorResponse> responseEntity = ResponseEntity
            .status(status).headers(headers).contentType(MediaTypes.PROBLEM).body(errorResponse);
        return Mono.just(responseEntity);
    }
}
```

### Customize or Override advices
Any autoconfigured advice can be customized by overriding the same and providing a different implementation. 
Make sure to add annotation `@Order(Ordered.HIGHEST_PRECEDENCE)` over the class, 
It makes this handler to take precedence over the fallback advice which handles `Throwable` i.e. for all exceptions for which no `ControllerAdvice`s are defined.

> For Spring Web applications
```java
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE) // Important to note
class CustomMethodArgumentNotValidExceptionHandler implements MethodArgumentNotValidAdviceTrait<NativeWebRequest, ResponseEntity<ProblemDetail>> {

    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValid(final MethodArgumentNotValidException exception, final NativeWebRequest request) {
        List<String> violations = processBindingResult(exception.getBindingResult());
        final String errors = violations.stream()
            .collect(Collectors.joining(", "));
        Problem problem = Problem.code(ProblemUtils.statusCode(HttpStatus.BAD_REQUEST)).title(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .detail(errors).build();
        return create(exception, request, HttpStatus.BAD_REQUEST,
            problem);
    }

    List<String> processBindingResult(final BindingResult bindingResult) {
        final List<String> fieldErrors =
            bindingResult.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();
        final List<String> globalErrors =
            bindingResult.getGlobalErrors().stream()
                .map(
                    objectError ->
                        objectError.getObjectName() + ": " + objectError.getDefaultMessage())
                .toList();

        final List<String> errors = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(fieldErrors)) {
            errors.addAll(fieldErrors);
        }
        if (CollectionUtils.isNotEmpty(globalErrors)) {
            errors.addAll(globalErrors);
        }
        return errors;
    }
}
```

> For Spring Webflux applications
```java
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE) // Important to note
class CustomMethodArgumentNotValidExceptionHandler implements MethodArgumentNotValidAdviceTrait<ServerWebExchange, Mono<ResponseEntity<ProblemDetail>>> {

  public Mono<ResponseEntity<ProblemDetail>> handleMethodArgumentNotValid(final MethodArgumentNotValidException exception, final ServerWebExchange request) {
    // It remains the same as implemented for Spring web, above
  }
}
```

## Define new advices
There should not be any need to create any custom exception hence new advices, but if there is a pressing need to do so,
custom exception can be created and corresponding custom `ControllerAdvice` can be defined for the same, though not recommended.
Following example demonstrates a new advice for some custom exception `MyCustomException`.

> For Spring Web applications
```java
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE) // Important to note
public class MyCustomAdvice implements AdviceTrait<NativeWebRequest, ResponseEntity<ProblemDetail>> {

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handleMyCustomException(final MyCustomException exception, final NativeWebRequest request) {
        // Custome logic to set the error response 
        Problem problem = Problem.code(String.valueOf(HttpStatus.BAD_REQUEST.value())).title(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .detail(exception.getMessage).build();
        return create(exception, request, HttpStatus.BAD_REQUEST,
            problem);
    }
}
```

> For Spring Webflux applications
```java
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE) // Important to note
public class MyCustomAdvice implements AdviceTrait<ServerWebExchange, Mono<ResponseEntity<ProblemDetail>>> {
    
    @ExceptionHandler
    public Mono<ResponseEntity<ProblemDetail>> handleMyCustomException(final MyCustomException exception, final ServerWebExchange request) {
        // It remains the same as implemented for Spring web, above
    }
}
```

## Licence
Open source [**`The MIT License`**](http://www.opensource.org/licenses/mit-license.php)

## Authors and acknowledgment
[**`Rajveer Singh`**](https://www.linkedin.com/in/rajveer-singh-589b3950/), In case you find any issues or need any support, please email me at raj14.1984@gmail.com

## Credits and references
Inspired and taken base code from [**`Zalando Problem libraries`**](https://github.com/zalando/problem-spring-web)

Refer to [**`problem-handler-web-demo`**](https://github.com/officiallysingh/problem-handler-web-demo) and 
[**`problem-handler-webflux-demo`**](https://github.com/officiallysingh/problem-handler-webflux-demo) 
as examples to see usage in **Spring Web** and **Spring Webflux** application respectively.

## Known Issues
* If an application uses multiple vendor relational databases then the [**`ConstraintNameResolver`**](src/main/java/com/ksoot/problem/spring/advice/dao/ConstraintNameResolver.java) 
may not work properly, needs further testing. For example if it is using Postgres and SQL Server both.