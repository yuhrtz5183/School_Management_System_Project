package view;

import model.*;
import my_util.Validation;
import controller.*;
import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;
import java.util.*;

public class AdminView extends JFrame {
    private JTable teacherTable, studentTable, courseTable;
    private DefaultTableModel courseModel;
    private JButton btnProfile, btnTeachers, btnCourses, btnStudents;
    private JPanel centerPanel;
    private User currentUser;
    private AdminController controller;
   
    
    public void initialize(User user) {
    	
    	setTitle("Admin Dashboard");
    	setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    	setSize(1200, 700);
    	setLocationRelativeTo(null);
    	
    	this.currentUser = user;
    	
    	loadButtons();
    	
    	centerPanel = new JPanel(new BorderLayout());
        add(centerPanel, BorderLayout.CENTER);
    
        loadProfilePanel(currentUser);
        controller = new AdminController();

        setVisible(true);
    }
    
    //creating the side panel of buttons
    private void loadButtons() {
    	JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
    	add(buttonPanel, BorderLayout.WEST);
    	
    	btnProfile = new JButton("Profile");
    	btnProfile.addActionListener(e -> loadProfilePanel(currentUser));
    	buttonPanel.add(btnProfile);
    	
    	btnCourses = new JButton("Courses");
    	btnCourses.addActionListener(e -> courseTable());
    	buttonPanel.add(btnCourses);
    	
    	btnTeachers = new JButton("Teachers");
    	btnTeachers.addActionListener(e -> userTable("teacher"));
    	buttonPanel.add(btnTeachers);
    	
    	btnStudents = new JButton("Students");
    	btnStudents.addActionListener(e -> userTable("student"));
    	buttonPanel.add(btnStudents);
    }
    
