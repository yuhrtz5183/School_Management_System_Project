package view;

import controller.*;
import javax.swing.*;
import java.awt.*;

public class AuthenticatorView extends JFrame{
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JButton btnLogin, btnForgotPassword;
	private JLabel errorMessage;
	public JLabel firstTimeMessage;
	
	public AuthenticatorView() {
		setTitle("School Login");
		setSize(400, 250);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		buildFormPanel();
		
		buildButtonPanel();
		buildErrorMessagePanel();
	}
	
	private void buildFormPanel() {
		JLabel Username = new JLabel("Username");
		txtUsername = new JTextField();
		Font font = new Font("Arial", Font.BOLD, 22);
		Font font1 = new Font("Arial", Font.PLAIN, 16);
		Username.setFont(font);
		txtUsername.setFont(font1);
		
		JLabel Password = new JLabel("Password");
		txtPassword = new JPasswordField();
		Password.setFont(font);
		txtPassword.setFont(font1);
		
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridLayout(2, 2, 5, 5));
		formPanel.add(Username);
		formPanel.add(txtUsername);
		formPanel.add(Password);
		formPanel.add(txtPassword);
		

		
		add(formPanel, BorderLayout.NORTH);
	}
	
	private void buildButtonPanel() {
		JPanel buttonPanel = new JPanel(new GridLayout(1,2));
		
		btnLogin = new JButton("Login");
		btnForgotPassword = new JButton("Forget Password?");
		
		btnLogin.addActionListener(e -> handleLogin());
		btnForgotPassword.addActionListener(e -> openResetPasswordView());
		
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnForgotPassword);
        

        add(buttonPanel, BorderLayout.SOUTH);
	}
	
    private void buildErrorMessagePanel() {
        // Create a JLabel for the error message
        errorMessage = new JLabel("Incorrect username or password.");
        errorMessage.setFont(new Font("Arial", Font.PLAIN, 12));
        errorMessage.setForeground(Color.RED);
        
        //initially hidden until called again
        errorMessage.setVisible(false);

        firstTimeMessage = new JLabel("First time logging in? Try using the Forget Password.");
        firstTimeMessage.setFont(new Font("Arial", Font.PLAIN, 12));
        firstTimeMessage.setForeground(Color.RED);
        firstTimeMessage.setVisible(false);
        
        JPanel errorPanel = new JPanel();
        errorPanel.add(errorMessage);
        errorPanel.add(firstTimeMessage);
        
        add(errorPanel, BorderLayout.CENTER);
    }
	
	private void handleLogin() {
        String username = txtUsername.getText();
        String password = String.valueOf(txtPassword.getPassword());

        new AuthenticatorController().handleLogin(username, password, this);
    }
	
	//show error message method
    public void showErrorMessage() {
        errorMessage.setVisible(true);
    }

    //hide error message method
    public void hideErrorMessage() {
        errorMessage.setVisible(false);
    }
	
    private void openResetPasswordView() {
        new ResetPasswordView().setVisible(true);
        this.dispose();
    }
    
 
}


