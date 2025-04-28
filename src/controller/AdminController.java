package controller;

import model.*;
import javax.swing.table.*;
import java.util.*;

public class AdminController {
	private UserDA model;
	private CourseDA courseModel;
	
    public AdminController() {
        model = new UserDA();
        courseModel = new CourseDA();
    }
    
    public DefaultTableModel getUserTableModel(String role) {
        ArrayList<User> users = model.getUsersByRole(role);

        String[] columnNames;
        if (role.equals("teacher")) {
            columnNames = new String[]{"ID", "First Name", "Last Name", "Email", "Initial Password", "Courses Teaching"};
        }
        else {
            columnNames = new String[]{"ID", "First Name", "Last Name", "Email", "Initial Password"};
        }

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (User u : users) {
            if (role.equals("teacher")) {
                ArrayList<String> courses = model.teacherCourses(u.getId());
                String courseList = courses.isEmpty() ? "None" : String.join(", ", courses);

                tableModel.addRow(new Object[]{
                    u.getId(), u.getfirstName(), u.getlastName(), u.getEmail(), u.getPassword(), courseList
                });
            }
            else {
                tableModel.addRow(new Object[]{
                    u.getId(), u.getfirstName(), u.getlastName(), u.getEmail(), u.getPassword()
                });
            }
        }
        return tableModel;
    }
    
    //for the update course
    public ArrayList<User> getTeachers() {
        return model.getUsersByRole("teacher");
    }
    
    public void addUser(User u) {
        model.addUser(u);
    }

    public void updateUser(int id, String first, String last, String email) {
        model.updateUser(id, first, last, email);
    }

    public void deleteUser(int id) {
        model.deleteUser(id);
    }
    
    public void addCourse(Course c) {
    	courseModel.addCourse(c);
    }
    
    public void updateCourse(String code, String name, String description, int maxCapacity, String status) {
    	courseModel.updateCourse(code, name, description, maxCapacity, status);
    }
    
    public void assignTeacherToCourse(int teacherId, String courseCode) {
        courseModel.assignTeacherToCourse(teacherId, courseCode);
    }
    
    public int getAssignedTeacherIdForCourse(String courseCode) {
        return courseModel.getAssignedTeacherId(courseCode);
    }
    
    public void deleteCourse(String code) {
    	courseModel.deleteCourse(code);
    }
    
    public boolean isEmailAlreadyUsed(String email) {
        return model.isEmailAlreadyUsed(email);
    }

    public boolean isCourseCodeAlreadyUsed(String courseCode) {
        return courseModel.isCourseCodeAlreadyUsed(courseCode);
    }
    
}


