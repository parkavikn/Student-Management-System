import java.sql.*;
import java.util.Scanner;

public class StudentManagement {
    static final String URL = "jdbc:mysql://localhost:3306/studentdb";
    static final String USER = "root";
    // Replace with your MySQL password
    static final String PASSWORD = "your_password";

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
                System.out.println("Database Connected Successfully!");
                int choice;
                do {
                    System.out.println("\n--- Student Management System ---");
                    System.out.println("1. Insert Student");
                    System.out.println("2. View Students");
                    System.out.println("3. Search Student");
                    System.out.println("4. Update Student");
                    System.out.println("5. Delete Student");
                    System.out.println("6. Exit");
                    System.out.print("Enter your choice: ");
                    choice = sc.nextInt();

                    switch (choice) {
                        case 1: insertStudent(con); break;
                        case 2: viewStudents(con); break;
                        case 3: searchStudent(con); break;
                        case 4: updateStudent(con); break;
                        case 5: deleteStudent(con); break;
                        case 6: System.out.println("Application Closed."); break;
                        default: System.out.println("Invalid Choice!");
                    }
                } while (choice != 6);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void insertStudent(Connection con) throws SQLException {
        System.out.print("Enter Roll Number: ");
        int roll = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Department: ");
        String dept = sc.nextLine();
        System.out.print("Enter Marks: ");
        double marks = sc.nextDouble();

        String sql = "INSERT INTO student VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, roll);
            ps.setString(2, name);
            ps.setString(3, dept);
            ps.setDouble(4, marks);
            ps.executeUpdate();
            System.out.println("Student Inserted Successfully!");
        }
    }

    static void viewStudents(Connection con) throws SQLException {
        String sql = "SELECT * FROM student";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\nRoll No | Name | Department | Marks");
            while (rs.next()) {
                System.out.println(rs.getInt("roll_no") + " | "
                    + rs.getString("name") + " | "
                    + rs.getString("department") + " | "
                    + rs.getDouble("marks"));
            }
        }
    }

    static void searchStudent(Connection con) throws SQLException {
        System.out.print("Enter Roll Number to Search: ");
        int roll = sc.nextInt();
        String sql = "SELECT * FROM student WHERE roll_no = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, roll);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Roll Number: " + rs.getInt("roll_no"));
                    System.out.println("Name: " + rs.getString("name"));
                    System.out.println("Department: " + rs.getString("department"));
                    System.out.println("Marks: " + rs.getDouble("marks"));
                } else {
                    System.out.println("Student Not Found!");
                }
            }
        }
    }

    static void updateStudent(Connection con) throws SQLException {
        System.out.print("Enter Roll Number to Update: ");
        int roll = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter New Name: ");
        String name = sc.nextLine();
        System.out.print("Enter New Department: ");
        String dept = sc.nextLine();
        System.out.print("Enter New Marks: ");
        double marks = sc.nextDouble();

        String sql = "UPDATE student SET name = ?, department = ?, marks = ? WHERE roll_no = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, dept);
            ps.setDouble(3, marks);
            ps.setInt(4, roll);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Student Updated Successfully!" : "Student Not Found!");
        }
    }

    static void deleteStudent(Connection con) throws SQLException {
        System.out.print("Enter Roll Number to Delete: ");
        int roll = sc.nextInt();
        String sql = "DELETE FROM student WHERE roll_no = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, roll);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Student Deleted Successfully!" : "Student Not Found!");
        }
    }
}
