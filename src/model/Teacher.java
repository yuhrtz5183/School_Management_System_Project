package model;

import java.util.ArrayList;

public class Teacher {
    private CourseDA courseDA;
    private UserDA userDA;
    private EmailDA emailDA;

    public Teacher() {
        courseDA = new CourseDA();
        userDA = new UserDA();
        emailDA = new EmailDA();
    }

    // Get the courses assigned to a teacher by teacher ID
    public ArrayList<Course> getAssignedCoursesForTeacher(int teacherId) {
        return courseDA.getCoursesByTeacher(teacherId); // Notice we use a correct method now
    }

    public ArrayList<Email> getEmailsForTeacher(int teacherId) {
        return emailDA.getEmailsByRecipientId(teacherId);
    }
    
    public ArrayList<User> getStudentsInCourse(int teacherId, String courseCode) {
        return userDA.getEnrolledStudentsInCourse(teacherId, courseCode);  // Call the method on the instance
    }
    
    public void updateEmailStatus(int emailId, String newStatus) {
        emailDA.updateEmailStatus(emailId, newStatus);
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

    public String getEmailById(int userId) {
        // Log the userId to make sure the function is working correctly
        System.out.println("Fetching email for user ID: " + userId);
        User user = userDA.getUserById(userId);
        return user != null ? user.getEmail() : null;
    }

}
