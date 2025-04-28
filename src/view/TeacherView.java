package view;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import model.*;
import controller.*;

public class TeacherView extends JFrame {
    private JTable courseTable, emailTable, studentTable;
    private JButton btnProfile, btnCourses, btnEmail, btnStudents, btnBack;
    private JPanel centerPanel;
    private User currentUser;
    private TeacherController controller;
    private int teacherId;

    public void initialize(User user) {
        this.teacherId = user.getId();
        controller = new TeacherController(this);

        setTitle("Teacher Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        
        this.currentUser = user;

        loadButtons();

        centerPanel = new JPanel(new BorderLayout());
        add(centerPanel, BorderLayout.CENTER);

        loadProfilePanel(user);

        setVisible(true);
    }
    
    private void loadButtons() {
    	JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
    	add(buttonPanel, BorderLayout.WEST);
    	
    	btnProfile = new JButton("Profile");
    	btnProfile.addActionListener(e -> loadProfilePanel(currentUser));
    	buttonPanel.add(btnProfile);
    	
    	btnCourses = new JButton("My Courses");
    	btnCourses.addActionListener(e -> controller.DisplayAssignedCourses(teacherId));
    	buttonPanel.add(btnCourses);
    	
    	btnEmail = new JButton("Email");
    	btnEmail.addActionListener(e -> controller.displayEmails(teacherId));
    	buttonPanel.add(btnEmail);
    	
    	btnStudents = new JButton("View Students");
    	btnStudents.addActionListener(e -> {
    	    // Ensure that a course is selected
    	    int selectedRow = courseTable.getSelectedRow();
    	    if (selectedRow >= 0) {
    	        String courseCode = courseTable.getValueAt(selectedRow, 0).toString();
    	        controller.displayEnrolledStudents(teacherId, courseCode);
    	    } else {
    	        JOptionPane.showMessageDialog(this, "Please select a course first.", "Error", JOptionPane.ERROR_MESSAGE);
    	    }
    	});
    	
    	btnBack = new JButton("Back");  // Initialize the Back button
    	btnBack.setPreferredSize(new Dimension(80, 30));
        btnBack.addActionListener(e -> {
            // Go back to the course list
            controller.DisplayAssignedCourses(teacherId);
        });
    }
    
    private void loadProfilePanel(User user) {
    	centerPanel.removeAll();
        centerPanel.add(CommonView.profilePanel(user), BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    public void displayAssignedCourses(ArrayList<Course> courses) {
        centerPanel.removeAll();

        // Initialize courseTable
        if (courseTable == null) {
            courseTable = new JTable();
        }

        JScrollPane scrollPane = new JScrollPane(courseTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        String[] columnNames = { "Code", "Name", "Description", "Status", "Max Capacity" };
        Object[][] data = new Object[courses.size()][5];

        for (int i = 0; i < courses.size(); i++) {
            Course c = courses.get(i);
            data[i][0] = c.getCode();
            data[i][1] = c.getName();
            data[i][2] = c.getDescription();
            data[i][3] = c.getStatus();
            data[i][4] = c.getMaxCapacity();
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        courseTable.setModel(model);

        // Add MouseListener to handle double-click event
        courseTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Mouse clicked on course table!");
                if (e.getClickCount() == 1) { // Check for double-click
                    int selectedRow = courseTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        // Get the course code from the selected row
                        String courseCode = courseTable.getValueAt(selectedRow, 0).toString();
                        System.out.println("Selected course code: " + courseCode); // Log the selected course code
                        // Fetch and display the enrolled students for the selected course
                        controller.displayEnrolledStudents(teacherId, courseCode);
                    }
                }
            }
        });

        centerPanel.revalidate();
        centerPanel.repaint();
    }
    
    public void displayEmails(ArrayList<Email> emails) {
        centerPanel.removeAll();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(550);
        centerPanel.add(splitPane, BorderLayout.CENTER);

        // LEFT PANEL: Inbox Table
        String[] columnNames = { "Subject", "Timestamp", "Status" };
        Object[][] data = new Object[emails.size()][3];

        for (int i = 0; i < emails.size(); i++) {
            Email e = emails.get(i);
            data[i][0] = e.getSubject();
            data[i][1] = e.getTimestamp();
            data[i][2] = e.getStatus();
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        emailTable = new JTable(model);
        JScrollPane tableScroll = new JScrollPane(emailTable);
        splitPane.setLeftComponent(tableScroll);

        // RIGHT PANEL: Email Details
        JPanel detailPanel = new JPanel(new BorderLayout());
        detailPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top panel: Reply Button
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton btnReply = new JButton("Reply");
        btnReply.setVisible(false);
        topPanel.add(btnReply, BorderLayout.EAST);
        detailPanel.add(topPanel, BorderLayout.NORTH);

        // Center panel: Email details
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        
        JLabel fromField = new JLabel("From: ");
        JLabel subjectField = new JLabel("Subject: ");
        JLabel classCodeField = new JLabel("Class Code: ");
        JLabel timestampField = new JLabel("Timestamp: ");
        JTextArea messageArea = new JTextArea(10, 30);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setEditable(false);
        JScrollPane messageScroll = new JScrollPane(messageArea);

        infoPanel.add(fromField);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(subjectField);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(classCodeField);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(timestampField);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(messageScroll);

        detailPanel.add(infoPanel, BorderLayout.CENTER);

        splitPane.setRightComponent(detailPanel);

        emailTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = emailTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Email selectedEmail = emails.get(selectedRow);

                    // Update the email details display
                    fromField.setText("From: " + selectedEmail.getSender());
                    subjectField.setText("Subject: " + selectedEmail.getSubject());
                    classCodeField.setText("Class Code: " + selectedEmail.getClassCode());
                    timestampField.setText("Timestamp: " + selectedEmail.getTimestamp());
                    messageArea.setText(selectedEmail.getMessage());

                    // Show the Reply button
                    btnReply.setVisible(true);

                    // If the email is not read, mark it as read
                    if (!selectedEmail.getStatus().equals("read")) {
                        model.setValueAt("read", selectedRow, 2);
                        selectedEmail.setStatus("read");

                        Teacher modelHelper = new Teacher();
                        modelHelper.updateEmailStatus(selectedEmail.getId(), "read");
                    }

                    // Remove existing action listeners to avoid multiple listeners being added
                    for (ActionListener listener : btnReply.getActionListeners()) {
                        btnReply.removeActionListener(listener);
                    }

                    // Add the action listener for the reply button
                    btnReply.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Pass the recipient details along with the course code
                            openReplyWindow(selectedEmail.getRecipientId(), selectedEmail.getSender(), selectedEmail.getClassCode());
                            
                        }
                    });
                }
            }
        });

        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void openReplyWindow(int emailId, String recipientName, String courseCode) {
        JTextField subjectField = new JTextField();
        JTextArea messageArea = new JTextArea(10, 30);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(new JLabel("To: " + recipientName), BorderLayout.NORTH);

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.add(new JLabel("Subject:"));
        fieldsPanel.add(subjectField);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(new JLabel("Message:"));
        fieldsPanel.add(new JScrollPane(messageArea));

        panel.add(fieldsPanel, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(this, panel, "Reply", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String subject = subjectField.getText();
            String message = messageArea.getText();
            if (!subject.isEmpty() && !message.isEmpty()) {
                Teacher modelHelper = new Teacher();
                
                // Get the original senderId (the student who sent the email to the teacher)
                EmailDA emailDA = new EmailDA();
                int senderId = emailDA.getSenderIdByEmailId(emailId); // Retrieve the senderId from the email by emailId
                
                // Send email with correct recipientId (the student)
                modelHelper.sendEmail(teacherId, senderId, subject, message, courseCode); // Pass the correct senderId
                JOptionPane.showMessageDialog(this, "Reply sent successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Subject and message cannot be empty.");
            }
        }
    }

    public void displayEnrolledStudents(ArrayList<User> students) {
        centerPanel.removeAll();

        centerPanel.add(btnBack, BorderLayout.NORTH);
        
        studentTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(studentTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        String[] columnNames = { "ID", "First Name", "Last Name", "Email" };
        Object[][] data = new Object[students.size()][4];

        for (int i = 0; i < students.size(); i++) {
            User student = students.get(i);
            data[i][0] = student.getId();
            data[i][1] = student.getfirstName();
            data[i][2] = student.getlastName();
            data[i][3] = student.getEmail();
            
        }
        
        DefaultTableModel model = new DefaultTableModel(data, columnNames);	
        studentTable.setModel(model);

        centerPanel.revalidate();
        centerPanel.repaint();
    }

}
