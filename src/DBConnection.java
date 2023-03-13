import java.sql.*;

public class DBConnection {
    public static void main(String[] args) throws SQLException{
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "postgres";

        Connection connection = DriverManager.getConnection(url,user,password);
        Statement statement = connection.createStatement();

        System.out.println(connection);
        String tableName = "public.medici";

        /*
        String insertName = "Mihai";
        int insertIDMedic = 1114;
        //int insertIDPacient = 1111;


         */
        String query = "INSERT into "+tableName+" (name,id_medic)"+" VALUES(?,?)";

        PreparedStatement preparedStmt = connection.prepareStatement(query);
        preparedStmt.setString(1,"Mihai");
        preparedStmt.setInt(2,1111);
        System.out.println(preparedStmt);
        preparedStmt.executeUpdate();

        System.out.println(query);
        /*
        String insertPersonQuery = "insert into "+tableName+" (name,id_medic)"+ " values('"+insertName+"',"+insertIDMedic+")";
        System.out.println(query);

        insertName = "Andrei";
        insertIDMedic = 1115;
        insertPersonQuery = "insert into "+tableName+" (name,id_medic)"+ " values('"+insertName+"',"+insertIDMedic+")";
        System.out.println(query);

        insertName = "Septimiu";
        insertIDMedic = 1116;
        insertPersonQuery = "insert into "+tableName+" (name,id_medic)"+ " values('"+insertName+"',"+insertIDMedic+")";
        System.out.println(query);

        statement.execute(query);

         */


    }
}
