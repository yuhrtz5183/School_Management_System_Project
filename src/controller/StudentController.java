package controller;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.*;
import view.*;

public class StudentController {
	private StudentView view;
	private Student model;
	
	public StudentController(StudentView view) {
        this.view = view;
        this.model = new Student();
    }
	
	public void displayMyCourses() {
	    ArrayList<Course> courses = model.getMyCourses(view.getStudentId());
	    view.displayMyCourses(courses);
	}
	
	public void enrollInCourse(int studentId, String courseCode) {
	    boolean success = model.enrollStudentInCourse(studentId, courseCode);
	    if (success) {
	        JOptionPane.showMessageDialog(view, "Enrollment successful!");
	        displayMyCourses(); // Refresh the list after enrolling
	    } 
	    else {
	    	System.out.println("No worky");
	        JOptionPane.showMessageDialog(view, "Enrollment failed. Course may be full or already enrolled.");
	    }
	}
	
	public void dropCourse(int studentId, String courseCode) {
	    boolean success = model.dropStudentFromCourse(studentId, courseCode);
	    if (success) {
	        JOptionPane.showMessageDialog(view, "Course dropped successfully.");
	        displayMyCourses(); // Refresh list
	    } else {
	        JOptionPane.showMessageDialog(view, "Failed to drop course. Try again.");
	    }
	}
	
    public void displayAvailableCourses() {
        ArrayList<Course> courses = model.getAvailableCourses();
        view.displayAvailableCourses(courses);
    }
    
    public void displayEmails(int studentId) {
        ArrayList<Email> emails = model.getEmailsForStudent(studentId);
        view.displayEmails(emails);
    }
    
    public void createMessage(int studentId, String teacherEmail, String subject, String message, String courseCode) {
    	
        if (subject.isEmpty() || message.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Subject and Message cannot be empty.");
            return;
        }
        // Get the teacherId from the teacher's email
        int teacherId = model.getTeacherIdByEmail(teacherEmail);
        if (teacherId == 0) {
            JOptionPane.showMessageDialog(view, "Invalid Teacher Email.");
            return;
        }
        if (courseCode.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Invalid Course Code.");
            return;
        }
        
        model.sendEmail(studentId, teacherId, subject, message, courseCode);
        JOptionPane.showMessageDialog(view, "Message sent successfully!");
    }
    
    
}
