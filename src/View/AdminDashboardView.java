package View;

import Utils.DatabaseLogic;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class AdminDashboardView extends JFrame {

    private JButton btnAddBook;
    private JButton btnRemoveBook;
    private JButton btnViewHistory;
    private JButton btnSearchBook;
    private JTextField txtSearchBook;

    public AdminDashboardView() {
        setTitle("Librarian Dashboard - Library Management System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);


        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel headingLabel = new JLabel("LIBRARY MANAGEMENT SYSTEM - ADMIN", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headingLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
        mainPanel.add(headingLabel, BorderLayout.NORTH);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));


        btnAddBook = createButton("Add Book", "Add a new book to the library");
        btnRemoveBook = createButton("Remove Book", "Remove an existing book");
        btnViewHistory = createButton("View Borrowing History", "View history of borrowed books");
        btnSearchBook = createButton("Search Book", "Search for a book by title or author");

        buttonPanel.add(createButtonPanel(btnAddBook));
        buttonPanel.add(createButtonPanel(btnRemoveBook));
        buttonPanel.add(createButtonPanel(btnSearchBook));
        buttonPanel.add(createButtonPanel(btnViewHistory));


        txtSearchBook = new JTextField(20);


        mainPanel.add(buttonPanel, BorderLayout.CENTER);


        add(mainPanel);


        btnAddBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });

        btnRemoveBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeBook();
            }
        });

        btnSearchBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBook();
            }
        });

        btnViewHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewHistory();
            }
        });
    }


    private JButton createButton(String buttonText, String toolTip) {
        JButton button = new JButton(buttonText);
        button.setToolTipText(toolTip);
        return button;
    }


    private JPanel createButtonPanel(JButton button) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(button, BorderLayout.CENTER);
        return panel;
    }


    private void addBook() {
        String title = JOptionPane.showInputDialog(this, "Enter Book Title:");
        String author = JOptionPane.showInputDialog(this, "Enter Author Name:");
        int bookId = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Book ID:"));
        boolean isAvailable = JOptionPane.showConfirmDialog(this, "Is the book available?", "Availability", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;


        DatabaseLogic.addBook(title, author, bookId, isAvailable);
    }


    private void removeBook() {
        int bookId = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Book ID to remove:"));
        DatabaseLogic.removeBook(bookId);
    }


    private void searchBook() {
        String query = txtSearchBook.getText();
        if (query.isEmpty()) {
            JOptionPane.showInputDialog("Search Book By Title or Author:");
            return;
        }

        DatabaseLogic.searchBook(query);
    }



    private void viewHistory() {

        List<Object[]> historyData = DatabaseLogic.getBorrowingHistory();


        String[] columnNames = {"User Name", "Book Title", "Borrow Date", "Return Date"};


        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);


        for (Object[] row : historyData) {
            tableModel.addRow(row);
        }


        JTable historyTable = new JTable(tableModel);


        JScrollPane scrollPane = new JScrollPane(historyTable);


        JFrame frame = new JFrame("Borrowing History");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.add(scrollPane);
        frame.setVisible(true);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminDashboardView view = new AdminDashboardView();
            view.setVisible(true);
        });
    }
}
