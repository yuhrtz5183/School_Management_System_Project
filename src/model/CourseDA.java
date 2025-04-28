package model;

import java.util.ArrayList;

import my_util.Database;

public class CourseDA {

    public ArrayList<Course> getCourseList() {
        // return contactModels;

        String query = "SELECT * FROM tb_course";

        return new Database().executeQuery(query,null, results ->{
            ArrayList<Course> courseList = new ArrayList<>();
             // Convert each database row into a Contact object
            while (results.next()) {
                 // Extract data from current row
                String code = results.getString("code");
                String name = results.getString("name");
                String description = results.getString("description");
                int max_capacity = results.getInt("max_capacity");
                String status = results.getString("status");

                
                // Create Contact object and add to list
                Course course = new Course(code, name,description, status, max_capacity);
                courseList.add(course);
        }
        //return the list 
        return courseList;
        });
    }
    
    public ArrayList<Course> getCoursesByTeacher(int teacherId) {
        String query = "SELECT c.code, c.name, c.description, c.status, c.max_capacity " +
                       "FROM tb_course c " +
                       "JOIN tb_teacher_courses tc ON c.code = tc.course_code " +
                       "WHERE tc.teacher_id = ?";

        return new Database().executeQuery(query, stmt -> {
            stmt.setInt(1, teacherId);
        }, results -> {
            ArrayList<Course> courses = new ArrayList<>();
            while (results.next()) {
                String code = results.getString("code");
                String name = results.getString("name");
                String description = results.getString("description");
                String status = results.getString("status");
                int maxCapacity = results.getInt("max_capacity");

                Course course = new Course(code, name, description, status, maxCapacity);
                courses.add(course);
            }
            return courses;
        });
    }
    
