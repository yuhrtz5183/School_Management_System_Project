package model;

public class Course {

    private String code, name, description, status;
    
    private int maxCapacity;
	
    public Course(String code, String name, String description, String status, int maxCapacity) {
        super();
        this.code = code;
        this.name = name;
        this.description = description;
        this.status = status;
        this.maxCapacity = maxCapacity;
    }


    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the courseName
     */
    public String getName() {
        return name;
    }

    /**
     * @param courseName the courseName to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param desciption the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Course [code=" + code + ", courseName=" + name + ", description=" + description + ", status="
                + status + ", max_capacity=" + maxCapacity + "]";
    }

    /**
     * @return the max_capacity
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * @param max_capacity the max_capacity to set
     */
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
 
    
}








