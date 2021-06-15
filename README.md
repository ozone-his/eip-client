# EIP Client

This is a Spring Boot application that depends on the [openmrs-eip](https://github.com/openmrs/openmrs-eip) watcher, forwarding debezium generated events to provided xml camel routes. The application is run in the same directory that may contain a **config** & **routes** folders and **application.properties** file providing default properties.

```
.
├── config/
|      ├── odoo-config/
|      |       └── application.properties
|      ├── senaite-config/
|      |       └── application.properties
|      └── ...
├── routes/
|      ├── odoo-routes/
|      |       ├── example-odoo-route.xml
|      |       └── ...
|      ├── senaite-routes/
|      |       ├── example-senaite-route.xml
|      |       └── ...
|      ├── ...
|      └── general-route.xml
├── application.properties
└── eip-app-1.0.0-SNAPSHOT.jar

```

The **config** folder may contain multiple single level directories with an **application.properties** file in each. This is the file organisation supported by Spring Boot version 2.3.0+.

The **routes** folder may contain multiple single level directories with any number of **xml defined routes** in each. Further still, any number of **xml defined route** may be provided in the **routes** directory.

## Build
From the terminal, navigate to your working directory, clone and build the project to generate the executable artifacts
by running the commands below.

```
git clone https://github.com/mekomsolutions/eip-client.git
cd eip-app
mvn clean install
```

Make sure the build completed successfully.

## Running the App
Following the above file structure, copy the generated `.jar` file located at `eip-client/app/target/` path into the working root directory and run the app on commandline with the following command.

```
java -jar client-app-1.0.0-SNAPSHOT.jar
```