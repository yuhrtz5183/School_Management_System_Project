package controller;

import model.*;
import my_util.Security;
import view.*;
import javax.swing.*;

public class AuthenticatorController {
	
	public void handleLogin(String username, String password, AuthenticatorView authenticatorView) {
	    UserDA userDA = new UserDA();
	    User user = userDA.authenticateUser(username, password);
	    

	    if (user != null) {
	    	if (user.isFirstLogin()) {
	    		authenticatorView.firstTimeMessage.setVisible(true);
	            JOptionPane.showMessageDialog(authenticatorView, "You must reset your password on first login.");
	            new ResetPasswordView(user).setVisible(true);
	            authenticatorView.dispose();
	            return;
	        }
	    	
	        //switch case to get correct role after login
	        switch (user.getRole().toLowerCase()) {
	            case "admin":
	                new AdminView().initialize(user);
	                break;
	            case "teacher":
	                new TeacherView().initialize(user);
	                break;
	            case "student":
	                new StudentView().initialize(user);
	                break;
	            default:
	                JOptionPane.showMessageDialog(authenticatorView, "Unknown role: " + user.getRole());
	                return;
	        }
	        authenticatorView.dispose();
	    } 
	    else {
	        authenticatorView.showErrorMessage();
	    }
	}
	
	public void handlePasswordReset(String email, int userId, String newPassword, ResetPasswordView resetPasswordView) {
        UserDA userDA = new UserDA();
        User user = userDA.getUserById(userId);

        if (user != null && user.getEmail().equals(email)) {
            
        	//password hashing and update data table
        	String hashedPassword = Security.hashPassword(newPassword);
            userDA.updatePassword(userId, hashedPassword);
            userDA.setFirstLoginFalse(userId);
            
            JOptionPane.showMessageDialog(null, "Password reset successfully. You can now log in with the new password.");

            resetPasswordView.navigateToAuthenticatorView();
        } 
        else {
            resetPasswordView.showErrorMessage(newPassword);
        }
    }
}
