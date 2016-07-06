package ch.brickwork.mockapi.dao;

import ch.brickwork.bsuit.database.AbstractSQLDatabase;
import ch.brickwork.bsuit.database.Record;
import ch.brickwork.bsuit.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by marcel on 7/5/16.
 */
public class XerialSQLiteDatabase extends AbstractSQLDatabase {

    private final String fileName;

    private Connection con;

    public XerialSQLiteDatabase(String fileName)
    {
        super(new Log());
        this.fileName = fileName;
        initLogger();
        initDatabase();
        openConnection();
        initMeta();
    }

    @Override
    protected void initLogger()
    {
        // no logging currently
    }

    @Override
    protected void initDatabase()
    {

    }

    @Override
    protected void openConnection()
    {
        try {
            // load the sqlite-JDBC driver using the current class loader
            Class.forName("org.sqlite.JDBC");

            // create a database connection
            if (con == null || con.isClosed()) {
                con = DriverManager.getConnection("jdbc:sqlite:" + fileName);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Record> prepare(String sql)
    {
        System.out.println("SQL: " + sql);
        final List<Record> result = new ArrayList<>();

        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = con.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                Record record = new Record();
                for (int col = 1; col <= rs.getMetaData().getColumnCount(); col++) {
                    if (rs.getString(col) != null) {
                        record.put(rs.getMetaData().getColumnName(col), rs.getString(col));
                    } else {
                        record.put(rs.getMetaData().getColumnName(col), "");
                    }
                }
                result.add(record);
            }
        } catch (SQLException e) {
            if (!e.toString().contains("query does not return ResultSet"))   // harmless
            {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public String getRowIdKeyWord()
    {
        return "rowid";
    }

    @Override
    public List<String> getTableOrViewColumnNames(String s)
    {
        List<String> columns = new ArrayList<>();

        final List<Record> result = prepare("PRAGMA table_info(" + s + ");");
        if (null != result) {
            for (Record record : result) {
                columns.add(record.getValue("name").getValue().toString());
            }
        }
        return columns;
    }

    @Override
    public Hashtable<String, Integer> getTableOrViewColumnNamesHash(String s)
    {
        Hashtable<String, Integer> columns = new Hashtable<>();

        List<Record> result = prepare("PRAGMA table_info(" + s + ");");
        int i = 0;
        if (result != null) {
            for (Record record : result) {
                columns.put(record.getValue("name").getValue().toString(), i);
                i++;
            }
            return columns;
        }
        return null;
    }

    @Override
    public boolean existsTable(String s)
    {
        List<Record> result = prepare("SELECT * FROM sqlite_master WHERE type='table' AND name='" + s.toLowerCase() + "'");
        return result != null && result.size() > 0;
    }

    @Override
    public boolean existsView(String s)
    {
        List<Record> result = prepare("SELECT * FROM sqlite_master WHERE type='view' AND name='" + s.toLowerCase() + "'");
        return result != null && result.size() > 0;
    }

    @Override
    public void insert(String tableName, List<Record> records)
    {
        if (records.size() == 0)
            return;

        for (Record record : records)
            prepare(createInsertStatement(tableName, record));
    }

    @Override
    protected List<String> getAllTableNames()
    {
        return getMasterTableEntries("table");
    }

    @Override
    protected List<String> getAllViewNames()
    {
        return getMasterTableEntries("view");
    }

    private List<String> getMasterTableEntries(String type)
    {
        List<Record> recs = prepare("SELECT name FROM sqlite_master WHERE type='" + type + "'");
        ArrayList<String> names = new ArrayList<>();
        for (Record r : recs) {
            names.add(r.getValue("name").getValue().toString());
        }
        return names;
    }
}