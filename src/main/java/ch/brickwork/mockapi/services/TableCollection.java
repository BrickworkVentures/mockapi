package ch.brickwork.mockapi.services;

import ch.brickwork.mockapi.Main;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

/**
 * delivers json array of records contained in the table
 * (C) Brickwork Ventures, 2016
 */
@Path("tables")
public class TableCollection {
    @GET
    @Produces("application/json")
    public String getTables()
    {
        String json = "[";

        boolean firstRecord = true;
        for(String tableName : Main.db.getTableNames()) {
            if(firstRecord)
                firstRecord = false;
            else
                json += ", ";

            json += "{ \"name\" : \"" + tableName + "\", \"_links\" : [ \"" + Main.BASE_URI + "tables/" + tableName + "\" ] }";
        }

        return json + "]";
    }
}