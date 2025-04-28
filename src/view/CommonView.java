package view;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;

public class CommonView extends JFrame {
	
	public static JPanel profilePanel(User user) {
	    JPanel panel = new JPanel(new BorderLayout());
	    
	    //header for profile
	    JLabel header = new JLabel("Profile", SwingConstants.CENTER);
	    header.setFont(new Font("Arial", Font.BOLD, 20));

	    //top panel for picture and button
	    JPanel topPanel = new JPanel();
	    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

	    JLabel pictureLabel = new JLabel();
	    pictureLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    pictureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

	    if (user.getProfilePicturePath() != null && !user.getProfilePicturePath().isEmpty()) {
	        ImageIcon imageIcon = new ImageIcon(user.getProfilePicturePath());
	        Image image = imageIcon.getImage().getScaledInstance(225, 225, Image.SCALE_SMOOTH);
	        pictureLabel.setIcon(new ImageIcon(image));
	    } else {
	        ImageIcon defaultIcon = new ImageIcon("Downloads/default_profile_pic");
	        Image image = defaultIcon.getImage().getScaledInstance(225, 225, Image.SCALE_SMOOTH);
	        pictureLabel.setIcon(new ImageIcon(image));
	    }
	    
	    //creating upload button
	    JButton uploadButton = new JButton("Upload Picture");
	    uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	    uploadButton.setFont(new Font("Arial", Font.PLAIN, 14));
	    
	    //go to file explore to get a file to upload as image
	    uploadButton.addActionListener(e -> {
	        JFileChooser fileChooser = new JFileChooser();

	        int result = fileChooser.showOpenDialog(panel);
	        if (result == JFileChooser.APPROVE_OPTION) {
	            File selectedFile = fileChooser.getSelectedFile();
	            try {
	                File destinationDir = new File("Downloads/default_profile_pic");
	                if (!destinationDir.exists()) {
	                    destinationDir.mkdirs();
	                }
	                File destination = new File(destinationDir, user.getId() + ".png");
	                Files.copy(selectedFile.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);

	                String savedPath = destination.getPath();
	                user.setProfilePicturePath(savedPath);
	                new UserDA().updateProfilePicture(user.getId(), savedPath);

	                ImageIcon newIcon = new ImageIcon(savedPath);
	                Image newImage = newIcon.getImage().getScaledInstance(225, 225, Image.SCALE_SMOOTH);
	                pictureLabel.setIcon(new ImageIcon(newImage));

	                JOptionPane.showMessageDialog(panel, "Profile picture updated successfully!");

	            } catch (IOException ex) {
	                ex.printStackTrace();
	                JOptionPane.showMessageDialog(panel, "Failed to upload picture.");
	            }
	        }
	    });

	    topPanel.add(pictureLabel);
	    
	    //space for upload button and picture
	    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
	    topPanel.add(uploadButton);

	    //info panel
	    JPanel infoPanel = new JPanel();
	    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
	    infoPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

	    Font labelFont = new Font("Arial", Font.BOLD, 30);
	    Font valueFont = new Font("Arial", Font.PLAIN, 26);

	    infoPanel.add(createInfoPanel("First Name:", user.getfirstName(), labelFont, valueFont));
	    infoPanel.add(createInfoPanel("Last Name:", user.getlastName(), labelFont, valueFont));
	    infoPanel.add(createInfoPanel("Email:", user.getEmail(), labelFont, valueFont));
	    infoPanel.add(createInfoPanel("Role:", user.getRole(), labelFont, valueFont));
	    infoPanel.add(createInfoPanel("ID:", String.valueOf(user.getId()), labelFont, valueFont));

	    //add to main panel
	    panel.add(header, BorderLayout.NORTH);
	    panel.add(topPanel, BorderLayout.CENTER);
	    panel.add(infoPanel, BorderLayout.SOUTH);

	    return panel;
	}
	
	private static JPanel createInfoPanel(String labelText, String valueText, Font labelFont, Font valueFont) {
	    JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

	    JLabel label = new JLabel(labelText);
	    label.setFont(labelFont);

	    JLabel value = new JLabel(valueText);
	    value.setFont(valueFont);

	    rowPanel.add(label);
	    rowPanel.add(value);

	    return rowPanel;
	}
	
}

