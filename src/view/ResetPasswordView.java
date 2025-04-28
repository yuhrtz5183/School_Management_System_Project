package view;

import controller.*;
import model.User;
import javax.swing.*;
import java.awt.*;

public class ResetPasswordView extends JFrame {
    private JTextField txtEmail, txtUserId;
    private JPasswordField txtNewPassword, txtConfirmPassword;
    private JButton btnSubmit;
    private JLabel errorMessage;
    private User user; // To handle first-time login forced password reset

    public ResetPasswordView() {
        this(null); // Default constructor calls the other one
    }

    public ResetPasswordView(User user) {
        this.user = user;

        setTitle("Reset Password");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);

        buildFormPanel();
        buildButtonPanel();
        buildErrorMessagePanel();
    }

    private void buildFormPanel() {
        JLabel lblEmail = new JLabel("Email");
        JLabel lblUserId = new JLabel("User ID");
        JLabel lblNewPassword = new JLabel("New Password");
        JLabel lblConfirmPassword = new JLabel("Confirm Password");

        txtEmail = new JTextField();
        txtUserId = new JTextField();
        txtNewPassword = new JPasswordField();
        txtConfirmPassword = new JPasswordField();

        Font labelFont = new Font("Arial", Font.BOLD, 16);
        Font inputFont = new Font("Arial", Font.PLAIN, 14);
        lblEmail.setFont(labelFont);
        lblUserId.setFont(labelFont);
        lblNewPassword.setFont(labelFont);
        lblConfirmPassword.setFont(labelFont);

        txtEmail.setFont(inputFont);
        txtUserId.setFont(inputFont);
        txtNewPassword.setFont(inputFont);
        txtConfirmPassword.setFont(inputFont);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.add(lblEmail);
        formPanel.add(txtEmail);
        formPanel.add(lblUserId);
        formPanel.add(txtUserId);
        formPanel.add(lblNewPassword);
        formPanel.add(txtNewPassword);
        formPanel.add(lblConfirmPassword);
        formPanel.add(txtConfirmPassword);

        if (user != null) {
            // Pre-fill email and userId if user is provided (first login reset)
            txtEmail.setText(user.getEmail());
            txtUserId.setText(String.valueOf(user.getId()));
            txtEmail.setEditable(false);
            txtUserId.setEditable(false);
        }

        add(formPanel, BorderLayout.CENTER);
    }

    private void buildButtonPanel() {
        btnSubmit = new JButton("Submit");
        btnSubmit.setFont(new Font("Arial", Font.BOLD, 14));
        btnSubmit.addActionListener(e -> handlePasswordResetRequest());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSubmit);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void buildErrorMessagePanel() {
        errorMessage = new JLabel("Email or User ID is incorrect.");
        errorMessage.setFont(new Font("Arial", Font.PLAIN, 12));
        errorMessage.setForeground(Color.RED);
        errorMessage.setVisible(false);

        JPanel errorPanel = new JPanel();
        errorPanel.add(errorMessage);
        add(errorPanel, BorderLayout.NORTH);
    }

    private void handlePasswordResetRequest() {
        String email = txtEmail.getText().trim();
        String userIdStr = txtUserId.getText().trim();
        String newPassword = String.valueOf(txtNewPassword.getPassword()).trim();
        String confirmPassword = String.valueOf(txtConfirmPassword.getPassword()).trim();

        if (email.isEmpty() || userIdStr.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showErrorMessage("All fields are required.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showErrorMessage("Passwords do not match.");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdStr);
            new AuthenticatorController().handlePasswordReset(email, userId, newPassword, this);
        } catch (NumberFormatException ex) {
            showErrorMessage("Invalid User ID.");
        }
    }

    public void showErrorMessage(String message) {
        errorMessage.setText(message);
        errorMessage.setVisible(true);
    }

    public void hideErrorMessage() {
        errorMessage.setVisible(false);
    }
    
    public void navigateToAuthenticatorView() {
        new AuthenticatorView().setVisible(true);  // Show the AuthenticatorView again
        this.setVisible(false);  // Hide the ResetPasswordView
    }
}
