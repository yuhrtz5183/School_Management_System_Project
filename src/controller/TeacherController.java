package controller;

import model.*;
import view.*;
import java.util.ArrayList;

public class TeacherController {
    private TeacherView view;
    private Teacher model;

    public TeacherController(TeacherView view) {
        this.view = view;
        this.model = new Teacher();
    }

    public void DisplayAssignedCourses(int teacherId) {
        ArrayList<Course> courses = model.getAssignedCoursesForTeacher(teacherId);

        view.displayAssignedCourses(courses);
    }
    
    public void displayEmails(int teacherId) {
        ArrayList<Email> emails = model.getEmailsForTeacher(teacherId);
        view.displayEmails(emails);
    }

    public void displayEnrolledStudents(int teacherId, String courseCode) {
        ArrayList<User> students = model.getStudentsInCourse(teacherId, courseCode);
        view.displayEnrolledStudents(students);
    }
    
    public String getEmailById(int userId) {
        return model.getEmailById(userId);
    }
}
