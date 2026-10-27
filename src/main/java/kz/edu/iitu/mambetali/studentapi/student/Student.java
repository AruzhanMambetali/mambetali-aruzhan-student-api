package kz.edu.iitu.mambetali.studentapi.student;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String fullName;

    @Column(nullable = false, length = 180)
    private String email;

    @Column(nullable = false, length = 120)
    private String program;

    @Column(nullable = false)
    private int year;

    protected Student() {
    }

    public Student(String fullName, String email, String program, int year) {
        this.fullName = fullName;
        this.email = email;
        this.program = program;
        this.year = year;
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getProgram() { return program; }
    public int getYear() { return year; }

    public void update(String fullName, String email, String program, int year) {
        this.fullName = fullName;
        this.email = email;
        this.program = program;
        this.year = year;
    }
}
