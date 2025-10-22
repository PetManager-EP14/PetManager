
# Pet Manager
[![CI/CD Pipeline](https://github.com/PetManager-EP14/PetManager/actions/workflows/backend-ci.yml/badge.svg)](https://github.com/PetManager-EP14/PetManager/actions/workflows/backend-ci.yml) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=PetManager-EP14_PetManager&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=PetManager-EP14_PetManager) [![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=PetManager-EP14_PetManager&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=PetManager-EP14_PetManager) [![Bugs](https://sonarcloud.io/api/project_badges/measure?project=PetManager-EP14_PetManager&metric=bugs)](https://sonarcloud.io/summary/new_code?id=PetManager-EP14_PetManager) [![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=PetManager-EP14_PetManager&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=PetManager-EP14_PetManager) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=PetManager-EP14_PetManager&metric=coverage)](https://sonarcloud.io/summary/new_code?id=PetManager-EP14_PetManager) [![Known Vulnerabilities](https://snyk.io/test/github/PetManager-EP14/PetManager/badge.svg)](https://snyk.io/test/github/PetManager-EP14/PetManager)

Implementation of a Simple App.

PetManager is a backend application written in Java for managing pet information. It provides basic management operations (create, read, update, and delete) on pet records and exposes a REST API for interacting with the application. The repository also includes a Dockerfile for building and running the application in containers.

Including integration with GitHub Actions, Sonarqube (SonarCloud), Coveralls and Snyk

### Folders Structure

In the folder `src` is located the main code of the app

In the folder `test` is located the unit tests

### How to install it

Execute:

```shell
$ mvnw spring-boot:run
```
to download the node dependencies

### How to test it

Execute:

```shell
$ mvnw clean install
```

### How to get coverage test

Execute:

```shell
$ mvwn -B package -DskipTests --file pom.xml
```

## Authors

- [@Juan Jose Monsalve Hernandez](https://github.com/JuanJoM14)
- [@Alejandro Chavarria Mora](https://github.com/AlejandroMora05)
- [@Ana Maria Granada Rodas](https://github.com/anagranada1)
