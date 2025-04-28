package model;

import java.util.ArrayList;

public class Student {
	private CourseDA courseDA;
	private EmailDA emailDA;
    
    public Student() {
        courseDA = new CourseDA();
        emailDA = new EmailDA();
    }
	
    public ArrayList<Course> getMyCourses(int studentId) {
        return courseDA.getStudentCourses(studentId);
    }
	
    public boolean enrollStudentInCourse(int studentId, String courseCode) {
        return courseDA.enrollStudent(studentId, courseCode);
    }
    
    public boolean dropStudentFromCourse(int studentId, String courseCode) {
        return courseDA.dropStudent(studentId, courseCode);
    }
    
    public ArrayList<Course> getAvailableCourses() {
        return courseDA.getAvailableCourses();
    }
    
    public ArrayList<Email> getEmailsForStudent(int studentId) {
        return emailDA.getEmailsByRecipientId(studentId);
    }
    
    public int getTeacherIdByEmail(String teacherEmail) {
        return emailDA.getTeacherIdByEmail(teacherEmail);
    }
    
    public void sendEmail(int senderId, int recipientId, String subject, String message, String courseCode) {
        // Log the email details before sending
        System.out.println("Sending email from sender ID: " + senderId + ", recipient ID: " + recipientId + 
                           ", Subject: " + subject + ", Message: " + message + ", CourseCode: " + courseCode);
        // Ensure recipientId is set correctly (should not be 0)
        if (recipientId == 0) {
            System.out.println("Error: Invalid recipient ID.");
            return;  // Avoid sending email with invalid recipientId
        }
        emailDA.insertEmail(senderId, recipientId, subject, message, courseCode);
    }
    
}

