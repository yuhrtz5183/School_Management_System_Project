package model;

import my_util.*;
import java.util.*;
import my_util.Security;

public class UserDA {

	public User authenticateUser(String email, String password) {
	    String query = "SELECT * FROM tb_user WHERE email = ? AND password = ?";
	    String hashedPassword = Security.hashPassword(password);

	    return new Database().executeQuery(query, stmt -> {
	        stmt.setString(1, email);
	        stmt.setString(2, hashedPassword);
	    }, resultSet -> {
	        if (resultSet.next()) {
	            String profilePicture = resultSet.getString("profile_picture");
	            if (profilePicture == null || profilePicture.isEmpty()) {
	                profilePicture = "Downloads/default_profile_pic";
	            }
	            
	            return new User(
	                resultSet.getInt("id"),
	                resultSet.getString("first_name"),
	                resultSet.getString("last_name"),
	                resultSet.getString("password"),
	                resultSet.getString("role_type"),
	                resultSet.getString("email"),
	                profilePicture,
	                resultSet.getBoolean("first_login")
	            );
	        }
	        return null;
	    });
	}
	
	public void setFirstLoginFalse(int userId) {
	    String query = "UPDATE tb_user SET first_login = 0 WHERE id = ?";
	    new Database().executeUpdate(query, stmt -> {
	        stmt.setInt(1, userId);
	    });
	}

	//get users by their role
	public ArrayList<User> getUsersByRole(String role) {
	    String query = "SELECT * FROM tb_user WHERE role_type = ?";
	    
	    return new Database().executeQuery(query, stmt -> {
	        stmt.setString(1, role);
	    }, resultSet -> {
	        ArrayList<User> users = new ArrayList<>();
	        while (resultSet.next()) {
	            boolean firstLogin = resultSet.getBoolean("first_login");
	            users.add(new User(
	                resultSet.getInt("id"),
	                resultSet.getString("first_name"),
	                resultSet.getString("last_name"),
	                resultSet.getString("password"),
	                resultSet.getString("role_type"),
	                resultSet.getString("email"),
	                resultSet.getString("profile_picture"),
	                firstLogin
	            ));
	        }
	        return users;
	    });
	}
	
	public void updatePassword(int userId, String newPassword) {
	    String query = "UPDATE tb_user SET password = ? WHERE id = ?";
	    new Database().executeUpdate(query, stmt -> {
	        stmt.setString(1, newPassword);
	        stmt.setInt(2, userId);
	    });
	}
	
	public User getUserById(int userId) {
	    String query = "SELECT * FROM tb_user WHERE id = ?";

	    return new Database().executeQuery(query, stmt -> {
	        stmt.setInt(1, userId);
	    }, resultSet -> {
	        if (resultSet.next()) {
	            String profilePicture = resultSet.getString("profile_picture");
	            boolean firstLogin = resultSet.getBoolean("first_login");
	            if (profilePicture == null || profilePicture.isEmpty()) {
	                profilePicture = "Downloads/default_profile_pic"; 
	            }
	            
	            return new User(
	                resultSet.getInt("id"),
	                resultSet.getString("first_name"),
	                resultSet.getString("last_name"),
	                resultSet.getString("password"),
	                resultSet.getString("role_type"),
	                resultSet.getString("email"),
	                profilePicture,
	                firstLogin
	            );
	        }
	        return null;
	    });
	}

	public void updateProfilePicture(int userId, String picturePath) {
	    String query = "UPDATE tb_user SET profile_picture = ? WHERE id = ?";
	    new Database().executeUpdate(query, stmt -> {
	        stmt.setString(1, picturePath);
	        stmt.setInt(2, userId);
	    });
	}
	
	public ArrayList<User> getEnrolledStudentsInCourse(int teacherId, String courseCode) {
	    String query = """
	        SELECT u.id, u.first_name, u.last_name, u.email, u.profile_picture, u.first_login
	        FROM tb_user u
	        JOIN tb_enrollment e ON u.id = e.student_id
	        JOIN tb_teacher_courses tc ON e.course_code = tc.course_code
	        WHERE tc.teacher_id = ? AND e.course_code = ?
	    """;

	    return new Database().executeQuery(query, stmt -> {
	        stmt.setInt(1, teacherId);
	        stmt.setString(2, courseCode);
	    }, resultSet -> {
	        ArrayList<User> students = new ArrayList<>();
	        while (resultSet.next()) {
	        	boolean firstLogin = resultSet.getBoolean("first_login");
	            students.add(new User(
	                resultSet.getInt("id"),
	                resultSet.getString("first_name"),
	                resultSet.getString("last_name"),
	                null,
	                "student",
	                resultSet.getString("email"),
	                resultSet.getString("profile_picture"),
	                firstLogin
	            ));
	        }
	        return students;
	    });
	}
	
	public ArrayList<String> teacherCourses(int teacherId) {
		String query = """
	            SELECT tc.course_code 
	            FROM tb_teacher_courses tc
	            WHERE tc.teacher_id = ?
	        """;
        return new Database().executeQuery(query, stmt -> {
            stmt.setInt(1, teacherId);
        }, rs -> {
            ArrayList<String> courses = new ArrayList<>();
            while (rs.next()) {
                //courses.add(rs.getString("name"));
                courses.add(rs.getString("course_code"));
            }
            return courses;
        });
    }
	
	public void addUser(User user) {
		String hashedPassword = Security.hashPassword(user.getPassword());
		
	    String query = "INSERT INTO tb_user (first_name, last_name, email, password, role_type) VALUES (?, ?, ?, ?, ?)";
	    new Database().executeUpdate(query, stmt -> {
	        stmt.setString(1, user.getfirstName());
	        stmt.setString(2, user.getlastName());
	        stmt.setString(3, user.getEmail());
	        stmt.setString(4, hashedPassword);
	        stmt.setString(5, user.getRole());
	    });
	}

	public void updateUser(int id, String first, String last, String email) {
	    String query = "UPDATE tb_user SET first_name = ?, last_name = ?, email = ? WHERE id = ?";
	    new Database().executeUpdate(query, stmt -> {
	        stmt.setString(1, first);
	        stmt.setString(2, last);
	        stmt.setString(3, email);
	        stmt.setInt(4, id);
	    });
	}

	public void deleteUser(int id) {
	    String query = "DELETE FROM tb_user WHERE id = ?";
	    new Database().executeUpdate(query, stmt -> {
	        stmt.setInt(1, id);
	    });
	}

	public boolean isEmailAlreadyUsed(String email) {
	    String query = "SELECT COUNT(*) FROM tb_user WHERE email = ?";

	    return new Database().executeQuery(query, stmt -> {
	        stmt.setString(1, email);
	    }, resultSet -> {
	        if (resultSet.next()) {
	            return resultSet.getInt(1) > 0;
	        }
	        return false;
	    });
	}
	
}
