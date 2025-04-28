package my_util;

public class Validation {
	public static boolean isValidEmail(String email) {
        return email.matches("^[\\w\\-.]+@([\\w\\-]+\\.)+[\\w\\-]{2,4}$");
    }

    public static boolean isValidPhone(String phone) {
        return phone.matches("\\d{3}-\\d{3}-\\d{4}");
    }
}
