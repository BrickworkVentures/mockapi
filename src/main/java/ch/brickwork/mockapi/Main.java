package ch.brickwork.mockapi;

import ch.brickwork.mockapi.dao.XerialSQLiteDatabase;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import java.io.IOException;
import java.net.URI;

/**
 * holds database and starts webserver
 */
public class Main {
    // Global var for DB
    public static XerialSQLiteDatabase db;

    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8089/myapp/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.csvful package
        final ResourceConfig rc = new ResourceConfig().packages("ch.brickwork.mockapi").register(MultiPartFeature.class);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        db = new XerialSQLiteDatabase("sample.db");

        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        System.in.read();
        server.stop();
    }
}