# spring-boot-coding-assignment

## Introduction

This service implements a JSON REST API that can be used 1) to validate Social Security Numbers that are in a format
enforced by the Finnish government and 2) to convert an amount from one currency to another.

## Requirements

The service is built using following tools and frameworks:

- Java 11
- Spring Boot 2.7.0
- Gradle
- Git
- Docker

## Development

In this section you can find information about setting up a local development environment.

### Cloning the repository

Source code is stored in [Github](https://github.com/kuusemi/spring-boot-coding-assignment) and can be cloned into a
local development machine using git command line tool, using a following command:

`git clone https://github.com/kuusemi/spring-boot-coding-assignment`

Project can then be imported to the IDE of your choice. For example using IntelliJ IDEA by selecting from **File** menu
option **New** and from under that option **Project from Existing Sources...**

Alternatively the source code can be cloned directly to IntelliJ IDEA by selecting from **File** menu
option **New** and from under that option **Project from Version Control...**

Other integrated development environments, e.g. Eclipse have similar functionality available.

After the source code has been imported, project dependencies are automatically loaded to the local Gradle cache.

### Building executable jar file from the command line

Since application is using a 3rd party service to fetch
the [currency exchange rates](https://apilayer.com/marketplace/exchangerates_data-api#details-tab), one needs to
register
as a user of the service and obtain a valid API key that needs to be placed into
the **exchange.rates.apiKey** property of the [application.yml](https://raw.githubusercontent.com/kuusemi/spring-boot-coding-assignment/main/src/main/resources/application.yml)
property file.

Application is using an H2 in-memory database to store the currency exchange rates locally.

Readily available executable can be built from the command line with a following command:

`./gradlew bootJar`

After the jar file has been built, it can be started from the command line:

`java -jar build/libs/spring-boot-coding-assignment-0.0.1-SNAPSHOT.jar `

By default, application will start listening on port 8090.

### Running unit tests

Unit tests can be executed from the command line using the following command:

`./gradlew test`

### Containerising the application

The repository contains a Docker build file that can be used to build a container image from the source. Docker image
can be built with the following command from the command line:

`docker build -t mkuusela/spring-boot-coding-assignment:latest .`

The -t option is optional and the command can be executed without it. The command runs unit tests, builds a bootable jar
file and creates a container image that can then be used to start a container by issuing a following command from the
command line:

`docker run -d --rm -p 8090:8090 --name spring-code-assignment mkuusela/spring-boot-coding-assignment:latest`

The aforementioned command starts up the application and exposes port 8090 to the host operating system. Application
logs can be accessed with the following command:

`docker logs --follow spring-code-assignment`

Application can be stopped with the following command:

`docker stop spring-code-assignment`

## Testing the application

The application can be tested using curl command line tool with following commands:

### Social Security Number validation

Successful request:

`
curl -v -X POST -H "Content-type: application/json" -d '{"ssn": "010507A632V", "countryCode": "FI"}' http://localhost:8090/validate_ssn/
`

Success response:

`
HTTP/1.1 200
{
"ssn_valid": true
}
`

Failed request:

`
curl -v -X POST -H "Content-type: application/json" -d '{"ssn": "010507A632V", "countryCode": "SE"}' http://localhost:8090/validate_ssn/
`

Failure response:

`
HTTP/1.1 200
{
"ssn_valid": false
}
`

### Currency exchange

Successful request:

`
curl -v -X GET -H "Accept: application/json" "http://localhost:8090/exchange_amount?from=SEK&to=USD&from_amount=10.90"
`

Success response:

`
HTTP/1.1 200
{
"from": "SEK",
"to": "USD",
"to_amount": 1.0738,
"exchange_rate": 1.045785
}
`

Failed request:

`
curl -v -X GET -H "Accept: application/json" "http://localhost:8090/exchange_amount?from=SEK&from_amount=10.90"
`

Failure response:

`
HTTP/1.1 400
{
"timestamp": "2022-06-13T12:21:38.246+00:00",
"status": 400,
"error": "Bad Request",
"trace": "org.springframework.web.bind.MissingServletRequestParameterException: Required request parameter 'to' for method parameter type String is not present\n\tat org.springframework.web.method.annotation.RequestParamMethodArgumentResolver.handleMissingValueInternal(RequestParamMethodArgumentResolver.java:218)\n\tat org.springframework.web.method.annotation.RequestParamMethodArgumentResolver.handleMissingValue(RequestParamMethodArgumentResolver.java:193)\n\tat org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver.resolveArgument(AbstractNamedValueMethodArgumentResolver.java:114)\n\tat org.springframework.web.method.support.HandlerMethodArgumentResolverComposite.resolveArgument(HandlerMethodArgumentResolverComposite.java:122)\n\tat org.springframework.web.method.support.InvocableHandlerMethod.getMethodArgumentValues(InvocableHandlerMethod.java:179)\n\tat org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:146)\n\tat org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:117)\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:895)\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:808)\n\tat org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\n\tat org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1067)\n\tat org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:963)\n\tat org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006)\n\tat org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:898)\n\tat javax.servlet.http.HttpServlet.service(HttpServlet.java:655)\n\tat org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883)\n\tat javax.servlet.http.HttpServlet.service(HttpServlet.java:764)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:227)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\n\tat org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\n\tat org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\n\tat org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\n\tat org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\n\tat org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:197)\n\tat org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97)\n\tat org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:541)\n\tat org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:135)\n\tat org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92)\n\tat org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78)\n\tat org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:360)\n\tat org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:399)\n\tat org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65)\n\tat org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:890)\n\tat org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1743)\n\tat org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)\n\tat org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191)\n\tat org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)\n\tat org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\n\tat java.base/java.lang.Thread.run(Thread.java:829)\n",
"message": "Required request parameter 'to' for method parameter type String is not present",
"path": "/exchange_amount"
}
`






