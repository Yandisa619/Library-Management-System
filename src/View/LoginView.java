package View;

import Utils.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginView extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnCancel;

    public LoginView() {
        setTitle("Library Management System - Login");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel headingLabel = new JLabel("LIBRARY MANAGEMENT SYSTEM", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headingLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
        mainPanel.add(headingLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Email:"), gbc);

        txtEmail = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);

        txtPassword = new JPasswordField(20);
        gbc.gridx = 1;
        formPanel.add(txtPassword, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        btnLogin = new JButton("Login");
        bottomPanel.add(btnLogin, gbc);

        btnCancel = new JButton("Cancel");
        gbc.gridy = 1;
        bottomPanel.add(btnCancel, gbc);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        btnLogin.addActionListener(e -> handleLogin());

        btnCancel.addActionListener(e -> System.exit(0));
    }

    private void handleLogin() {
        String email = txtEmail.getText();
        char[] password = txtPassword.getPassword();

        if (validateCredentials(email, password)) {
            String role = getRole(email);
            String name = getName(email);
            if (role != null) {
                if (role.equals("Admin")) {
                    JOptionPane.showMessageDialog(this, "Welcome Librarian, " + name);

                    AdminDashboardView adminDashboard = new AdminDashboardView();
                    adminDashboard.setVisible(true);
                    this.dispose();
                } else if (role.equals("User")) {
                    JOptionPane.showMessageDialog(this, "Welcome " + name);

                    UserDashboardView userDashboard = new UserDashboardView();
                    userDashboard.setVisible(true);
                    this.dispose();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.");
        }
    }


    private boolean validateCredentials(String email, char[] password) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE email = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, email);
                stmt.setString(2, new String(password));
                ResultSet rs = stmt.executeQuery();
                return rs.next();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private String getRole(String email) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT role FROM users WHERE email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getString("role");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private String getName(String email) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT name FROM users WHERE email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DatabaseConnection.initializeDatabase();
            LoginView view = new LoginView();
            view.setVisible(true);
        });
    }
}
