# Problem Handler

A Generic library for handling exceptions in Spring web or webflux applications

## Getting started

Exception handling is a cross-cutting concern, should be kept separate from business logic and applied declaratively. 

A common practice is to create some custom exception classes like some ServiceException and errors code enums, 
wherein each instance of error code enum represents an error scenario.
An exception class could be either checked or unchecked, but handling of exception is no different. 
For almost all error scenarios unchecked exception can serve the purpose really well, 
saving developers from explicitly writing `try` `catch` blocks and `throws` clauses. Though not recommended but 
limited checked exceptions can be created and thrown from methods where calling programs can take some recovery measures.

Standard way of handling exceptions in Spring is `@ControllerAdvice` using AOP, 
following the same principles **problem-handler** makes available everything related to exception handling 
for both **Servlet** (Web) and **Reactive** (Webflux) Spring boot Rest applications, 
so there is no need to define any custom exceptions or custom `ControllerAdvice` advices into consumer application, 
all can be done with zero custom code but specifying error details in `properties` file.

## Installation

### Spring boot

Add the `problem-handler.jar` to application dependencies. That is all it takes to get a default working 
exception handling mechanism in an application
```xml
<dependency>
    <groupId>com.pchf.client</groupId>
    <artifactId>problem-handler</artifactId>
    <version>${problem.handler.version}</version>
</dependency>
```

Having `problem-handler.jar` in classpath does all hard part, A lot of advices are out of box available 
which are autoconfigures as `ControllerAdvice`s 
depending on the jars in classpath of consumer application. 
**Even for exceptions for which no advices are defined**, respective error response can be specified by 
messages in `properties` file, 
elaborated in *Usage* section.
New custom advices could be required only in cases where it is required to take some data from exception instance 
to dynamically derive **Error key** (Elaborated later)
or to use this data to resolve any placeholders in error message. In such cases consumer application can define 
their own custom `ControllerAdvice`s,
Any existing advice can be referred to weave the custom advice into the framework.

A default set of `ControllerAdvice`s are always configured irrespective of the fact that whether 
the application is **Servlet** (Web) or **Reactive** (Webflux), but few advices are conditional 
such as for Handling Security, OpenAPI and Dao related exceptions, which are elaborated in their respective sections.

## Features

* A lot of inbuilt `ControllerAdvice`s out of box to handling most common exceptions.
* Extendable to add more advices in consumer applications
* Provides mechanism to specify error response for any kind of exception without defining any `ControllerAdvice`
* Works with both Spring web and Spring webflux
* Customizable to override the default messages, 
* The autoconfigured advices can be disabled or overridden or extended as per needs

## Spring web

#### General advices all Spring Rest services need to handle

These advices are autoconfigured as either bean of class [**`ExceptionHandler`**](src/main/java/com/ksoot/problem/spring/boot/autoconfigure/web/ExceptionHandler.java) 
or [**`ProblemHandlingWebflux`**](src/main/java/com/ksoot/problem/spring/advice/webflux/advice/ProblemHandlingWebflux.java) depending on whether application is of type **Servlet** or **Reactive** respetively

