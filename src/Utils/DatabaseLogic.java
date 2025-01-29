package Utils;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseLogic {

    private static final String DB_URL = "jdbc:sqlite:library.db";


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }


    public static void addBook(String title, String author, int bookId, boolean isAvailable) {

        String avabilityStatus = isAvailable ? "yes" : "no";
        String query = "INSERT INTO books (book_id, title, author, availability_status) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            stmt.setString(2, title);
            stmt.setString(3, author);
            stmt.setString(4, avabilityStatus);
            int result = stmt.executeUpdate();

            if (result > 0) {
                System.out.println("Book added successfully.");
            } else {
                System.out.println("Failed to add book.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void removeBook(int bookId) {
        String query = "DELETE FROM books WHERE book_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            int result = stmt.executeUpdate();

            if (result > 0) {
                System.out.println("Book removed successfully.");
            } else {
                System.out.println("Failed to remove book.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void searchBook(String query) {
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + query + "%");
            stmt.setString(2, "%" + query + "%");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String available = rs.getString("available");
                System.out.println("Book ID: " + bookId + ", Title: " + title + ", Author: " + author + ", Available: " + available);

                if (available.equalsIgnoreCase("NO")){
                    JOptionPane.showMessageDialog(null, "The book '" + title + "'is currently not available in the library.", "Book Unavailable", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No books found matching your search query.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while searching for books", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static List<Object[]> getBorrowingHistory() {
        String sql = "SELECT u.name AS user_name, b.title AS book_title, br.borrow_date, br.return_date " +
                "FROM borrowing_history br " +
                "JOIN users u ON br.user_id = u.user_id " +
                "JOIN books b ON br.book_id = b.book_id";

        List<Object[]> historyList = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {
                String userName = rs.getString("user_name");
                String bookTitle = rs.getString("book_title");
                Date borrowDate = rs.getDate("borrow_date");
                Date returnDate = rs.getDate("return_date");


                String returnDateStr = (returnDate != null) ? returnDate.toString() : "Not Returned";


                historyList.add(new Object[]{userName, bookTitle, borrowDate, returnDateStr});
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving borrowing history:");
            e.printStackTrace();
        }

        return historyList;
    }




    public static void borrowBook(int userId, int bookId) {
        String query = "INSERT INTO borrowing_history (user_id, book_id, borrow_date) VALUES (?, ?, NOW())";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            int result = stmt.executeUpdate();

            if (result > 0) {
                System.out.println("Book borrowed successfully.");

                updateBookAvailability(bookId, false);
            } else {
                System.out.println("Failed to borrow book.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void returnBook(int userId, int bookId) {
        String returnBookQuery = "UPDATE borrowing_records SET return_date = NOW() WHERE user_id = ? AND book_id = ? AND return_date IS NULL";
        String updateBookQuery = "UPDATE books SET available = ? WHERE book_id = ?";


        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement returnStmt = conn.prepareStatement(returnBookQuery);
                 PreparedStatement updateStmt = conn.prepareStatement(updateBookQuery)) {


                returnStmt.setInt(1, userId);
                returnStmt.setInt(2, bookId);
                int result = returnStmt.executeUpdate();

                if (result > 0) {
                    System.out.println("Book returned successfully.");


                    updateStmt.setBoolean(1, true);
                    updateStmt.setInt(2, bookId);
                    updateStmt.executeUpdate();

                    conn.commit();
                } else {
                    System.out.println("Failed to return book. No matching borrowing record found.");
                    conn.rollback();
                }

            } catch (SQLException e) {
                e.printStackTrace();
                conn.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



        public static void viewBorrowingHistory() {
            String query = "SELECT u.name, b.title, br.borrow_date, br.return_date " +
                    "FROM borrowing_history br " +
                    "JOIN users u ON br.user_id = u.user_id " +
                    "JOIN books b ON br.book_id = b.book_id";

            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                ResultSet rs = stmt.executeQuery();
                StringBuilder history = new StringBuilder();

                while (rs.next()) {
                    String userName = rs.getString("name");
                    String bookTitle = rs.getString("title");
                    String borrowDate = rs.getDate("borrow_date").toString();
                    String returnDate = rs.getDate("return_date") != null ? rs.getDate("return_date").toString() : "Not Returned";

                    history.append("User: ").append(userName)
                            .append(", Book: ").append(bookTitle)
                            .append(", Borrowed on: ").append(borrowDate)
                            .append(", Returned on: ").append(returnDate)
                            .append("\n");
                }


                JOptionPane.showMessageDialog(null, history.toString(), "Borrowing History", JOptionPane.INFORMATION_MESSAGE);

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error retrieving borrowing history.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    private static void updateBookAvailability(int bookId, boolean isAvailable) {
        String query = "UPDATE books SET available = ? WHERE book_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBoolean(1, isAvailable);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
