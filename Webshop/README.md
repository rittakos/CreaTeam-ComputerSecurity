# CAFF webshop

## Run backend locally

You should have:
- Java 17
- Mysql server
- optionally maven

Setup:
- Set the database connection properties in the `application.properties` file
- run `mvn clean install -DskipTests` to generate the sources from the openapi file
- in IntelliJ you may need to set the `target/generated-sources/src/main/java` folder as source folder in the `file/project structuremodules/sources` options

MySQL install in docker: `docker run --name mysql-dev -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql`

## Run backend in docker

If you want to host the frontend as well you need to run `npm run build` in the frontend folder

For frontend development with docker set the target in `frontend/proxy.conf.js` to `http://localhost` to access the server hosted from docker

### Run everything in docker in any environment

Run `docker-compose -f .\docker-compose-dev.yaml up -d`. In this case you only need docker

This version is slower

### Run everything in docker if you have Java 17 on your computer

Run `mvnw clean package -DskipTests` in the backend folder

Run `docker-compose -f .\docker-compose-prod.yaml up -d`

This version is faster

## Running frontend

Start the development server with `npm start`, this will use the proxy configuration

To generate services and models from the openapi file run `npm run openapi:codegen`
