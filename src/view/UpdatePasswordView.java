package view;

import model.*;
import my_util.*;
import javax.swing.*;
import java.awt.*;

public class UpdatePasswordView extends JFrame {
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton btnUpdate;

    private User user;

    public UpdatePasswordView(User user) {
        this.user = user;

        setTitle("Update Password");
        setLayout(new GridLayout(3, 2, 5, 5));

        add(new JLabel("New Password:"));
        newPasswordField = new JPasswordField();
        add(newPasswordField);

        add(new JLabel("Confirm Password:"));
        confirmPasswordField = new JPasswordField();
        add(confirmPasswordField);

        btnUpdate = new JButton("Update Password");
        btnUpdate.addActionListener(e -> updatePassword());
        add(btnUpdate);

        setSize(400, 200);
        setLocationRelativeTo(null);
        setVisible(true);
        
        
    }

    private void updatePassword() {
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!");
            return;
        }

        // Update password in database and set first_login = false
        String query = "UPDATE tb_user SET password = ?, first_login = FALSE WHERE id = ?";
        new Database().executeUpdate(query, stmt -> {
            stmt.setString(1, Security.hashPassword(newPassword)); // Hash here
            stmt.setInt(2, user.getId());
        });

        JOptionPane.showMessageDialog(this, "Password updated successfully!");

        // After updating, go to their dashboard
        if (user.getRole().equalsIgnoreCase("student")) {
            new StudentView().initialize(user);
        } else if (user.getRole().equalsIgnoreCase("teacher")) {
            new TeacherView().initialize(user);
        } else if (user.getRole().equalsIgnoreCase("admin")) {
            new AdminView().initialize(user);
        }

        this.dispose();
    }


}
