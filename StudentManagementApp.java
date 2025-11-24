import java.util.*;

/**
 * Main application class with console-based user interface
 */
public class StudentManagementApp {
    private StudentManager manager;
    private Scanner scanner;
    
    public StudentManagementApp() {
        manager = new StudentManager();
        scanner = new Scanner(System.in);
    }
    
    public void run() {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║  STUDENT MANAGEMENT SYSTEM                 ║");
        System.out.println("║  Academic Performance Tracking             ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    addNewStudent();
                    break;
                case 2:
                    viewAllStudents();
                    break;
                case 3:
                    searchStudent();
                    break;
                case 4:
                    updateStudent();
                    break;
                case 5:
                    deleteStudent();
                    break;
                case 6:
                    addGradeToStudent();
                    break;
                case 7:
                    viewTranscript();
                    break;
                case 8:
                    viewAtRiskStudents();
                    break;
                case 9:
                    viewStatistics();
                    break;
                case 10:
                    exportData();
                    break;
                case 0:
                    running = false;
                    System.out.println("\nThank you for using Student Management System!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
    
    private void displayMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1.  Add New Student");
        System.out.println("2.  View All Students");
        System.out.println("3.  Search Student");
        System.out.println("4.  Update Student Information");
        System.out.println("5.  Delete Student");
        System.out.println("6.  Add/Update Grade");
        System.out.println("7.  View Student Transcript");
        System.out.println("8.  View At-Risk Students");
        System.out.println("9.  View System Statistics");
        System.out.println("10. Export Data to CSV");
        System.out.println("0.  Exit");
        System.out.println("===============================");
    }
    
    private void addNewStudent() {
        System.out.println("\n--- Add New Student ---");
        
        String id = getStringInput("Enter Student ID: ");
        if (manager.getStudent(id) != null) {
            System.out.println("Error: Student ID already exists!");
            return;
        }
        
        String name = getStringInput("Enter Name: ");
        String email = getStringInput("Enter Email: ");
        int age = getIntInput("Enter Age: ");
        
        Student student = new Student(id, name, email, age);
        if (manager.addStudent(student)) {
            System.out.println("✓ Student added successfully!");
        } else {
            System.out.println("✗ Failed to add student.");
        }
    }
    
    private void viewAllStudents() {
        System.out.println("\n--- All Students ---");
        System.out.println("1. Sort by Name");
        System.out.println("2. Sort by GPA");
        System.out.println("3. Default order");
        
        int choice = getIntInput("Choose sorting option: ");
        List<Student> students;
        
        switch (choice) {
            case 1:
                students = manager.getStudentsSortedByName();
                break;
            case 2:
                students = manager.getStudentsSortedByGPA();
                break;
            default:
                students = manager.getAllStudents();
        }
        
        if (students.isEmpty()) {
            System.out.println("No students in the system.");
        } else {
            System.out.println("\nTotal Students: " + students.size());
            System.out.println("─────────────────────────────────────────────────────────────────────────");
            for (Student student : students) {
                System.out.println(student);
            }
            System.out.println("─────────────────────────────────────────────────────────────────────────");
        }
    }
    
    private void searchStudent() {
        System.out.println("\n--- Search Student ---");
        String searchTerm = getStringInput("Enter student name (or part of it): ");
        
        List<Student> results = manager.searchByName(searchTerm);
        if (results.isEmpty()) {
            System.out.println("No students found matching: " + searchTerm);
        } else {
            System.out.println("\nSearch Results (" + results.size() + " found):");
            System.out.println("─────────────────────────────────────────────────────────────────────────");
            for (Student student : results) {
                System.out.println(student);
            }
            System.out.println("─────────────────────────────────────────────────────────────────────────");
        }
    }
    
    private void updateStudent() {
        System.out.println("\n--- Update Student Information ---");
        String id = getStringInput("Enter Student ID: ");
        
        Student student = manager.getStudent(id);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        
        System.out.println("Current Information: " + student);
        System.out.println("\nEnter new information (press Enter to keep current value):");
        
        String name = getStringInput("New Name [" + student.getName() + "]: ");
        if (name.isEmpty()) name = student.getName();
        
        String email = getStringInput("New Email [" + student.getEmail() + "]: ");
        if (email.isEmpty()) email = student.getEmail();
        
        String ageStr = getStringInput("New Age [" + student.getAge() + "]: ");
        int age = ageStr.isEmpty() ? student.getAge() : Integer.parseInt(ageStr);
        
        if (manager.updateStudent(id, name, email, age)) {
            System.out.println("✓ Student updated successfully!");
        } else {
            System.out.println("✗ Failed to update student.");
        }
    }
    
    private void deleteStudent() {
        System.out.println("\n--- Delete Student ---");
        String id = getStringInput("Enter Student ID: ");
        
        Student student = manager.getStudent(id);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        
        System.out.println("Student to delete: " + student);
        String confirm = getStringInput("Are you sure? (yes/no): ");
        
        if (confirm.equalsIgnoreCase("yes")) {
            if (manager.deleteStudent(id)) {
                System.out.println("✓ Student deleted successfully!");
            } else {
                System.out.println("✗ Failed to delete student.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
    
    private void addGradeToStudent() {
        System.out.println("\n--- Add/Update Grade ---");
        String id = getStringInput("Enter Student ID: ");
        
        Student student = manager.getStudent(id);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        
        System.out.println("Student: " + student.getName());
        String subject = getStringInput("Enter Subject Name: ");
        double grade = getDoubleInput("Enter Grade (0-100): ");
        
        if (manager.addGrade(id, subject, grade)) {
            System.out.println("✓ Grade added successfully!");
            System.out.println("Updated GPA: " + String.format("%.2f", student.calculateGPA()));
        } else {
            System.out.println("✗ Failed to add grade. Check that grade is between 0-100.");
        }
    }
    
    private void viewTranscript() {
        System.out.println("\n--- View Student Transcript ---");
        String id = getStringInput("Enter Student ID: ");
        
        Student student = manager.getStudent(id);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        
        System.out.println(student.generateTranscript());
    }
    
    private void viewAtRiskStudents() {
        System.out.println("\n--- At-Risk Students (GPA < 2.0) ---");
        List<Student> atRiskStudents = manager.getAtRiskStudents();
        
        if (atRiskStudents.isEmpty()) {
            System.out.println("No at-risk students found. All students are in good standing!");
        } else {
            System.out.println("Found " + atRiskStudents.size() + " at-risk student(s):");
            System.out.println("─────────────────────────────────────────────────────────────────────────");
            for (Student student : atRiskStudents) {
                System.out.println(student);
            }
            System.out.println("─────────────────────────────────────────────────────────────────────────");
            System.out.println("\n⚠ These students may need academic support or counseling.");
        }
    }
    
    private void viewStatistics() {
        System.out.println(manager.generateStatisticsReport());
    }
    
    private void exportData() {
        System.out.println("\n--- Export Data to CSV ---");
        String filename = getStringInput("Enter filename (e.g., students.csv): ");
        
        if (!filename.endsWith(".csv")) {
            filename += ".csv";
        }
        
        if (manager.exportToCSV(filename)) {
            System.out.println("✓ Data exported successfully to: " + filename);
        } else {
            System.out.println("✗ Failed to export data.");
        }
    }
    
    
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    public static void main(String[] args) {
        StudentManagementApp app = new StudentManagementApp();
        app.run();
    }
}