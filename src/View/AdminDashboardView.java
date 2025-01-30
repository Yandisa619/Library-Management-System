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
    private JButton viewBooksButton;
    private JButton btnSearchBook;
    private JButton btnLogout;
    private JTextField txtSearchBook;

    public AdminDashboardView() {
        setTitle("Librarian Dashboard - Library Management System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);


        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel headingLabel = new JLabel("LIBRARY MANAGEMENT SYSTEM - ADMIN", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headingLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
        mainPanel.add(headingLabel, BorderLayout.NORTH);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));


        btnAddBook = createButton("Add Book", "Add a new book to the library");
        btnRemoveBook = createButton("Remove Book", "Remove an existing book");
        btnViewHistory = createButton("View Borrowing History", "View history of borrowed books");
        viewBooksButton = createButton("View Books", "View available books in the library");
        btnSearchBook = createButton("Search Book", "Search for a book by title or author");
        btnLogout = createButton("Logout", "Logout and return to login page");


        buttonPanel.add(createButtonPanel(btnAddBook));
        buttonPanel.add(createButtonPanel(btnRemoveBook));
        buttonPanel.add(createButtonPanel(btnSearchBook));
        buttonPanel.add(createButtonPanel(btnViewHistory));
        buttonPanel.add(createButtonPanel(viewBooksButton));
        buttonPanel.add(createButtonPanel(btnLogout));

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setPreferredSize(new Dimension(350, 300));
        containerPanel.add(buttonPanel, BorderLayout.CENTER);


        txtSearchBook = new JTextField(20);


        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);

        btnLogout.addActionListener(e -> logout());

        viewBooksButton.addActionListener(e -> showAvailableBooks());

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
        button.setPreferredSize(new Dimension(250, 50));
        button.setMaximumSize(new Dimension(300, 60));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(button);
        return panel;
    }

    public static void showAvailableBooks() {
        DatabaseLogic.showAvailableBooks();
    }

    private void addBook() {
        String title = JOptionPane.showInputDialog(this, "Enter Book Title:");
        String author = JOptionPane.showInputDialog(this, "Enter Author Name:");
        int bookId = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Book ID:"));

        int isAvailable = JOptionPane.showConfirmDialog(this, "Is the book available?\n1 means Available", "Availability", JOptionPane.YES_NO_OPTION);

        if (isAvailable == JOptionPane.YES_OPTION) {
            isAvailable = 1;
        } else {
            isAvailable = 0;
        }

        JOptionPane.showMessageDialog(this, "You selected: " + (isAvailable == 1 ? "Available" : "Unavailable") + "\n1 means Available and 0 means Unavailable");


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

    private void logout() {
        dispose();
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
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
