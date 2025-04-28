package app;

import model.*;
import view.*;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		
		AuthenticatorView view = new AuthenticatorView();
		view.setVisible(true);
		
		ArrayList<Course> courses = new CourseDA().getCourseList();
		System.out.println(courses);
		
	}
}