    private void loadProfilePanel(User user) {
    	centerPanel.removeAll();
        centerPanel.add(CommonView.profilePanel(user), BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();
    }
    
    private void courseTable() {
    	centerPanel.removeAll();
    	
    	CourseDA courseDA = new CourseDA();
        ArrayList<Course> courses = courseDA.getCourseList();

        //column name of table
        String[] columnNames = {"Code", "Name", "Description","Status", "Max Capacity"};

        courseModel = new DefaultTableModel(columnNames, 0);
        courseTable = new JTable(courseModel);

        for (Course c : courses) {
            courseModel.addRow(new Object[]{
                c.getCode(), c.getName(), c.getDescription(), c.getStatus(), c.getMaxCapacity()
            });
        }
        
        //makes it so you can not rearrange the columns of the table
        courseTable.getTableHeader().setReorderingAllowed(false);

        //header creation
        JLabel header = new JLabel("Courses Table", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        
        //button panel for adding, editing, and deleting courses
        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Add Course");
        JButton btnUpdate = new JButton("Edit Course");
        JButton btnDelete = new JButton("Delete Course");
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        
        centerPanel.add(header, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(courseTable), BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        centerPanel.revalidate();
        centerPanel.repaint();
        
        btnAdd.addActionListener(e -> addCourse());
        btnUpdate.addActionListener(e -> updateCourse());
        btnDelete.addActionListener(e -> deleteCourse());
    }
    
    //add courses
    private void addCourse() {
        JTextField courseCodeField = new JTextField();
        JTextField courseName = new JTextField();
        JTextField courseDescription = new JTextField();
        JTextField courseStatus = new JTextField();
        JTextField courseMaxCapacity = new JTextField();
        
        //warning label for invalid input
        JLabel warningLabel = createWarningLabel("All fields must be filled out!");
        
        //drop down box for a list of teachers
        JComboBox<String> teacherComboBox = new JComboBox<>();
        ArrayList<User> teachers = controller.getTeachers();

        //add teachers to the drop down box
        for (User teacher : teachers) {
            String fullName = teacher.getfirstName() + " " + teacher.getlastName();
            teacherComboBox.addItem(fullName);
        }
        
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Course Code: "));
        panel.add(courseCodeField);
        panel.add(new JLabel("Course Name: "));
        panel.add(courseName);
        panel.add(new JLabel("Course Description: "));
        panel.add(courseDescription);
        panel.add(new JLabel("Course Status: "));
        panel.add(courseStatus);
        panel.add(new JLabel("Course Max Capacity: "));
        panel.add(courseMaxCapacity);
        panel.add(new JLabel("Assign Teacher: "));
        panel.add(teacherComboBox);
        panel.add(warningLabel);
        
        boolean validInput = false;

        while (!validInput) {
            int result = JOptionPane.showConfirmDialog(this, panel, "Add New Course", JOptionPane.OK_CANCEL_OPTION);
        
            if (result == JOptionPane.OK_OPTION) {
                String code = courseCodeField.getText().trim();
                String name = courseName.getText().trim();
                String description = courseDescription.getText().trim();
                String status = courseStatus.getText().trim();
                String maxCapacityTxt = courseMaxCapacity.getText().trim();

                if (code.isEmpty() || name.isEmpty() || description.isEmpty() || status.isEmpty() || maxCapacityTxt.isEmpty()) {
                	//set warning label to true so that it displays to user
                    warningLabel.setVisible(true);
                    continue;
                }
                try {
                    int maxCapacity = Integer.parseInt(maxCapacityTxt);
                    
                    if (controller.isCourseCodeAlreadyUsed(code)) {
                        warningLabel.setText("Course code already exists. Please enter a different course code.");
                        warningLabel.setVisible(true);
                        continue;
                    }
                    int selectedTeacherIndex = teacherComboBox.getSelectedIndex();
                    User selectedTeacher = teachers.get(selectedTeacherIndex);
                    int teacherId = selectedTeacher.getId();
 
                    Course newCourse = new Course(code, name, description, status, maxCapacity);
                    controller.addCourse(newCourse);
                    controller.assignTeacherToCourse(teacherId, code);
                    
                    courseTable();
                    validInput = true;

                }
                catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Max capacity must be a valid integer.");
                }
            } 
            else {
                break;
            }
        }
    }
    
    //update courses
    private void updateCourse() {
    	int selectedRow = courseTable.getSelectedRow();
    	if (selectedRow == -1) {
    		JOptionPane.showMessageDialog(this, "Please select a course to edit.");
    		return;
    	}
    	
    	String courseCode = (String) courseModel.getValueAt(selectedRow, 0);
    	String courseName = (String) courseModel.getValueAt(selectedRow, 1);
    	String courseDescription = (String) courseModel.getValueAt(selectedRow, 2);
    	String courseStatus = (String) courseModel.getValueAt(selectedRow, 3);
    	String courseMaxCapacityString = courseModel.getValueAt(selectedRow, 4).toString(); // This should be the max capacity column
        int courseMaxCapacity = 0;
        try {
            courseMaxCapacity = Integer.parseInt(courseMaxCapacityString);  // Parse the string into an integer
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Max Capacity must be a valid number.");
            return;
        }
        
        int currentTeacherId = controller.getAssignedTeacherIdForCourse(courseCode);
        
    	JTextField courseCodeField = new JTextField(courseCode);
    	courseCodeField.setEditable(false);
    	JTextField courseNameField = new JTextField(courseName);
    	JTextField courseDescriptionField = new JTextField(courseDescription);
    	JTextField courseMaxCapacityField = new JTextField(String.valueOf(courseMaxCapacity));
    	JTextField courseStatusField = new JTextField(courseStatus);
    	
    	JComboBox<String> teacherComboBox = new JComboBox<>();
        ArrayList<User> teachers = controller.getTeachers();
    	
        int selectedIndex = -1;
        for (int i = 0; i < teachers.size(); i++) {
            User teacher = teachers.get(i);
            String fullName = teacher.getfirstName() + " " + teacher.getlastName();
            teacherComboBox.addItem(fullName);
            if (teacher.getId() == currentTeacherId) {
                selectedIndex = i;
            }
        }
        if (selectedIndex != -1) {
            teacherComboBox.setSelectedIndex(selectedIndex);
        }

    	JPanel panel = new JPanel(new GridLayout(0, 1));
    	panel.add(new JLabel("Course Code: "));
    	panel.add(courseCodeField);
    	panel.add(new JLabel("Course Name: "));
    	panel.add(courseNameField);
    	panel.add(new JLabel("Course Description: "));
    	panel.add(courseDescriptionField);
    	panel.add(new JLabel("Course Status: "));
    	panel.add(courseStatusField);
    	panel.add(new JLabel("Course Max Capacity: "));
    	panel.add(courseMaxCapacityField);
    	panel.add(new JLabel("Assign Teacher: "));
        panel.add(teacherComboBox);
    	
    	int result = JOptionPane.showConfirmDialog(this, panel, "Edit Course", JOptionPane.OK_CANCEL_OPTION);
    	if (result == JOptionPane.OK_OPTION) {
    		String code = courseCodeField.getText();
    		String name = courseNameField.getText();
    		String description = courseDescriptionField.getText();
    		int maxCapacity = Integer.parseInt(courseMaxCapacityField.getText());
    		String status = courseStatusField.getText();
    		int teacherId = teachers.get(teacherComboBox.getSelectedIndex()).getId();
    		
    		controller.updateCourse(code, name, description, maxCapacity, status);
            controller.assignTeacherToCourse(teacherId, code);
            
            courseTable();
    	}
    }
    
    //delete courses
    private void deleteCourse() {
    	int selectedRow = courseTable.getSelectedRow();
    	if (selectedRow == -1) {
    		JOptionPane.showMessageDialog(this, "Please select a course to delete.");
    		return;
    	}
    	
    	String courseCode = (String) courseModel.getValueAt(selectedRow, 0);
    	int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
    	
    	if (confirm == JOptionPane.YES_OPTION) {
    		controller.deleteCourse(courseCode);
    		courseTable();
    	}
    }
    
    //display user table method **Teachers and Students**
    private void userTable(String role) {
        centerPanel.removeAll();

        DefaultTableModel userModel = controller.getUserTableModel(role);
        JTable userTable = new JTable(userModel);
        
        //makes it so you can not rearrange the columns of the table
        userTable.getTableHeader().setReorderingAllowed(false);
        
        if (role.equals("teacher")) {
            teacherTable = userTable;
        } else if (role.equals("student")) {
            studentTable = userTable;
        }

        //header for user table for either Teacher or Student
        JLabel header = new JLabel(role.substring(0, 1).toUpperCase() + role.substring(1) + " Table", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(header, BorderLayout.CENTER);

        //button panel for add, edit, and delete
        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Add");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(userTable), BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();

        btnAdd.addActionListener(e -> addUser(role));
        btnEdit.addActionListener(e -> updateUser(userTable, role));
        btnDelete.addActionListener(e -> deleteUser(userTable, role));
    }

    //add user method
    private void addUser(String role) {
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField emailField = new JTextField();
        
        //warning label is hidden for now
        JLabel warningLabel = createWarningLabel("All fields must be filled out");
        
        //panel for
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);
        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(warningLabel);
        
        boolean validInput = false;

        while (!validInput) {
        	int result = JOptionPane.showConfirmDialog(this, panel, "Add New " + role.substring(0, 1).toUpperCase() + role.substring(1), JOptionPane.OK_CANCEL_OPTION);
        	
        	if (result == JOptionPane.OK_OPTION) {
        		String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String email = emailField.getText().trim();
                String password = "123";

                //validation for firstName, lastName, email
                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
                    warningLabel.setVisible(true);
                    continue;
                }
                
                //validation to see if email is in correct format
                if (!Validation.isValidEmail(email)) {
                    warningLabel.setText("Invalid email format. Please enter a valid email address.");
                    warningLabel.setVisible(true);
                    continue;
                }
                
                //validation to see if email is already used
                if (controller.isEmailAlreadyUsed(email)) {
                    warningLabel.setText("Email is already in use. Please use a different email.");
                    warningLabel.setVisible(true);
                    continue;
                }
                
                User newUser = new User(0, firstName, lastName, password, role, email, "Downloads/default_profile_pic", true);
                controller.addUser(newUser);
                refreshTable(role);
                validInput = true;
            } 
        	else {
                break;
            }   
        }
    }
    
    //update user
    private void updateUser(JTable userTable, String role) {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a " + role + " to edit.");
            return;
        }

        int userId = (int) userTable.getModel().getValueAt(selectedRow, 0);
        String firstName = (String) userTable.getModel().getValueAt(selectedRow, 1);
        String lastName = (String) userTable.getModel().getValueAt(selectedRow, 2);
        String email = (String) userTable.getModel().getValueAt(selectedRow, 3);

        JTextField firstNameField = new JTextField(firstName);
        JTextField lastNameField = new JTextField(lastName);
        JTextField emailField = new JTextField(email);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);
        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit " + role, JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            controller.updateUser(userId, firstNameField.getText(), lastNameField.getText(), emailField.getText());
            refreshTable(role);
        }
    }

    //delete user
    private void deleteUser(JTable userTable, String role) {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a " + role + " to delete.");
            return;
        }

        int userId = (int) userTable.getModel().getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this " + role + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            controller.deleteUser(userId);
            refreshTable(role);
        }
    }

    //get updated view of table
    private void refreshTable(String role) {
        DefaultTableModel updatedModel = controller.getUserTableModel(role);
        if (role.equals("teacher")) {
            teacherTable.setModel(updatedModel);
        } else {
            studentTable.setModel(updatedModel);
        }
    }
    
    //invalid input warning label
    private JLabel createWarningLabel(String message) {
        JLabel label = new JLabel(message);
        label.setForeground(Color.RED);
        label.setFont(new Font("Arial", Font.PLAIN, 10));
        label.setVisible(false);
        return label;
    }
    
    
}
