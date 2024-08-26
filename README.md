# Setup

1. Install **Java 17**.

2. Configure the necessary resources and environment variables:

    - **JWT Secret Key**:
        - Set an arbitrary value for the JWT secret key:
            - `IXN_APP_JWT_SECRET_KEY`

    - **AWS S3 Bucket**:
        - Create a bucket named `ixn-radio`.
        - Configure IAM access keys to allow access to the S3 bucket:
            - `IXN_AWS_USER_ACCESS_KEY`
            - `IXN_AWS_USER_SECRET_KEY`

    - **PostgreSQL Database**:
        - Set up your PostgreSQL database and configure the following environment variables:
            - `IXN_POSTGRES_PASSWORD`
            - `IXN_POSTGRES_URL`
            - `IXN_POSTGRES_USERNAME`

3. Build the project using Maven:
   `./mvnw install`
4. Start the Spring Boot application:
    `./mvnw spring-boot:run`

# Data Config

Data (users, content, and secrets) can be reset or used as normal by modifying `src/main/resources/application.yml`. You must choose enable either one option to start the server.
- Option 1: To reset the database to pre-populated data on server start, enable `ddl-auto: create` and `initialize: true`. This will delete all existing data and populate the database using the script in `src/main/java/com/hng/ixn/DataInitializer.java`. 
- Option 2: To use existing content as normal on server start, enable `ddl-auto: update`and `initialize: false`. This option should be enabled to run the database in production, as it does not reset the data when the server is restarted.

After choosing either option, start the server.

Note: files are stored separately in s3.



