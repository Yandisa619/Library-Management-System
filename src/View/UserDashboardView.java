package View;

import Utils.DatabaseLogic;

import javax.swing.*;
import java.awt.*;

public class UserDashboardView extends JFrame {

    private JButton btnBorrowBook;
    private JButton viewBooksButton;
    private JButton btnReturnBook;
    private JButton btnViewHistory;
    private final JButton btnSearchBook;
    private JButton btnLogout;
    private final JTextField txtSearchBook;


    public UserDashboardView() {
        setTitle("User Dashboard - Library Management System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel searchPanel = new JPanel(new FlowLayout());
        txtSearchBook = new JTextField(20);
        btnSearchBook = new JButton("Search Book");
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(txtSearchBook);
        searchPanel.add(btnSearchBook);
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        JLabel headingLabel = new JLabel("LIBRARY MANAGEMENT SYSTEM - USER", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headingLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
        mainPanel.add(headingLabel, BorderLayout.NORTH);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));


        btnBorrowBook = new JButton("Borrow Book");
        btnReturnBook = new JButton("Return Book");
        btnViewHistory = new JButton("View Borrowing History");
        viewBooksButton = new JButton("View Available Books");
        btnLogout = new JButton("Logout");

        buttonPanel.add(createButtonPanel(btnBorrowBook, "Borrow a book from the library"));
        buttonPanel.add(createButtonPanel(btnReturnBook, "Return a borrowed book"));
        buttonPanel.add(createButtonPanel(btnViewHistory, "View your borrowing history"));
        buttonPanel.add(createButtonPanel(viewBooksButton, "View Available Books"));
        buttonPanel.add(createButtonPanel(btnLogout, "Logout and return to Login Page"));
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setPreferredSize(new Dimension(350, 300));
        containerPanel.add(buttonPanel, BorderLayout.CENTER);

        mainPanel.add(containerPanel, BorderLayout.CENTER);

        add(mainPanel);
        addListeners();
    }

    private void addListeners() {

        btnSearchBook.addActionListener(e -> searchBook());


        btnBorrowBook.addActionListener(e -> borrowBook());


        btnReturnBook.addActionListener(e -> returnBook());

        viewBooksButton.addActionListener(e -> showAvailableBooks());

        btnViewHistory.addActionListener(e -> viewHistory());

        btnLogout.addActionListener(e -> logout());
    }

    private JPanel createButtonPanel(JButton button, String toolTip) {
        button.setPreferredSize(new Dimension(250, 50));
        button.setMaximumSize(new Dimension(300, 60));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        button.setToolTipText(toolTip);
        panel.add(button);
        return panel;
    }

    public JButton getBorrowBookButton() {
        return btnBorrowBook;
    }

    public JButton getReturnBookButton() {
        return btnReturnBook;
    }

    public JButton getViewHistoryButton() {
        return btnViewHistory;
    }

    public JButton getViewBooksButton(){return viewBooksButton;}

    private void logout() {
        dispose();
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }

    public static void showAvailableBooks() {
        DatabaseLogic.showAvailableBooks();
    }

    private void searchBook() {
        String query = txtSearchBook.getText();
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a title or author to search.");
            return;
        }


        DatabaseLogic.searchBook(query);
    }

    private void borrowBook() {
        try {
            int bookId = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Book ID to Borrow:"));
            int userId = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Your User ID:"));
            DatabaseLogic.borrowBook(userId, bookId);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for Book ID and User ID.");
        }
    }

    private void returnBook() {
        try {
            int bookId = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Book ID to Return:"));
            int userId = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Your User ID:"));
            DatabaseLogic.returnBook(userId, bookId);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for Book ID and User ID.");
        }
    }

    private void viewHistory() {

        DatabaseLogic.viewBorrowingHistory();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserDashboardView view = new UserDashboardView();
            view.setVisible(true);
        });
    }
}
