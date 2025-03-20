import java.sql.*;

public class MySQLJDBCExample {

    public static void main(String[] args) {
        // Database connection variables
        String url = "jdbc:mysql://localhost:3306/students"; 
        String user = "root";
        String password = "root_user"; 

        String query = "SELECT * FROM students"; 

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(url, user, password);

            statement = connection.createStatement();

            resultSet = statement.executeQuery(query);


            while (resultSet.next()) {

                int column1 = resultSet.getInt("id"); 
                String column2 = resultSet.getString("name");
                System.out.println("ID: " + column1 + ", Name: " + column2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
