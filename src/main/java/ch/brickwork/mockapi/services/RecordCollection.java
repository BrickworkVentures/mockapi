package ch.brickwork.mockapi.services;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.brickwork.mockapi.Main;
import ch.brickwork.mockapi.util.RecordUtils;

/**
 * delivers json array of records contained in the table
 * (C) Brickwork Ventures, 2016
 */
@Path("tables")
public class RecordCollection {
    @Path("{table}")
    @GET
    @Produces("application/json")
    public Response getRecords(@PathParam("table") String table)
    {
        if (!Main.db.existsTableOrView(table)) {
           return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(RecordUtils.toJSON(Main.db.getAllRecordsFromTableOrView(table, null, null)), MediaType.APPLICATION_JSON).build();
    }
}