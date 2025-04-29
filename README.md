<!DOCTYPE html>
<html lang="en">
<body>

<h1>School Management System</h1>
<p>A Java Swing-based desktop application for managing students, teachers, and courses with secure login, role-specific dashboards, and enhanced features like profile pictures, password recovery, and messaging.</p>

<hr>

<h2>Features</h2>

<h3>System Access/Login</h3>
<ul>
    <li>Secure login screen with username/password authentication</li>
    <li>Passwords stored using hashing for enhanced security</li>
    <li>Password reset allows users to recover forgotten passwords via email</li>
    <li>Role-based access: Users can access only their specific dashboard (Admin, Teacher, Student)</li>
</ul>

<h3>Administrator Functions</h3>
<ul>
    <li><b>Student Management:</b>
        <ul>
            <li>Fields: First Name, Last Name, Email</li>
            <li>Auto-generated: Student ID, Initial Password</li>
            <li>Operations: Create, View, Update, Delete</li>
        </ul>
    </li>
    <li><b>Teacher Management:</b>
        <ul>
            <li>Fields: First Name, Last Name, Department, Email</li>
            <li>Auto-generated: Teacher ID, Initial Password</li>
            <li>Operations: Create, View, Update, Delete</li>
        </ul>
    </li>
    <li><b>Course Management:</b>
        <ul>
            <li>Fields: Course Code, Name, Maximum Capacity</li>
            <li>Operations: Create, View, Update, Delete</li>
            <li>Assign and unassign teachers to courses</li>
        </ul>
    </li>
</ul>

<h3>Teacher Functions</h3>
<ul>
    <li>View assigned courses</li>
    <li>View enrolled students</li>
    <li>Respond to student messages via email</li>
</ul>

<h3>Student Functions</h3>
<ul>
    <li>View available courses (that are not full)</li>
    <li>Enroll in courses</li>
    <li>View enrolled courses</li>
    <li>Send messages to teachers via email</li>
</ul>

<hr>

<h2>Advanced Features</h2>

<h3>Data Validation</h3>
<ul>
    <li>Email format validation</li>
    <li>Required fields must be completed</li>
    <li>Duplicate prevention (email addresses, course codes)</li>
</ul>

<h3>Enhanced User Experience</h3>
<ul>
    <li>Upload profile pictures (JPG/PNG, max 5MB)</li>
    <li>Password recovery via email</li>
    <li>Search and filter students, teachers, and courses</li>
</ul>

<hr>

<h2>Technologies Used</h2>
<ul>
    <li>Java SE (Swing, AWT)</li>
    <li>MySQL (Database)</li>
    <li>MVC Architecture</li>
    <li>Apache Commons Email for messaging</li>
    <li>JavaMail API for sending password recovery emails</li>
    <li>File Handling for profile pictures</li>
</ul>

<hr>

<h2>Project Structure</h2>
<pre>
├── app/
│   └── Main.java
├── controller/
│   ├── AdminController.java
│   ├── AuthenticatorController.java
│   ├── StudentController.java
│   └── TeacherController.java
├── model/
│   ├── Course.java
│   ├── CourseDA.java
│   ├── Email.java
│   ├── EmailDA.java
│   ├── Student.java
│   ├── Teacher.java
│   ├── User.java
│   └── UserDA.java
├── my_util/
│   ├── Database.java
│   ├── Security.java
│   └── Validation.java
└── view/
    ├── AdminView.java
    ├── AuthenticatorView.java
    ├── CommonView.java
    ├── ResetPasswordView.java
    ├── StudentView.java
    ├── TeacherView.java
    └── UpdatePasswordView.java
</pre>

</body>
</html>
