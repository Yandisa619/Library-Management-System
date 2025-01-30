package View;
import Utils.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Random;

public class RegisterView extends JFrame {

    private JTextField txtUsername;
    private JTextField txtName;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JComboBox<String> roleComboBox;
    private JButton btnGeneratePassword;
    private JCheckBox chkShowPassword;

    private JButton btnSubmit;
    private JButton btnCancel;

    public RegisterView() {

        DatabaseConnection.initializeDatabase();
        setTitle("Library Management System - Registration");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel headingLabel = new JLabel("LIBRARY MANAGEMENT SYSTEM", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headingLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,0));
        mainPanel.add(headingLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);

        txtUsername = new JTextField(20);
        txtUsername.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(txtUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Name:"), gbc);

        txtName = new JTextField(20);
        txtName.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(txtName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Email:"), gbc);

        txtEmail = new JTextField(20);
        txtEmail.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Password:"), gbc);

        txtPassword = new JPasswordField(20);
        txtPassword.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Confirm Password:"), gbc);

        txtConfirmPassword = new JPasswordField(20);
        txtConfirmPassword.setPreferredSize(new Dimension(200,30));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(txtConfirmPassword, gbc);

        chkShowPassword = new JCheckBox("Show Password");
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(chkShowPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Role:"), gbc);

        String[] roles = {"User", "Admin"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(roleComboBox, gbc);


        btnGeneratePassword = new JButton("Generate Password");
        btnGeneratePassword.setPreferredSize(new Dimension(200, 40));
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnGeneratePassword, gbc);


        btnSubmit = new JButton("Submit");
        btnSubmit.setPreferredSize(new Dimension(150, 40));
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnSubmit, gbc);


        btnCancel = new JButton("Cancel");
        btnCancel.setPreferredSize(new Dimension(150, 40));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnCancel, gbc);

        mainPanel.add(panel, BorderLayout.CENTER);
        setContentPane(mainPanel);


        btnGeneratePassword.addActionListener(e -> generatePassword());
        btnSubmit.addActionListener(e -> handleSubmit());
        btnCancel.addActionListener(e -> handleCancel());

        chkShowPassword.addActionListener(e -> {
            if (chkShowPassword.isSelected()) {
                txtPassword.setEchoChar((char) 0); // Show password
                txtConfirmPassword.setEchoChar((char) 0);
            } else {
                txtPassword.setEchoChar('*'); // Hide password
                txtConfirmPassword.setEchoChar('*');
            }
        });
    }




    private void generatePassword() {
        String generatedPassword = generateRandomPassword(12);
        txtPassword.setText(generatedPassword);
        txtConfirmPassword.setText(generatedPassword);
        JOptionPane.showMessageDialog(this, "Password generated and filled automatically.", "Password Generator", JOptionPane.INFORMATION_MESSAGE);
    }

    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            password.append(chars.charAt(index));
        }

        return password.toString();
    }



 

    private void handleSubmit() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        String username = txtUsername.getText().trim();
        String role = (String) roleComboBox.getSelectedItem();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (isEmailAlreadyRegistered(email)) {
            JOptionPane.showMessageDialog(this, "Email address is already registered!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int userId = saveUserToDatabase(name, email, password, role, username);
        if (userId != -1) {
            JOptionPane.showMessageDialog(this, "Registration Successful!\nYour User ID: " + userId, "Success", JOptionPane.INFORMATION_MESSAGE);
            redirectToLoginPage();
        } else {
            JOptionPane.showMessageDialog(this, "Registration Failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int fetchUserId(String email) {
        String query = "SELECT user_id FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if user ID is not found
    }

    private int saveUserToDatabase(String name, String email, String password, String role, String username) {
        String insertQuery = "INSERT INTO users (name, email, password, role, username) VALUES (?, ?, ?, ?, ?)";
        String getIdQuery = "SELECT last_insert_rowid() AS user_id";  // Works for SQLite; Use `SELECT LAST_INSERT_ID()` for MySQL

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery);
             Statement stmt = conn.createStatement()) {

            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, role);
            pstmt.setString(5, username);
            pstmt.executeUpdate();

            ResultSet rs = stmt.executeQuery(getIdQuery);
            if (rs.next()) {
                return rs.getInt("user_id"); // Return user_id instead of boolean
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if insertion fails
    }

    private boolean isEmailAlreadyRegistered(String email) {
        String query = "SELECT email FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // If a result exists, the email is already registered
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void handleCancel() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel?", "Confirm Cancel", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            dispose();  // Close the current form
        }
    }


    private void redirectToLoginPage() {
        dispose();
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegisterView registerView = new RegisterView();
            registerView.setVisible(true);
        });
    }
}
