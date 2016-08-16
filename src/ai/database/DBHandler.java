package ai.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author arsalan
 */
public class DBHandler {

    private static Connection dbConnection = null;
    private final BufferedReader buffer;
    private final String port;
    private final String dbName;
    private final String dbId;
    private final String password;
    private PreparedStatement statement;

    private DBHandler() throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {
        buffer = new BufferedReader(new FileReader(new File("build.properties")));
        port = buffer.readLine();
        dbName = buffer.readLine();
        dbId = buffer.readLine();
        password = buffer.readLine();
        Class.forName("com.mysql.jdbc.Driver");
        dbConnection = (Connection) DriverManager.getConnection("jdbc:mysql://" + port + "/" + dbName, dbId, password);
        buffer.close();
    }

    public static Connection getDBConnectionInstance() throws IOException, FileNotFoundException, ClassNotFoundException, SQLException {
        if (dbConnection == null) {
            synchronized (DBHandler.class) {
                if (dbConnection == null) {
                    new DBHandler();
                }
            }
        }
        return dbConnection;
    }

    public static boolean runInsertQuery(String query) throws SQLException, IOException, FileNotFoundException, ClassNotFoundException {
        boolean isValid = false;
        if (dbConnection != null) {
            isValid = true;
            dbConnection.prepareStatement(query).executeUpdate();
        } else {
            new DBHandler();
        }
        return isValid;
    }

    public static ResultSet getQueryResult(String query) throws SQLException, IOException, FileNotFoundException, ClassNotFoundException {
        if (dbConnection == null) {
            new DBHandler();
        }
        return dbConnection.prepareStatement(query).executeQuery();
    }
}
