package ch.brickwork.mockapi.util;

import ch.brickwork.bsuit.database.Record;
import ch.brickwork.bsuit.database.Value;

import java.util.List;

/**
 * Created by marcel on 7/6/16.
 */
public class RecordUtils {
    public static String toJSON(List<Record> records)
    {
        String json = "[";
        boolean firstRecord = true;
        for (Record r : records) {
            if (firstRecord) firstRecord = false;
            else json += ", ";

            json += "{";

            boolean firstValue = true;
            for (Value v : r) {
                if (firstValue) {
                    firstValue = false;
                } else {
                    json += ", ";
                }

                String value = v.getValue().toString();

                // if necessary, split lines into array
                if (value.contains("\n") || value.contains("\r")) {
                    String lines = "";
                    boolean firstLine = true;
                    for (String line : value.split("[\n\r]")) {
                        if (firstLine) firstLine = false;
                        else lines += ", ";
                        lines += "\"" + line + "\"";
                    }

                    json += "\"" + v.getAttributeName() + "\" : " + "[" + lines + "]";
                } else {
                    json += "\"" + v.getAttributeName() + "\" : " + "\"" + value + "\"";
                }
            }

            json += "}";
        }
        return json + "]";
    }
}
