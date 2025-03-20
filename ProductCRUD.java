Here’s the code without comments:

```java
import java.sql.*;
import java.util.Scanner;

public class ProductCRUD {

    private static Connection conn;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/yourDatabase", "username", "password");
            createTable();

            while (true) {
                System.out.println("\n----- Menu -----");
                System.out.println("1. Create Product");
                System.out.println("2. Read Product");
                System.out.println("3. Update Product");
                System.out.println("4. Delete Product");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        createProduct();
                        break;
                    case 2:
                        readProduct();
                        break;
                    case 3:
                        updateProduct();
                        break;
                    case 4:
                        deleteProduct();
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        closeConnection();
                        return;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS product (" +
                                "productID INT PRIMARY KEY AUTO_INCREMENT, " +
                                "productName VARCHAR(100), " +
                                "price FLOAT, " +
                                "quantity INT)";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
        }
    }

    private static void createProduct() {
        System.out.print("Enter Product Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Price: ");
        float price = scanner.nextFloat();
        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        String insertSQL = "INSERT INTO product (productName, price, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, name);
            pstmt.setFloat(2, price);
            pstmt.setInt(3, quantity);

            conn.setAutoCommit(false);
            pstmt.executeUpdate();
            conn.commit();
            System.out.println("Product created successfully!");
        } catch (SQLException e) {
            try {
                conn.rollback();
                System.out.println("Error occurred, transaction rolled back.");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private static void readProduct() {
        System.out.print("Enter Product ID to read: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        String selectSQL = "SELECT * FROM product WHERE productID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Product ID: " + rs.getInt("productID"));
                System.out.println("Product Name: " + rs.getString("productName"));
                System.out.println("Price: " + rs.getFloat("price"));
                System.out.println("Quantity: " + rs.getInt("quantity"));
            } else {
                System.out.println("Product not found with ID " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateProduct() {
        System.out.print("Enter Product ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new Product Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new Price: ");
        float price = scanner.nextFloat();
        System.out.print("Enter new Quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        String updateSQL = "UPDATE product SET productName = ?, price = ?, quantity = ? WHERE productID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, name);
            pstmt.setFloat(2, price);
            pstmt.setInt(3, quantity);
            pstmt.setInt(4, id);

            conn.setAutoCommit(false);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                conn.commit();
                System.out.println("Product updated successfully!");
            } else {
                conn.rollback();
                System.out.println("No product found with ID " + id + ". Transaction rolled back.");
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
                System.out.println("Error occurred, transaction rolled back.");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private static void deleteProduct() {
        System.out.print("Enter Product ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        String deleteSQL = "DELETE FROM product WHERE productID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, id);

            conn.setAutoCommit(false);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                conn.commit();
                System.out.println("Product deleted successfully!");
            } else {
                conn.rollback();
                System.out.println("No product found with ID " + id + ". Transaction rolled back.");
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
                System.out.println("Error occurred, transaction rolled back.");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private static void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```