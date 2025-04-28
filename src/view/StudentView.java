package view;

import model.Course;
import model.Email;
import model.EmailDA;
import model.Student;
import model.Teacher;
import model.User;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controller.StudentController;

public class StudentView extends JFrame {

    private JButton btnProfile, btnMyCourses, btnAvailableCourses, btnEnroll, btnDrop;
    private JPanel centerPanel;
    private JTable courseTable;
    private StudentController controller;
    private User currentUser;
    private int studentId;

    public void initialize(User user) {
        setTitle("Student Dashboard");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        this.currentUser = user;
        this.studentId = user.getId();
        this.controller = new StudentController(this);

        loadButtons();

        centerPanel = new JPanel(new BorderLayout());
        add(centerPanel, BorderLayout.CENTER);
        
        setVisible(true);
        
        //always loads profile panel first
        loadProfilePanel(currentUser);
    }

    //load the buttons into the panel
    private void loadButtons() {
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
        add(buttonPanel, BorderLayout.WEST);

        //profile button
        btnProfile = new JButton("Profile");
        btnProfile.addActionListener(e -> loadProfilePanel(currentUser));
        buttonPanel.add(btnProfile);

        //my courses button
        btnMyCourses = new JButton("My Courses");
        btnMyCourses.addActionListener(e -> controller.displayMyCourses());
        buttonPanel.add(btnMyCourses);

        //available courses button
        btnAvailableCourses = new JButton("Available Courses");
        btnAvailableCourses.addActionListener(e -> controller.displayAvailableCourses());
        buttonPanel.add(btnAvailableCourses);
        
        //email button
        JButton btnEmail = new JButton("Email");
        btnEmail.addActionListener(e -> controller.displayEmails(studentId));
        buttonPanel.add(btnEmail);
    }

    //profile panel
    private void loadProfilePanel(User user) {
        centerPanel.removeAll();
        centerPanel.add(CommonView.profilePanel(user), BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    //display enrolled course
    public void displayMyCourses(ArrayList<Course> courses) {
        centerPanel.removeAll();
        centerPanel.setLayout(new BorderLayout());

        //available course table creation
        courseTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(courseTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        String[] columnNames = {"Code", "Name", "Description", "Status", "Max Capacity"};
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

        JLabel header = new JLabel("My Courses", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 16));

        btnDrop = new JButton("Drop Course");
        btnDrop.addActionListener(e -> dropSelectedCourse());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnDrop);

        centerPanel.add(header, BorderLayout.NORTH);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        centerPanel.revalidate();
        centerPanel.repaint();
    }

    //display available courses
    public void displayAvailableCourses(ArrayList<Course> courses) {
        centerPanel.removeAll();
        centerPanel.setLayout(new BorderLayout());

        //available course table creation
        courseTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(courseTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        String[] columnNames = {"Code", "Name", "Description", "Status", "Max Capacity"};
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

        JLabel header = new JLabel("All Courses", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 16));

        btnEnroll = new JButton("Enroll");
        btnEnroll.addActionListener(e -> enrollSelectedCourse());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnEnroll);

        centerPanel.add(header, BorderLayout.NORTH);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        centerPanel.revalidate();
        centerPanel.repaint();
    }

