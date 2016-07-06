Mockapi creates a simple API allowing you to upload CSV files via a website (http://localhost:8089/mockapi/). After uploading a file foo.csv with columns a b c d, it will be served via 

http://localhost:8089/mockapi/foo/        (List all foo records)
http://localhost:8089/mockapi/foo/2x      (Get the foo record where id (=1st column) is equal to 2x


HOW TO BUILD

0. Clone it

1. mvn install:install-file -Dfile=src/lib/boilersuit-core.jar -DgroupId=ch.brickwork.bsuit -DartifactId=core -Dversion=1.0 -Dpackaging=jar
  (Boilersuit is a dependency that is not available on a public mvn repo, so local)

2. mvn install
  (build)

3. mvn exec:java
  (start webserver)