    public ArrayList<Course> getStudentCourses(int studentId) {
        String query = """
            SELECT c.code, c.name, c.description, c.status, c.max_capacity
            FROM tb_course c
            INNER JOIN tb_enrollment e ON c.code = e.course_code
            WHERE e.student_id = ?
        """;
        return new Database().executeQuery(query, stmt -> {
            stmt.setInt(1, studentId);
        }, resultSet -> {
            ArrayList<Course> courses = new ArrayList<>();
            while (resultSet.next()) {
                courses.add(new Course(
                    resultSet.getString("code"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("status"),
                    resultSet.getInt("max_capacity")
                ));
            }
            return courses;
        });
    }
    
    public boolean enrollStudent(int studentId, String courseCode) {
        // First check if course is already full
        String capacityQuery = """
            SELECT COUNT(e.student_id) AS enrolled, c.max_capacity, c.status
            FROM tb_course c
            LEFT JOIN tb_enrollment e ON c.code = e.course_code
            WHERE c.code = ?
            GROUP BY c.max_capacity, c.status
        """;

        return new Database().executeQuery(capacityQuery, stmt -> {
            stmt.setString(1, courseCode);
        }, rs -> {
            int enrolled = 0;
            int maxCapacity = 0;
            String status = "";

            if (rs.next()) {
                enrolled = rs.getInt("enrolled");
                maxCapacity = rs.getInt("max_capacity");
                status = rs.getString("status");
            }

            // Debugging info
            System.out.println("Course Code: " + courseCode);
            System.out.println("Enrolled: " + enrolled + ", Max Capacity: " + maxCapacity);
            System.out.println("Status: " + status);

            if (status.equalsIgnoreCase("inactive")) {
                System.out.println("Cannot enroll: Course is inactive.");
                return false; // Course is inactive, cannot enroll
            }

            if (enrolled >= maxCapacity) {
                System.out.println("Cannot enroll: Course is full.");
                return false; // Course is full
            }

            // Now check if already enrolled
            String checkQuery = "SELECT COUNT(*) FROM tb_enrollment WHERE student_id = ? AND course_code = ?";
            boolean alreadyEnrolled = new Database().executeQuery(checkQuery, stmt2 -> {
                stmt2.setInt(1, studentId);
                stmt2.setString(2, courseCode);
            }, rs2 -> {
                if (rs2.next()) {
                    return rs2.getInt(1) > 0;
                }
                return false;
            });

            if (alreadyEnrolled) {
                System.out.println("Cannot enroll: Student is already enrolled in this course.");
                return false; // Already enrolled
            }

            // Insert enrollment
            String insertQuery = "INSERT INTO tb_enrollment (student_id, course_code, enrollment_date) VALUES (?, ?, NOW())";
            new Database().executeUpdate(insertQuery, stmt3 -> {
                stmt3.setInt(1, studentId);
                stmt3.setString(2, courseCode);
            });

            System.out.println("Enrollment successful!");
            return true;
        });
    }

    
    public boolean dropStudent(int studentId, String courseCode) {
        String deleteQuery = "DELETE FROM tb_enrollment WHERE student_id = ? AND course_code = ?";
        
        new Database().executeUpdate(deleteQuery, stmt -> {
            stmt.setInt(1, studentId);
            stmt.setString(2, courseCode);
        });

        return true; // Assume success for now
    }

    
    public ArrayList<Course> getAvailableCourses() {
        String query = """
            SELECT c.code, c.name, c.description, c.status, c.max_capacity, 
                   COUNT(e.student_id) AS enrolled_students
            FROM tb_course c
            LEFT JOIN tb_enrollment e ON c.code = e.course_code
            GROUP BY c.code
            HAVING COUNT(e.student_id) < c.max_capacity
        """;
        return new Database().executeQuery(query, stmt -> {}, resultSet -> {
            ArrayList<Course> courses = new ArrayList<>();
            while (resultSet.next()) {
                courses.add(new Course(
                    resultSet.getString("code"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("status"),
                    resultSet.getInt("max_capacity")
                ));
            }
            return courses;
        });
    }
    
    public void addCourse(Course course) {
    	String query = "INSERT INTO tb_course (code, name, description, max_capacity, status) VALUES (?, ?, ?, ?, ?)";
    	new Database().executeUpdate(query, stmt -> {
    		stmt.setString(1, course.getCode());
    		stmt.setString(2, course.getName());
    		stmt.setString(3, course.getDescription());
    		stmt.setInt(4, course.getMaxCapacity());
    		stmt.setString(5, course.getStatus());
    		
    		System.out.println("Course added: " + course.getCode() + " (" + course.getStatus() + ")");
    	});
    }
    
    public void updateCourse(String code, String name, String description, int maxCapacity, String status) {
    	String query = "UPDATE tb_course SET name = ?, description = ?, max_capacity = ?, status = ? WHERE code = ?";
    	new Database().executeUpdate(query, stmt -> {
    		stmt.setString(1, name);
    		stmt.setString(2, description);
    		stmt.setInt(3, maxCapacity);
    		stmt.setString(4, status);
    		stmt.setString(5, code);
    	});
    }
    
    public void deleteCourse(String code) {
    	String query = "DELETE FROM tb_course WHERE code = ?";
    	new Database().executeUpdate(query, stmt -> {
    		stmt.setString(1, code);
    	});
    }
    
    public boolean isCourseCodeAlreadyUsed(String courseCode) {
        String query = "SELECT COUNT(*) FROM tb_course WHERE code = ?";

        return new Database().executeQuery(query, stmt -> {
            stmt.setString(1, courseCode);
        }, resultSet -> {
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Returns true if course code exists
            }
            return false;
        });
    }

    
    public void assignTeacherToCourse(int teacherId, String courseCode) {
        // Ensure only one teacher per course
        String deleteQuery = "DELETE FROM tb_teacher_courses WHERE course_code = ?";
        String insertQuery = "INSERT INTO tb_teacher_courses (teacher_id, course_code) VALUES (?, ?)";

        new Database().executeUpdate(deleteQuery, stmt -> {
            stmt.setString(1, courseCode);
        });

        new Database().executeUpdate(insertQuery, stmt -> {
            stmt.setInt(1, teacherId);
            stmt.setString(2, courseCode);
        });
    }
    
    public int getAssignedTeacherId(String courseCode) {
        String query = "SELECT teacher_id FROM tb_teacher_courses WHERE course_code = ?";
        return new Database().executeQuery(query, stmt -> {
            stmt.setString(1, courseCode);
        }, results -> {
            if (results.next()) {
                return results.getInt("teacher_id");
            }
            return -1; // No teacher assigned
        });
    }

}