    //enroll in a course
    private void enrollSelectedCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to enroll.");
            return;
        }
        
        String courseCode = courseTable.getValueAt(selectedRow, 0).toString();
        controller.enrollInCourse(studentId, courseCode);
    }

    //drop a course
    private void dropSelectedCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to drop.");
            return;
        }

        String courseCode = courseTable.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to drop this course?", "Confirm Drop", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            controller.dropCourse(studentId, courseCode);
        }
    }
    
    public void displayEmails(ArrayList<Email> emails) {
        centerPanel.removeAll();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(500);
        centerPanel.add(splitPane, BorderLayout.CENTER);

        //left panel: inbox
        String[] columnNames = { "Subject", "Timestamp", "Status" };
        Object[][] data = new Object[emails.size()][3];

        for (int i = 0; i < emails.size(); i++) {
            Email e = emails.get(i);
            data[i][0] = e.getSubject();
            data[i][1] = e.getTimestamp();
            data[i][2] = e.getStatus();
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable emailTable = new JTable(model);
        JScrollPane tableScroll = new JScrollPane(emailTable);
        splitPane.setLeftComponent(tableScroll);

        //right panel: selected email details
        JPanel detailPanel = new JPanel(new BorderLayout());
        detailPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //reply button
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton btnReply = new JButton("Reply");
        btnReply.setVisible(false);
        topPanel.add(btnReply, BorderLayout.EAST);
        detailPanel.add(topPanel, BorderLayout.NORTH);

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
        
        JButton btnCreateMessage = new JButton("New Message");
        btnCreateMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCreateMessageWindow();
            }
        });
        
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(btnCreateMessage, BorderLayout.NORTH);
        leftPanel.add(tableScroll, BorderLayout.CENTER);
        splitPane.setLeftComponent(leftPanel);

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

                    //show reply button
                    btnReply.setVisible(true);

                    //mark email as read
                    if (!selectedEmail.getStatus().equals("read")) {
                        model.setValueAt("read", selectedRow, 2);
                        selectedEmail.setStatus("read");

                        Teacher modelHelper = new Teacher();
                        modelHelper.updateEmailStatus(selectedEmail.getId(), "read");
                    }

                    for (ActionListener listener : btnReply.getActionListeners()) {
                        btnReply.removeActionListener(listener);
                    }

                    btnReply.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                        	openReplyWindow(selectedEmail.getId(), selectedEmail.getSender(), selectedEmail.getClassCode());
                            
                        }
                    });
                }
            }
        });
        
        centerPanel.revalidate();
        centerPanel.repaint();
    }
    
    private void openCreateMessageWindow() {
        JDialog createDialog = new JDialog(this, "New Message", true);
        createDialog.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        inputPanel.setBorder(new EmptyBorder(10, 10, 0, 10));

        JLabel recipientLabel = new JLabel("Recipient (Teacher Email):");
        JTextField recipientField = new JTextField();
        JLabel subjectLabel = new JLabel("Subject:");
        JTextField subjectField = new JTextField();
        JLabel classCodeLabel = new JLabel("Class Code:");
        JTextField classCodeField = new JTextField();
        JLabel messageLabel = new JLabel("Message:");
        JTextArea messageArea = new JTextArea(3, 15);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane messageScroll = new JScrollPane(messageArea);

        inputPanel.add(recipientLabel);
        inputPanel.add(recipientField);
        inputPanel.add(subjectLabel);
        inputPanel.add(subjectField);
        inputPanel.add(classCodeLabel);
        inputPanel.add(classCodeField);
        inputPanel.add(messageLabel);
        inputPanel.add(messageScroll);

        // Error message label
        JLabel errorMessageLabel = new JLabel("");
        errorMessageLabel.setForeground(Color.RED);
        errorMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.add(inputPanel);
        formPanel.add(errorMessageLabel); // Add error message label to form panel

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String teacherEmail = recipientField.getText().trim();
                String subject = subjectField.getText();
                String message = messageArea.getText();
                String courseCode = classCodeField.getText();

                // Validate email and course code inputs
                if (teacherEmail.isEmpty() || courseCode.isEmpty()) {
                    errorMessageLabel.setText("Teacher email and Course code cannot be empty.");
                    return;
                }

                // Proceed with creating the message
                errorMessageLabel.setText(""); // Clear any previous error message
                StudentController controller = new StudentController(StudentView.this);
                controller.createMessage(studentId, teacherEmail, subject, message, courseCode);
                
                createDialog.dispose();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createDialog.dispose();
            }
        });

        buttonPanel.add(sendButton);
        buttonPanel.add(cancelButton);

        createDialog.add(inputPanel, BorderLayout.NORTH);
        createDialog.add(buttonPanel, BorderLayout.SOUTH);

        createDialog.pack();
        createDialog.setLocationRelativeTo(this);
        createDialog.setVisible(true);
    }

    private void openReplyWindow(int emailId, String senderName, String courseCode) {
        JTextField subjectField = new JTextField();
        JTextArea messageArea = new JTextArea(10, 30);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(new JLabel("To: " + senderName), BorderLayout.NORTH);	

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
                Student modelHelper = new Student();
                
                EmailDA emailDA = new EmailDA();
                int senderId = emailDA.getSenderIdByEmailId(emailId);
                
                modelHelper.sendEmail(studentId, senderId, subject, message, courseCode);
                JOptionPane.showMessageDialog(this, "Reply sent successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Subject and message cannot be empty.");
            }
        }
    }

    public int getStudentId() {
        return studentId;
    }
}
