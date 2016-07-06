package ch.brickwork.mockapi.services;

import ch.brickwork.mockapi.Main;
import javax.print.attribute.standard.Media;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * index.html - is it best practice and beautiful? no. does it need to be? no.
 */
@Path("/")
public class Index {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getIndexHTML()
    {
        String tables = "<ul>";

        for (String tableName : Main.db.getTableNames()) {
            tables += "<li><a href=\"" + Main.BASE_URI + "tables/" + tableName + "\">" + tableName + "</a></li>";
        }

        tables += "</ul>";

        String uploadForm = "     <h1>File Upload</h1>\n" + "\n"
            + "            <form action=\"" + Main.BASE_URI + "upload/\" method=\"post\" enctype=\"multipart/form-data\">\n" + "\n"
            + "                <p>Select a file : <input type=\"file\" name=\"file\" size=\"45\" /></p>\n"
            + "                <input type=\"submit\" value=\"Upload CSV\" />\n" + "\n" + "            </form>";

        return "<?xml version=\"1.0\"?><html>" + "<head><title>Mockapi</title></head>" + "<body>" + "<h1>Welcome to Mockapi</h1>" + "<h2>Tables</h2>" + tables
            + uploadForm + "</body>" + "</html>" + "" + "";
    }
}
