
# Database Setup (reset/ use as normal)
Data (users, content, and secrets) can be reset or used as normal by modifying `src/main/resources/application.yml`. You must choose enable either one option to start the server.

Option 1: To reset the database to pre-populated data on server start, enable `ddl-auto: create` and `initialize: true`. This will delete all existing data and populate the database using the script in `src/main/java/com/hng/ixn/DataInitializer.java`. 

Option 2: To use existing content as normal on server start, enable `ddl-auto: update`and `initialize: false`. This option should be enabled to run the database in production, as it does not reset the data when the server is restarted.

After choosing either option, start the server.

Note: files are stored separately in s3.



