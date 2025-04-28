package model;

public class User {
	public int id;
    public String firstName;
    public String lastName;
    public String password;
    public String role;
    public String email;
    public String profilePicturePath;
    private boolean firstLogin;
    
    public User(int id, String firstName, String lastName, String password, String role, String email, String profilePicturePath, boolean firstLogin) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.role = role;
        this.email = email;
        this.profilePicturePath = profilePicturePath != null ? profilePicturePath : "Downloads/default_profile_pic";
        this.firstLogin = firstLogin;
    }
    
    public boolean isFirstLogin() {
        return firstLogin;
    }
    
    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }
    
    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }
    
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the first Name
	 */
	public String getfirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the last Name
	 */
	public String getlastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setlastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

}

