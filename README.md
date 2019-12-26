# i18n-merge-app

Minimal [Spring Boot](http://projects.spring.io/spring-boot/) application that can make merging i18n files easier.

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).
- [Maven 3](https://maven.apache.org).
- Git Credentials saved on (windows) Credential Manager.

## What it does

- Compare the i18n from production to the local file and lists new, removed and updated lines.
- Give the user the option which lines should be added, removed and which version of the i18n line should be sent to production, the current one (default) or the development one.
- Commit and push the changes to the git repository.

### What it *doesn't* do

- Download the i18n from production.
- Pull the most recent code from `develop`.
- Merge the code from `develop` branch to `master`.

## Cloning the repository

To clone the repository, first you need to have [git](https://git-scm.com/) installed in your machine (and since you're on GitHub, I assume you do), then you can run the following command where you want your application folder to be:

```shell
git clone https://github.com/alexandre596/i18n-merge.git
```

## Application Properties

The project contains some properties that might need some changed before the project can be run in your machine, and some properties that are better left off alone. These properties are located on the file `src/main/resources/application.properties`

| Property | Description |
| --- | --- |
| `default.i18n.zip.location` | The location of the zip i18n file downloaded from Production CRX.de. This value will be used to automatically fill the _text field_ on the first screen (Upload File) |
| `default.workspace.location` | The base folder where all your projects are located. This value will be used to automatically fill all the the _text fields_ on the second screen (Projects)  |
| `i18n.file.name` | The file name of the i18n file on the projects. This value should rarely be changed |
| `temporary.directory` | A temporary directory where the application will extract the production zip file. If the directory doesn't exist, it will be created. This directory will be **removed** when the application ends. |
| `exclude.directories` | The directories where the application will not scan for i18n files |
| `font.family` | The font family that will be used on all the application UIs |
| `font.size` | The font size that will be used on all the application UIs |
| `table.max.rows` | The maximum amount of rows that should be displayed in each table of the third screen (Merge) without adding a scroll |
| `git.allowed.branches` | A comma-separated regex that list which branches the application is allowed to commit to. |
| `git.credentials.address` | The address where the git credentials are stored in the (windows) Credential Manager |

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.celfocus.omnichannel.digital.MainRunner` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

## Licencing

Feel free to copy, change, fork and do whatever you want (but removing haha) with this repository.