| General Advice Traits                                                                                                                                                              | Produces                                                  | Error Key                                                   |
|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------|-------------------------------------------------------------|
| [**`ProblemHandlingWeb`**](src/main/java/com/ksoot/problem/spring/advice/web/advice/ProblemHandlingWeb.java)                                                                 |                                                           |                                                             |
| [**`ProblemHandlingWebflux`**](src/main/java/com/ksoot/problem/spring/advice/webflux/advice/ProblemHandlingWebflux.java)                                                     |                                                           |                                                             |
| `├──`[**`ApplicationAdviceTraits`**](src/main/java/com/ksoot/problem/spring/advice/application/ApplicationAdviceTraits.java)                                                 |                                                           |                                                             |
| `│   ├──`[`ApplicationProblemAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/application/ApplicationProblemAdviceTrait.java)                            | *depends*                                                 | Provided by application while throwing exception            |
| `│   ├──`[`ApplicationExceptionAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/application/ApplicationExceptionAdviceTrait.java)                        | *depends*                                                 | Provided by application while throwing exception            |
| `│   └──`[ `ApplicationMultiProblemAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/application/ApplicationMultiProblemAdviceTrait.java)                 | *depends*                                                 | Provided by application while throwing exception            |
| `├──`[**`GeneralAdviceTraits`**](src/main/java/com/ksoot/problem/spring/advice/general/GeneralAdviceTraits.java)                                                             |                                                           |                                                             |
| `│   ├──`[`ProblemAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/general/ProblemAdviceTrait.java)                                                      | [`500 Internal Server Error`](https://httpstatus.es/500)  | internal.server.error                                       |
| `│   ├──`[`ThrowableAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/general/ThrowableAdviceTrait.java)                                                  | [`500 Internal Server Error`](https://httpstatus.es/500)  | internal.server.error                                       |
| `│   └──`[ `UnsupportedOperationAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/general/UnsupportedOperationAdviceTrait.java)                           | [`501 Not Implemented`](https://httpstatus.es/501)        | java.lang.UnsupportedOperationException                     |
| `├──`[**`HttpAdviceTraits`**](src/main/java/com/ksoot/problem/spring/advice/http/HttpAdviceTraits.java)                                                                      |                                                           |                                                             |
| `│   ├──`[`HttpMediaTypeNotAcceptableAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/http/HttpMediaTypeNotAcceptableAdviceTrait.java)                   | [`415 Unsupported Media Type`](https://httpstatus.es/415) | media.type.not.acceptable                                   |
| `│   ├──`[`HttpMediaTypeNotSupportedExceptionAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/http/HttpMediaTypeNotSupportedExceptionAdviceTrait.java)   | [`415 Unsupported Media Type`](https://httpstatus.es/415) | media.type.not.supported                                    |
| `│   ├──`[`UnsupportedMediaTypeStatusAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/http/UnsupportedMediaTypeStatusAdviceTrait.java)                   | [`415 Unsupported Media Type`](https://httpstatus.es/415) | media.type.not.supported                                    |
| `│   ├──`[`HttpRequestMethodNotSupportedAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/http/http/HttpRequestMethodNotSupportedAdviceTrait.java)        | [`405 Method Not Allowed`](https://httpstatus.es/405)     | request.method.not.supported                                |
| `│   ├──`[`MethodNotAllowedAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/http/MethodNotAllowedAdviceTrait.java)                                       | [`405 Method Not Allowed`](https://httpstatus.es/405)     | method.not.allowed                                          |
| `│   ├──`[`NotAcceptableStatusAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/http/NotAcceptableStatusAdviceTrait.java)                                 | [`406 Not Acceptable`](https://httpstatus.es/406)         | org.springframework.web.server.NotAcceptableStatusException |
| `│   ├──`[`ResponseStatusAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/http/ResponseStatusAdviceTrait.java)                                           | *depends*                                                 |                                                             |
| `├──`[**`IOAdviceTraits`**](src/main/java/com/ksoot/problem/spring/advice/io/IOAdviceTraits.java)                                                                            |                                                           |                                                             |
| `│   ├──`[`MessageNotReadableAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/io/MessageNotReadableAdviceTrait.java)                                     | [`400 Bad Request`](https://httpstatus.es/400)            | *Derived* from exception                                    |
| `│   └──`[`MultipartAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/io/MultipartAdviceTrait.java)                                                       | [`400 Bad Request`](https://httpstatus.es/400)            | org.springframework.web.multipart.MultipartException        |
| `├──`[**`RoutingAdviceTraits`**](src/main/java/com/ksoot/problem/spring/advice/routing/RoutingAdviceTraits.java)                                                             |                                                           |                                                             |
| `│   ├──`[`MissingRequestHeaderAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/routing/MissingRequestHeaderAdviceTrait.java)                            | [`400 Bad Request`](https://httpstatus.es/400)            | *Derived* from exception                                    |
| `│   ├──`[`MissingServletRequestParameterAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/routing/MissingServletRequestParameterAdviceTrait.java)        | [`400 Bad Request`](https://httpstatus.es/400)            | *Derived* from exception                                    |
| `│   ├──`[`MissingServletRequestPartAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/routing/MissingServletRequestPartAdviceTrait.java)                  | [`400 Bad Request`](https://httpstatus.es/400)            | *Derived* from exception                                    |
| `│   ├──`[`NoHandlerFoundAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/routing/NoHandlerFoundAdviceTrait.java)                                        | [`404 Not Found`](https://httpstatus.es/404)              | no.handler.found                                            |
| `│   └──`[`ServletRequestBindingAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/routing/ServletRequestBindingAdviceTrait.java)                          | [`400 Bad Request`](https://httpstatus.es/400)            | org.springframework.web.bind.ServletRequestBindingException |
| `└──`[**`ValidationAdviceTraits`**](src/main/java/com/ksoot/problem/spring/advice/validation/ValidationAdviceTraits.java)                                                    |                                                           |                                                             |
| `    ├──`[`ConstraintViolationAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/validation/ConstraintViolationAdviceTrait.java)                        | [`400 Bad Request`](https://httpstatus.es/400)            | *Derived* from exception                                    |
| `    └──`[`BindAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/validation/BindAdviceTrait.java)                                                      | [`400 Bad Request`](https://httpstatus.es/400)            | *Derived* from exception                                    |
| `    └──`[`MethodArgumentNotValidAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/validation/MethodArgumentNotValidAdviceTrait.java)                  | [`400 Bad Request`](https://httpstatus.es/400)            | *Derived* from exception                                    |
| `    └──`[`MethodArgumentTypeMismatchAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/validation/MethodArgumentTypeMismatchAdviceTrait.java)          | [`400 Bad Request`](https://httpstatus.es/400)            | *Derived* from exception                                    |
| `    └──`[`TypeMismatchAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/validation/TypeMismatchAdviceTrait.java)                                      | [`400 Bad Request`](https://httpstatus.es/400)            | *Derived* from exception                                    |


#### Dao advices

| Dao Advice Traits                                                                                                                              | Produces                                                  | Error Key                                              |
|------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------|--------------------------------------------------------|
| [**`DaoAdviceTraits`**](src/main/java/com/ksoot/problem/spring/advice/dao/DaoAdviceTraits.java)                                          |                                                           |                                                        | 
| `├──`[**`DataIntegrityViolationAdviceTrait`**](src/main/java/com/ksoot/problem/spring/advice/dao/DataIntegrityViolationAdviceTrait.java) | [`500 Internal Server Error`](https://httpstatus.es/500)  | data.integrity.violation.\<Failed DB constraint name\> |
| `├──`[**`DuplicateKeyExceptionAdviceTrait`**](src/main/java/com/ksoot/problem/spring/advice/dao/DuplicateKeyExceptionAdviceTrait.java)   | [`500 Internal Server Error`](https://httpstatus.es/500)  | data.integrity.violation.\<Failed DB constraint name\> |

It is autoconfigured if `spring-data-jpa` or `spring-data-mongodb` jar is detected in classpath 
and `ConditionalOnWebApplication.Type` is `SERVLET`
Database type must be specified in `application.properties` in case application is using some relational database, 
it is used to autoconfigure [**`ConstraintNameResolver`**](src/main/java/com/ksoot/problem/spring/advice/dao/ConstraintNameResolver.java) 
to extract database constraint name from exception message to derive error key 
when `DataIntegrityViolationException` is thrown

#### Security advices

| Security Advice Traits                                                                                                                                      | Produces                                        | Error Key              |
|-------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------|------------------------|
| [**`SecurityAdviceTraits`**](src/main/java/com/ksoot/problem/spring/advice/security/SecurityAdviceTraits.java)                                        |                                                 |                        |
| `├──`[**`AuthenticationAdviceTrait`**](src/main/java/com/ksoot/problem/spring/advice/security/AuthenticationAdviceTrait.java)                         | [`401 Unauthorized`](https://httpstatus.es/401) | security.unauthorized  |
| `├──`[**`InsufficientAuthenticationAdviceTrait`**](src/main/java/com/ksoot/problem/spring/advice/security/InsufficientAuthenticationAdviceTrait.java) | [`401 Unauthorized`](https://httpstatus.es/401) | security.unauthorized  |
| `├──`[**`AccessDeniedAdviceTrait`**](src/main/java/com/ksoot/problem/spring/advice/security/AccessDeniedAdviceTrait.java)                             | [`403 Forbidden`](https://httpstatus.es/403)    | security.access.denied |


For **Servlet** (Web) applications
It is autoconfigured if `spring-security-config` jar is detected in classpath and `ConditionalOnWebApplication.Type` is `SERVLET`

[**`ProblemAuthenticationEntryPoint`**](src/main/java/com/ksoot/problem/spring/advice/security/ProblemAuthenticationEntryPoint.java)
and [**`ProblemAccessDeniedHandler`**](src/main/java/com/ksoot/problem/spring/advice/security/ProblemAccessDeniedHandler.java) 
are autoconfigured as `authenticationEntryPoint` and `accessDeniedHandler` beans respectively. 

But to make it work following needs to be done in application Spring Security configuration
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

For **Reactive** (Webflux) applications
It is autoconfigured if `spring-security-config` jar is detected in classpath and `ConditionalOnWebApplication.Type` is `REACTIVE`

[**`ServerAuthenticationEntryPoint`**](https://github.com/spring-projects/spring-security/blob/main/web/src/main/java/org/springframework/security/web/server/ServerAuthenticationEntryPoint.java)
and [**`ServerAccessDeniedHandler`**](https://github.com/spring-projects/spring-security/blob/main/web/src/main/java/org/springframework/security/web/server/authorization/ServerAccessDeniedHandler.java)
are autoconfigured as `authenticationEntryPoint` and `accessDeniedHandler` beans respectively.

But to make it work following needs to be done in application Spring Security configuration
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
**Note**: For `web-flux` need to test and verify once.

#### OpenAPI validation advice

| OpenAPI Validation Advice Traits                                                                                                       | Produces                                        | Error Key                  |
|----------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------|----------------------------|
| [**`OpenApiValidationAdviceTrait`**](src/main/java/com/ksoot/problem/spring/advice/validation/OpenApiValidationAdviceTrait.java) | [`400 Bad Request`](https://httpstatus.es/400)  | *Derived* from exception   |

[**`OpenApiValidationExceptionHandler`**](src/main/java/com/ksoot/problem/spring/boot/autoconfigure/web/OpenApiValidationExceptionHandler.java) is configured if `swagger-request-validator-spring-webmvc-2.34.x.jar` is detected in classpath 
and `ConditionalOnWebApplication.Type` is `SERVLET` and at least one of `problem.open-api.req-validation-enabled` or `problem.open-api.res-validation-enabled` is set as `true` in `application.properties`

**Note**: The same needs to be implemented and tested for **Reactive** (Webflux) applications

## Configurations

The [`NoHandlerFoundAdviceTrait`](src/main/java/com/ksoot/problem/spring/advice/routing/NoHandlerFoundAdviceTrait.java)
in addition also requires the following configuration:

```properties
spring.mvc.throw-exception-if-no-handler-found=true
```

While using ORM advices, set database platform as follows.
```properties
spring.jpa.database=POSTGRESQL
```
Refer to [`Database`](https://github.com/spring-projects/spring-framework/blob/main/spring-orm/src/main/java/org/springframework/orm/jpa/vendor/Database.java) for the list of database vendors.
DB2, DERBY, H2, HANA, HSQL, INFORMIX, MYSQL, ORACLE, POSTGRESQL, SQL_SERVER, SYBASE

**Note**: [**`ConstraintNameResolver`**](src/main/java/com/ksoot/problem/spring/advice/dao/ConstraintNameResolver.java) is implemented for Postgres, SQL Server and MongoDB only as of now.
If any other relational database is used then respective [**`ConstraintNameResolver`**](src/main/java/com/ksoot/problem/spring/advice/dao/ConstraintNameResolver.java) need to be implemented.

Make sure to disable the `ErrorMvcAutoConfiguration` as follows

```java
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
```
or in `application.properties` as follows
```properties
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
```

Specify message source bundles as follows. Make sure to include `i18/problems` bundled in the library, as it 
has default messages for certain exception. And it should be last in the list of `basenames`, 
so that it has lowest priority and any default messages coming from `problems.properties` can be overridden 
by specifying the property with different value in application's `errors.properties`
```properties
spring.messages.basename=i18n/errors,i18/problems
spring.messages.use-code-as-default-message=true
```
if `use-code-as-default-message` is set to false and the message is not found in any of the `properties` file 
then it will throw exception saying message not found with given key. 
So if it is intended to enforce all messages for exceptions to be specified in `properties` file, set it to `false`, 
but not recommended.
To be on safer side, it's recommended to keep it `true`, in that case if some message is not found, 
the message key is taken as its value, which can be updated later into property file, once noticed.

**Following are the main configurations** to customize default `problem-handler` behaviour.
```properties
# Can set to false if you want to disable problem handling, lets say for debugging any advices defined in any project
problem.enabled=true
# Very important configuration, set it to true to find where to put error details such as code, message etc. in properties file
problem.debug-enabled=false
# Set it to true to get the stacktrace in error response, should only be used on local for debugging purpose
problem.stacktrace-enabled=false
# Set it to true to get cause chains in error response, again for debugging purpose only
problem.cause-chains-enabled=false
```

To enable OpenAPI validation following properties need to be specified along with having `swagger-request-validator-spring-webmvc-2.34.x.jar` in classpath.
```properties
# Must start with /
problem.open-api.path=/oas/api.json
# To validate requests with OpenAPI Spec
problem.open-api.req-validation-enabled=true
# To validate responses with OpenAPI Spec
problem.open-api.res-validation-enabled=false
```
At least once of `req-validation-enabled` or `res-validation-enabled` must be true and `path` must be given with a valid Open API specification file 
to enable Open API validation exception. General practice is to validate request only, not response.

Refer to [**`ProblemProperties`**](src/main/java/com/ksoot/problem/spring/common/config/ProblemProperties.java)
to have a look at defaults for above `properties`

## Usage

Following is an example response for an error.
```json
{
  "instance": "/api/myjob",
  "method": "PUT",
  "requestTime": "2023-06-11T16:10:26.310178Z",
  "errorCount": 1,
  "errors": [
    {
      "code": "409",
      "title": "Conflict",
      "message": "A job with given parameters already completed",
      "details": "Please force restart the job"
    }
  ]
}
```
This message and details are being picked up from `properties` file.

To know how to define the error attributes in properties file, enable debugging as follows.
```properties
problem.debug-enabled=true
```
Now the error response itself would contain the resolvers for respective attributes, as follows.
```json
{
  "instance": "/api/myjob",
  "method": "PUT",
  "requestTime": "2023-06-11T17:14:03.52623Z",
  "errorCount": 1,
  "errors": [
    {
      "code": "409",
      "title": "Conflict",
      "message": "A job with given parameters already completed",
      "details": "Please force restart the job",
      "codeResolver": {
        "codes": [
          "code.org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException"
        ],
        "defaultMessage": "409",
        "arguments": null
      },
      "titleResolver": {
        "codes": [
          "title.org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException"
        ],
        "defaultMessage": "Conflict",
        "arguments": null
      },
      "messageResolver": {
        "codes": [
          "message.org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException"
        ],
        "defaultMessage": "A job instance already exists and is complete for parameters={'date':'{value=2023-06-10, type=class java.time.LocalDate, identifying=true}'}.  If you want to run this job again, change the parameters.",
        "arguments": null
      },
      "detailsResolver": {
        "codes": [
          "details.org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException"
        ],
        "defaultMessage": "A job instance already exists and is complete for parameters={'date':'{value=2023-06-10, type=class java.time.LocalDate, identifying=true}'}.  If you want to run this job again, change the parameters.",
        "arguments": null
      },
      "statusResolver": {
        "codes": [
          "status.org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException"
        ],
        "defaultMessage": "500",
        "arguments": null
      }
    }
  ]
}
```
Respective codes for corresponding attribute can be copied and message can be specified for same in `properties` file.

So in above case the **Error key** is `org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException` i.e. fully qualified name of exception
Hence the error response can be specified as follows.

```properties
status.org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException=409
code.org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException=Some code
title.org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException=Some title
message.org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException=Some message
details.org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException=Some details
```
**This scenario also covers all the exceptions for which advices are not defined**.
In such cases the **Error key** is derived as fully qualified exception class name. 
But additionally `HttpStatus` need to be specified in `properties` file as it has not been specified anywhere in code because `ControllerAdvice` is not defined, 
if not given even in `properties` file `HttpStatus.INTERNAL_SERVER_ERROR` is taken as default

To minimize the number of properties following defaults are taken if `HttpStatus` is specified as `status.`\<error key\> property.
* Code is taken as specified `HttpStatus`'s int code e.g. if `HttpStatus` is given as EXPECTATION_FAILED then the Code default would be `417`
* Title is taken as specified `HttpStatus`'s reason phrase e.g. if `HttpStatus` is given as EXPECTATION_FAILED like the Title default would be `Expectation Failed`
* Message and Details defaults are taken from thrown exception's `exception.getMessage()`

**Note**: `status.`(error key) property is considered only for exceptions where no explicit advice is defined, 
otherwise `HttpStatus` is specified in the java code.

## Creating and throwing exceptions

Apart from exceptions thrown by frameworks or java, every application need to throw custom exceptions.
[**`ApplicationProblem`**](src/main/java/com/ksoot/problem/core/ApplicationProblem.java) and
[**`ApplicationException`**](src/main/java/com/ksoot/problem/core/ApplicationException.java) 
classes are available in the library to throw an unchecked or checked exception respectively.
There should not be any need to create any custom exception, but if there is a pressing need to do so, 
it can be created and corresponding custom `ControllerAdvice` can be defined for the same, though not recommended.
While defining a custom `ControllerAdvice` in application following `annotation` must be applied to the class.
```java
@Order(Ordered.HIGHEST_PRECEDENCE)
```
It makes `ControllerAdvice` to take precedence over the fallback advice which handles `Throwable` 
i.e. for all exceptions for which no `ControllerAdvice`s are defined.

[**`Problems`**](src/main/java/com/ksoot/problem/Problems.java) **is the central static helper class to create 
Problem instances and throw either checked or unchecked exceptions**, as demonstrated below.
It provides fluent methods to build and throw exceptions.

The simplistic way is to just specify a unique error key and `HttpStatus`.
```java
throw Problems.newInstance("sample.problem").throwAble(HttpStatus.EXPECTATION_FAILED);
```
Error response attributes `code`, `title`, `message` and `details` are expected from the message source (`properties` file) available as follows.
The main concept behind specifying the error attributes in `properties` file is **Error key**, which is mandatory to be unique for each error scenario.
Notice the error key *sample.problem* in following properties

```properties
code.sample.problem=AYX123
title.sample.problem=Some title
message.sample.problem=Some message
details.sample.problem=Some details
```

But exceptions come with some default attributes as follows, to minimize the number of properties required to be defined in `properties` file

If the messages are not found in `properties` files, defaults are taken as follows.
* Code is taken as specified `HttpStatus`'s int code e.g. if `HttpStatus` is given as `EXPECTATION_FAILED` then the Code default would be `417`
* Title is taken as specified `HttpStatus`'s reason phrase e.g. if `HttpStatus` is given as `EXPECTATION_FAILED` like the Title default would be `Expectation Failed`
* Message and Details defaults are taken from thrown exception's `exception.getMessage()`

There are multiple other methods available while creating exceptions through Problems helper. 
For better understanding, have a look at java docs for each method in [**`Problems`**](src/main/java/com/ksoot/problem/Problems.java)
```java
throw Problems.newInstance("sample.problem")
    .defaultMessage("Default message if not found in properties file")
    .messageArgs("PARAM")
    .defaultDetails("Default details if not found in properties file")
    .detailsArgs("P1", "P2")
    .cause(new IllegalStateException("Artificially induced illegal state"))
    .throwAble(Status.EXPECTATION_FAILED); // .throwAbleChecked(Status.EXPECTATION_FAILED)
```
The above code snippet would throw unchecked exception, though not recommended but to throw checked exception,
use `throwAbleChecked` as terminal operation as highlighted in java comment above.

The attributes corresponding to error key `sample.problem` can be provided in `properties` file as follows.
```properties
code.sample.problem=404
title.sample.problem=Some title
message.sample.problem=Some message with param: {0}
details.sample.problem=Some details with param one: {0} and param other: {1}
```

Sometimes it is not desirable to throw exceptions as they occur, but collect them to throw at a later point in execution.
Or to throw multiple exceptions together.That can be done as follows.
```java
Problem problemOne = Problems.newInstance("sample.problem.one").get();
Problem problemTwo = Problems.newInstance("sample.problem.two").get();
throw Problems.throwAble(Status.MULTI_STATUS, problemOne, problemTwo);
```

**It is recommended to use just** [**`Problems`**](src/main/java/com/ksoot/problem/Problems.java), 
but following are also the ways to create exceptions by using inbuilt exception classes. 
These classes are internally used by `problem-handler`.

Exception instances can be created as follows, to dynamically add any more attributes to error response 
along with default attributes. 
The `HttpStatus` for rest api response would be `500` in this case
```java
ThrowableProblem problem = Problem.code("3456")
    .title("Service Error")
    .message("Invalid request received")
    .details("Please retry with correct input")
    .parameter("additional-attribute", "Some additional attribute") // Dynamically add any more attributes to error response
    .build();
throw problem;
```

`HttpStatus` can also be set over custom exception as follows, the same would reflect in error response and 
other error attributes defult would be derived by given `@ResponseStatus`
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

To throw an unchecked exception with specific `HttpStatus`
```java
ApplicationProblem problem = ApplicationProblem.of(HttpStatus.BAD_REQUEST, "234", "Business error",
        "Please provide correct input", "Please contact administrator if the error persists");
throw problem;
```

To throw a checked exception with specific `HttpStatus`, though checked exceptions are not recommended.
```java
public void someMethod() throws ApplicationException {
    ApplicationException problem = ApplicationProblem.of(HttpStatus.BAD_REQUEST, "234", "Business error",
        "Please provide correct input", "Please contact administrator if the error persists");
    throw problem;
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
  "instance": "/api/myjob",
  "method": "PUT",
  "requestTime": "2023-06-11T17:39:13.328595Z",
  "errorCount": 1,
  "errors": [
    {
      "code": "409",
      "title": "Conflict",
      "message": "A job with given parameters already completed",
      "details": "Please force restart the job",
      "stacktrace": [
        "org.springframework.batch.core.repository.support.SimpleJobRepository.createJobExecution(SimpleJobRepository.java:159)",
        "java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)",
        "java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)",
        "java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)",
        "java.base/java.lang.reflect.Method.invoke(Method.java:568)",
        "org.springframework.aop.support.AopUtils.invokeJoinpointUsingReflection(AopUtils.java:343)",
        "org.springframework.aop.framework.ReflectiveMethodInvocation.invokeJoinpoint(ReflectiveMethodInvocation.java:196)",
        "org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)",
        "org.springframework.transaction.interceptor.TransactionInterceptor$1.proceedWithInvocation(TransactionInterceptor.java:123)",
        "org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:391)",
        "org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:119)",
        "org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184)",
        "org.springframework.batch.core.repository.support.AbstractJobRepositoryFactoryBean$1.invoke(AbstractJobRepositoryFactoryBean.java:207)",
        "org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184)",
        "org.springframework.aop.framework.JdkDynamicAopProxy.invoke(JdkDynamicAopProxy.java:223)",
        "jdk.proxy2/jdk.proxy2.$Proxy136.createJobExecution(Unknown Source)",
        "org.springframework.batch.core.launch.support.SimpleJobLauncher.run(SimpleJobLauncher.java:145)",
        "org.springframework.batch.core.launch.support.TaskExecutorJobLauncher.run(TaskExecutorJobLauncher.java:59)",
        "java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)",
        "java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)",
        "java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)",
        "java.base/java.lang.reflect.Method.invoke(Method.java:568)",
        "org.springframework.aop.support.AopUtils.invokeJoinpointUsingReflection(AopUtils.java:343)",
        "org.springframework.aop.framework.ReflectiveMethodInvocation.invokeJoinpoint(ReflectiveMethodInvocation.java:196)",
        "org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)",
        "org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:750)",
        "org.springframework.validation.beanvalidation.MethodValidationInterceptor.invoke(MethodValidationInterceptor.java:141)",
        "org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184)",
        "org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:750)",
        "org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:702)",
        "java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)",
        "java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)",
        "java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)",
        "java.base/java.lang.reflect.Method.invoke(Method.java:568)",
        "org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:207)",
        "org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:152)",
        "org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:118)",
        "org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:884)",
        "org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:797)",
        "org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)",
        "org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1081)",
        "org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:974)",
        "org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1011)",
        "org.springframework.web.servlet.FrameworkServlet.doPut(FrameworkServlet.java:925)",
        "jakarta.servlet.http.HttpServlet.service(HttpServlet.java:593)",
        "org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:885)",
        "jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)",
        "org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:205)",
        "org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)",
        "org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:51)",
        "org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174)",
        "org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)",
        "org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)",
        "org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)",
        "org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174)",
        "org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)",
        "org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)",
        "org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)",
        "org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174)",
        "org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)",
        "org.springframework.web.filter.ServerHttpObservationFilter.doFilterInternal(ServerHttpObservationFilter.java:109)",
        "org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)",
        "org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174)",
        "org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)",
        "org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)",
        "org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)",
        "org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174)",
        "org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)",
        "org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:101)",
        "org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174)",
        "org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)",
        "org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:166)",
        "org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:90)",
        "org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:482)",
        "org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:115)",
        "org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:93)",
        "org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74)",
        "org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:341)",
        "org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:390)",
        "org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63)",
        "org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:894)",
        "org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1741)",
        "org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52)",
        "org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191)",
        "org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)",
        "org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)",
        "java.base/java.lang.Thread.run(Thread.java:833)"
      ]
    }
  ]
}
```

## Causal chains
An exception may have a cause, which in tern may also have one and so one.
The complete cause chain can also be viewed in error response, again it should just be used for local debugging purposes only.

```properties
problem.cause-chains-enabled=true
```
Example response
```json
{
  "instance": "/api/myjob",
  "method": "PUT",
  "requestTime": "2023-06-11T17:43:36.889372Z",
  "errorCount": 1,
  "errors": [
    {
      "code": "500",
      "title": "Internal Server Error",
      "message": "Invalid input job run date",
      "details": "Invalid input job run date",
      "cause": {
        "code": "500",
        "title": "Internal Server Error",
        "message": "A job instance already exists and is complete for parameters={'date':'{value=2023-06-10, type=class java.time.LocalDate, identifying=true}'}.  If you want to run this job again, change the parameters.",
        "details": "A job instance already exists and is complete for parameters={'date':'{value=2023-06-10, type=class java.time.LocalDate, identifying=true}'}.  If you want to run this job again, change the parameters."
      }
    }
  ]
}
```
## Authors and acknowledgment
Rajveer Singh, In case you find any issues or need any support, please ping me on Teams or email me at rajveer.singh2@piramal.com

## Credits and references
Inspired and taken base code from [**`Zalando Problem libraries`**](https://github.com/zalando/problem-spring-web)

## Known Issues
* If an application uses multiple vendor relational databases then the [**`ConstraintNameResolver`**](src/main/java/com/ksoot/problem/spring/advice/dao/ConstraintNameResolver.java) 
may not work properly, needs further testing. For example if it is using Postgres and SQL Server both.