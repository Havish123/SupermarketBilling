package Extension;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseUtil {
    private static final String URL = "jdbc:postgresql://localhost:5432/";
    private static final String DATABASE_NAME = "supermarket";
    private static final String USER = "postgres";
    private static final String PASSWORD = "12345678";

    static Connection con = null;
    public static Connection getConnection()
    {
        if (con != null){
            return con;
        }
        return getConnection(DATABASE_NAME, USER, PASSWORD);
    }
    public static Connection getConnection(String db,String username,String password){

        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(URL+db,
                    username, password);
        } catch (Exception e) {
            System.err.println("Error in connectToDb"+e.getMessage());
            System.exit(0);
        }
        return con;
    }


}
