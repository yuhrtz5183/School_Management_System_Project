package my_util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Security {
	
	
	public static String hashPassword(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hashedBytes = md.digest(password.getBytes());
			StringBuilder stringBuilder = new StringBuilder();
			for (byte b : hashedBytes) {
				stringBuilder.append(String.format("%02x", b));
			}
			return stringBuilder.toString();
		} 
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
//	public static String generateSalt() {
//        // Simple salt generation (you can improve this with better random generation)
//        return Long.toHexString(Double.doubleToLongBits(Math.random()));
//    }
//
//    // Method to hash the password with the salt
//    public static String hashPassword(String password, String salt) {
//        try {
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            md.update(salt.getBytes());  // Update the hash with the salt
//            byte[] hashedBytes = md.digest(password.getBytes());  // Hash the password
//            return bytesToHex(hashedBytes);  // Return the hashed password as a hex string
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    // Method to convert bytes to hex (for password hash output)
//    private static String bytesToHex(byte[] bytes) {
//        StringBuilder hexString = new StringBuilder();
//        for (byte b : bytes) {
//            hexString.append(String.format("%02x", b));
//        }
//        return hexString.toString();
//    }
	
	
	
}
