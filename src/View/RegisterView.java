package View;
import Utils.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

public class RegisterView extends JFrame {

    private JTextField txtUsername;
    private JTextField txtName;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JComboBox<String> roleComboBox;
    private JButton btnGeneratePassword;
    private JButton btnSubmit;
    private JButton btnCancel;

    public RegisterView() {

        DatabaseConnection.initializeDatabase();
        setTitle("Library Management System - Registration");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel headingLabel = new JLabel("LIBRARY MANAGEMENT SYSTEM", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headingLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,0));
        mainPanel.add(headingLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);

        txtUsername = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Name:"), gbc);

        txtName = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Email:"), gbc);

        txtEmail = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Password:"), gbc);

        txtPassword = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Confirm Password:"), gbc);

        txtConfirmPassword = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(txtConfirmPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Role:"), gbc);

        String[] roles = {"User", "Admin"};
        roleComboBox = new JComboBox<>(roles);
        gbc.gridx = 1;
        panel.add(roleComboBox, gbc);


        btnGeneratePassword = new JButton("Generate Password");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(btnGeneratePassword, gbc);


        btnSubmit = new JButton("Submit");
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        panel.add(btnSubmit, gbc);


        btnCancel = new JButton("Cancel");
        gbc.gridx = 1;
        panel.add(btnCancel, gbc);

        mainPanel.add(panel, BorderLayout.CENTER);
        setContentPane(mainPanel);


        btnGeneratePassword.addActionListener(e -> generatePassword());
        btnSubmit.addActionListener(e -> handleSubmit());
        btnCancel.addActionListener(e -> handleCancel());
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
        String username = txtName.getText().trim();
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
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

        if (saveUserToDatabase(name, email, password, role, username)) {
            JOptionPane.showMessageDialog(this, "Registration Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            redirectToLoginPage();
        } else {
            JOptionPane.showMessageDialog(this, "Registration Failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCancel() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel?", "Confirm Cancel", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    private boolean isEmailAlreadyRegistered(String email) {
        String query = "SELECT email FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean saveUserToDatabase(String name, String email, String password, String role, String username) {
        String insertQuery = "INSERT INTO users (name, email, password, role, username) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, role);
            pstmt.setString(5, username);

            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
