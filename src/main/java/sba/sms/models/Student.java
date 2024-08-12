package sba.sms.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


/**
 * Student is a POJO, configured as a persistent class that represents (or maps to) a table
 * name 'student' in the database. A Student object contains fields that represent student
 * login credentials and a join table containing a registered student's email and course(s)
 * data. The Student class can be viewed as the owner of the bi-directional relationship.
 * Implement Lombok annotations to eliminate boilerplate code.
 */

@Entity
@Table(name = "student")
@Data
@RequiredArgsConstructor()
public class Student {
    @Id
    @Column(name = "email", length = 50, nullable = false)
    private  String email;

    @Column(name = "name",  nullable = false ,length = 50)
    private String name;

    @Column(name = "password", nullable = false, length = 50)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.REMOVE,
                    CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "student_courses",
            joinColumns = {@JoinColumn(name = "student_email")},
            inverseJoinColumns = {@JoinColumn(name = "courses_id")}
    )
    private Set<Course> courses = new HashSet<>();

    public Student(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(email, student.email) && Objects.equals(name, student.name) && Objects.equals(password, student.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, name, password);
    }
}