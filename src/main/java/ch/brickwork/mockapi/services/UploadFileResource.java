package ch.brickwork.mockapi.services;

import ch.brickwork.bsuit.database.IDatabase;
import ch.brickwork.bsuit.globals.IBoilersuitApplicationContext;
import ch.brickwork.bsuit.interpreter.DefaultCommandInterpreterFactory;
import ch.brickwork.bsuit.interpreter.ScriptProcessor;
import ch.brickwork.bsuit.util.ILog;
import ch.brickwork.bsuit.util.Log;
import ch.brickwork.mockapi.Main;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * upload csv file and import it into database via boilersuit
 */
@Path("upload")
public class UploadFileResource {
    @POST
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    public Response uploadPdfFile(  @FormDataParam("file") InputStream fileInputStream,
                                    @FormDataParam("file") FormDataContentDisposition fileMetaData) throws Exception
    {
        try
        {
            int read;
            byte[] bytes = new byte[1024];

            System.out.println("Writing file: " + fileMetaData.getFileName() + "...");
            OutputStream out = new FileOutputStream(new File(fileMetaData.getFileName()));
            while ((read = fileInputStream.read(bytes)) != -1)
            {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e)
        {
            throw new WebApplicationException("Error while uploading file. Please try again !!");
        }

        System.out.println("Importing via Boilersuit...");

        new ScriptProcessor(new DefaultCommandInterpreterFactory(), new IBoilersuitApplicationContext() {
            @Override
            public String getWorkingDirectory()
            {
                return ".";
            }

            @Override
            public void setWorkingDirectory(String s)
            {

            }

            @Override
            public boolean changeDBFileDirectory(String s)
            {
                return true;
            }

            @Override
            public IDatabase getDatabase()
            {
                return Main.db;
            }

            @Override
            public void setDatabase(IDatabase iDatabase)
            {

            }

            @Override
            public ILog getLog()
            {
                return new Log();
            }

            @Override
            public void setLog(ILog iLog)
            {

            }
        }).processScript(fileMetaData.getFileName().toLowerCase().replace(".csv", "") + " := " + fileMetaData.getFileName(), null, null);

        return Response.seeOther(new URI(Main.BASE_URI)).build();
    }
}