# Contact Service


## Technology

The component uses the following technologies:
- **Java 11** as the main platform
- Spring Boot 2.1 for use of IoC, for ease of configuration through spring-boot-starters and for the reactive support
- Liquibase for database versioning
- PostgreSQL as database / H2 as embedded database

### Liquibase

- Liquibase starts automatically when the application runs and loads changelogs included in resources/db/changelog/db.changelog-master.yaml.
- Changelogs are presented in resources/db/changelog/
- Changelog files were created with this name convention: <date_hour_mins_2_random_digits>-<name_of_operation>.xml
- To get the SQL commands you need to download liquibase jar from https://download.liquibase.org/, unzip the jar file(<liquibase_unzipped_folder>), copy postgresql jar(e.g. postgresql-42.2.2) and slf4j jar(e.g. slf4j-api-1.7.25) into <liquibase_unzipped_folder>/lib, copy the db folder (from resources) inside the unzipped liquibase folder <liquibase_unzipped_folder> and then call:
```
$ ./liquibase --url="<database_url>" --username=<database_username_required> --password=<database_password_required> --changeLogFile=<path_to_db.changelog-master.yaml> updateSQL
```

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
