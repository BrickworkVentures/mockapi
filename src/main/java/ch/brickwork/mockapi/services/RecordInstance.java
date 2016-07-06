package ch.brickwork.mockapi.services;

import ch.brickwork.mockapi.Main;
import ch.brickwork.mockapi.util.RecordUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("tables")
public class RecordInstance {

    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{table}/{id}")
    @GET
    public Response getRecord(@PathParam("table") String table, @PathParam("id") String id)
    {
        if (!Main.db.existsTableOrView(table)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<String> colNames = Main.db.getTableOrViewColumnNames(table);
        String str = RecordUtils.toJSON(Main.db.prepare("SELECT * FROM " + table + " WHERE " + colNames.get(0) + " = '" + id + "'"));
        return Response.ok(str, MediaType.APPLICATION_JSON).build();
    }
}
