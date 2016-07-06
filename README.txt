Mockapi creates a simple API allowing you to upload CSV files via a website:

http://localhost:8089/mockapi/            (Welcome, List available tables, Upload CSV File)

After uploading a file foo.csv with columns a b c d, it will be served via 

http://localhost:8089/mockapi/foo/        (List all foo records)

http://localhost:8089/mockapi/foo/2x      (Get the foo record where id (=1st column) is equal to 2x

The csv files are stored in a local SQLite DB called sample.db. Loading of csv is done via the Boilersuit framwork.

HOW TO BUILD

0. Clone it

1. mvn install:install-file -Dfile=src/lib/boilersuit-core.jar -DgroupId=ch.brickwork.bsuit -DartifactId=core -Dversion=1.0 -Dpackaging=jar
  (Boilersuit is a dependency that is not available on a public mvn repo, so local)

2. mvn install
  (build)

3. mvn exec:java
  (start webserver)
