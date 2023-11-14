# Ozone EIP Client

[![CI](https://github.com/ozone-his/eip-client/actions/workflows/build.yml/badge.svg)](https://github.com/ozone-his/eip-client/actions/workflows/build.yml)

A generic Spring Boot application designed for executing Apache Camel routes within Ozone. It can be used as a base image for building custom EIP clients. It has support for **_Java DSL_** and **_XML DSL_** routes.

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

## Docker Support

### Building a Docker image

```bash
docker build . -t mekomsolutions/eip-client:latest
```

This image can now be consumed in a docker-compose.yml file with:

```yaml
eip-client:
   image: mekomsolutions/eip-client:latest
   container_name: eip-client
   volumes:
     - "./path-to-your-configs:/config"
     - "./path-to-your-routes:/routes"
     - "./eip-home:/eip-home"
   ports:
   - "8083:8083"
```

### Running the Docker image

```bash
docker run -p 8083:8083 -v ./path-to-your-configs:/config -v ./path-to-your-routes:/routes mekomsolutions/eip-client:latest
```

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](https://www.apache.org/licenses/LICENSE-2.0.txt) file for details.
