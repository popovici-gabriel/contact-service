# Contact Service


## Technology

The component uses the following technologies:
- **Java 11** as the main platform
- Spring Boot 2.1 for use of IoC, for ease of configuration through spring-boot-starters and for the reactive support
- Liquibase for database versioning
- PostgreSQL as database / H2 as embedded database
- Spring State Machine 
## Development

The application uses Apache Maven as build tool. It follows the [team's git branching model](http://domdocsbs01.ops.server.lan:7777/docs-domains-registration/Domains-Git-Branching-Model).
Business documentation can be found in docs folder.

## Running locally

The most easiest way is to run the main file from your IDE.

The setup is as follows:
```
$ git clone ssh://git@bitbucket.1and1.org/dr/contact-service.git
$ cd contact-service
$ mvn clean install
$ java -jar target/*.jar or mvn spring-boot:run
```
