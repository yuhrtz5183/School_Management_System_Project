package model;

import java.sql.*;
import java.util.ArrayList;
import my_util.Database;

public class EmailDA {
    
    public ArrayList<Email> getEmailsByRecipientId(int recipientId) {
        ArrayList<Email> emails = new ArrayList<>();
        try {
            Connection conn = Database.getConnection();
            String sql = "SELECT m.id, m.subject, m.message, m.timestamp, m.status, " +
                    "m.sender_id, CONCAT(u.first_name, ' ', u.last_name) AS sender_name, m.recipient_id, m.code_code " +
                    "FROM tb_message m " +
                    "JOIN tb_user u ON m.sender_id = u.id " +
                    "WHERE m.recipient_id = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, recipientId);  
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Email email = new Email();
                email.setId(rs.getInt("id"));
                email.setSubject(rs.getString("subject"));
                email.setMessage(rs.getString("message"));
                email.setTimestamp(rs.getString("timestamp"));
                email.setStatus(rs.getString("status"));
                email.setSender(rs.getString("sender_name"));
                email.setRecipientId(rs.getInt("recipient_id"));
                email.setClassCode(rs.getString("code_code"));
                emails.add(email);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emails;
    }

    public void updateEmailStatus(int emailId, String newStatus) {
        try {
            Connection conn = Database.getConnection();
            String query = "UPDATE tb_message SET status = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, newStatus);
            stmt.setInt(2, emailId);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int getTeacherIdByEmail(String email) {
        int teacherId = 0;
        try (Connection conn = Database.getConnection()) {
            String query = "SELECT id FROM tb_user WHERE email = ? AND role_type = 'teacher'";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                teacherId = rs.getInt("id");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teacherId;
    }
    
//    public void insertEmail(int senderId, int recipientId, String subject, String message, String courseCode) {
//        try (Connection conn = Database.getConnection()) {
//
//            // Check if the recipient exists in tb_user
//            String checkUserQuery = "SELECT id FROM tb_user WHERE id = ?";
//            try (PreparedStatement checkStmt = conn.prepareStatement(checkUserQuery)) {
//                checkStmt.setInt(1, recipientId);
//                try (ResultSet rs = checkStmt.executeQuery()) {
//                    if (!rs.next()) {
//                        System.out.println("Error: Recipient ID does not exist in the tb_user table.");
//                        return;
//                    }
//                }
//            }
//
//            //check to see if course is then the course table
//            String checkCourseQuery = "SELECT code FROM tb_course WHERE code = ?";
//            try (PreparedStatement checkCourseStmt = conn.prepareStatement(checkCourseQuery)) {
//                checkCourseStmt.setString(1, courseCode);
//                try (ResultSet rsCourse = checkCourseStmt.executeQuery()) {
//                    if (!rsCourse.next()) {
//                        System.out.println("Error: Course code does not exist in tb_course.");
//                        return;
//                    }
//                }
//            }
//
//            //insert email to tb_message
//            String emailInsertQuery = "INSERT INTO tb_message (sender_id, recipient_id, subject, message, status, timestamp, code_code) " +
//                         "VALUES (?, ?, ?, ?, 'unread', NOW(), ?)";
//            try (PreparedStatement stmt = conn.prepareStatement(emailInsertQuery)) {
//                stmt.setInt(1, senderId);
//                stmt.setInt(2, recipientId);
//                stmt.setString(3, subject);
//                stmt.setString(4, message);
//                stmt.setString(5, courseCode);
//
//                int rowsInserted = stmt.executeUpdate();
//                System.out.println("Rows inserted: " + rowsInserted);  
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
    
    public void insertEmail(int senderId, int recipientId, String subject, String message, String courseCode) {
        try (Connection conn = Database.getConnection()) {

            // Validate recipient exists
            String checkUserQuery = "SELECT id FROM tb_user WHERE id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkUserQuery)) {
                checkStmt.setInt(1, recipientId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        return;
                    }
                }
            }

            // Validate course exists
            String checkCourseQuery = "SELECT code FROM tb_course WHERE code = ?";
            try (PreparedStatement checkCourseStmt = conn.prepareStatement(checkCourseQuery)) {
                checkCourseStmt.setString(1, courseCode);
                try (ResultSet rsCourse = checkCourseStmt.executeQuery()) {
                    if (!rsCourse.next()) {
                        return;
                    }
                }
            }

            // Insert email into tb_message
            String emailInsertQuery = "INSERT INTO tb_message (sender_id, recipient_id, subject, message, status, timestamp, code_code) " +
                    "VALUES (?, ?, ?, ?, 'unread', NOW(), ?)";
            try (PreparedStatement stmt = conn.prepareStatement(emailInsertQuery)) {
                stmt.setInt(1, senderId);
                stmt.setInt(2, recipientId);
                stmt.setString(3, subject);
                stmt.setString(4, message);
                stmt.setString(5, courseCode);

                int rowsInserted = stmt.executeUpdate();
                System.out.println("Rows inserted: " + rowsInserted);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int getSenderIdByEmailId(int emailId) {
        int senderId = 0;
        try {
            Connection conn = Database.getConnection();
            String query = "SELECT sender_id FROM tb_message WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, emailId);  
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                senderId = rs.getInt("sender_id");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return senderId;
    }
    

}
