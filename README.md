# Ozone EIP Client

A generic Spring Boot application, designed for executing Apache Camel routes. The application is run in the same directory that may contain a **config** & **routes** folders and **application.properties** file providing default properties.

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
└── eip-client-<version>.jar

```

The **config** folder may contain multiple single level directories with an **application.properties** file in each. 

The **routes** folder may contain multiple single level directories with any number of **xml defined routes** in each. Further still, any number of **xml defined route** may be provided in the **routes** directory.

## Getting Started

Follow the instructions below to get a copy of the project up and running on your local machine for development and testing purposes.

1. **Clone the project**
     ```bash
    git clone https://github.com/ozone-his/eip-client.git
     ```

2. **Build the project**
   
    Navigate to the project directory and build the application using Maven:
    ```bash
    cd eip-client
    mvn clean install
    ``` 

3. **Run the app**
   
    After building the project, you can run the application using the following command:
    ```bash
    java -jar app/target/eip-client-<version>.jar
    ```

# Docker

## Building a Docker image

```
mv app/target/*jar docker/
cd docker/
docker build . -t mekomsolutions/eip-client:latest
```

This image can now be consumed in a docker-compose.yml with:
```
eip-client:
   image: mekomsolutions/eip-client:latest
   volumes:
     - "./path-to-your-configs:/config"
     - "./path-to-your-routes:/routes"
     - eip-home:/eip-home
   ports:
   - "8083:8083"
```
