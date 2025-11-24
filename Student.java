import java.util.*;
import java.io.Serializable;


public class Student implements Serializable, Comparable<Student> {
    private static final long serialVersionUID = 1L;
    
    private String studentId;
    private String name;
    private String email;
    private int age;
    private Map<String, Double> grades; 
    
    public Student(String studentId, String name, String email, int age) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.age = age;
        this.grades = new HashMap<>();
    }
    
    
    public void addGrade(String subject, double grade) {
        if (grade < 0 || grade > 100) {
            throw new IllegalArgumentException("Grade must be between 0 and 100");
        }
        grades.put(subject, grade);
    }
    
    
    public double calculateGPA() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        
        double totalPoints = 0.0;
        for (double grade : grades.values()) {
            totalPoints += convertToGPA(grade);
        }
        return totalPoints / grades.size();
    }
    
    
    private double convertToGPA(double grade) {
        if (grade >= 90) return 4.0;
        if (grade >= 80) return 3.0;
        if (grade >= 70) return 2.0;
        if (grade >= 60) return 1.0;
        return 0.0;
    }
    
    
    public String getLetterGrade(double grade) {
        if (grade >= 90) return "A";
        if (grade >= 80) return "B";
        if (grade >= 70) return "C";
        if (grade >= 60) return "D";
        return "F";
    }
    
    
    public boolean isAtRisk() {
        return calculateGPA() < 2.0;
    }
    
    
    public double getAverageGrade() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        return grades.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
    
    
    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public Map<String, Double> getGrades() { return new HashMap<>(grades); }
    
    @Override
    public int compareTo(Student other) {
        return this.name.compareTo(other.name);
    }
    
    @Override
    public String toString() {
        return String.format("ID: %s | Name: %s | Email: %s | Age: %d | GPA: %.2f | Avg Grade: %.2f%%",
                studentId, name, email, age, calculateGPA(), getAverageGrade());
    }
    
    
    public String generateTranscript() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== STUDENT TRANSCRIPT ==========\n");
        sb.append(String.format("Student ID: %s\n", studentId));
        sb.append(String.format("Name: %s\n", name));
        sb.append(String.format("Email: %s\n", email));
        sb.append(String.format("Age: %d\n\n", age));
        sb.append("GRADES:\n");
        sb.append("----------------------------------------\n");
        
        if (grades.isEmpty()) {
            sb.append("No grades recorded\n");
        } else {
            for (Map.Entry<String, Double> entry : grades.entrySet()) {
                sb.append(String.format("%-20s: %6.2f%% (%s)\n", 
                    entry.getKey(), entry.getValue(), getLetterGrade(entry.getValue())));
            }
            sb.append("----------------------------------------\n");
            sb.append(String.format("Average Grade: %.2f%%\n", getAverageGrade()));
            sb.append(String.format("GPA: %.2f / 4.0\n", calculateGPA()));
            sb.append(String.format("Status: %s\n", isAtRisk() ? "AT RISK - Needs Support" : "Good Standing"));
        }
        sb.append("========================================\n");
        return sb.toString();
    }
}