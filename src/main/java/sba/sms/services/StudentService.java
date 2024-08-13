package sba.sms.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import sba.sms.dao.StudentI;
import sba.sms.models.Course;
import sba.sms.models.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * StudentService is a concrete class. This class implements the
 * StudentI interface, overrides all abstract service methods and
 * provides implementation for each method. Lombok @Log used to
 * generate a logger file.
 */

public class StudentService implements StudentI{
    CourseService service = new CourseService();
    SessionFactory factory = new Configuration().configure().buildSessionFactory();;

    @Override
    public List<Student> getAllStudents() {
        Session session = factory.openSession();
        Transaction transaction = null;
        List<Student> students = new ArrayList<>();
        try{
            transaction = session.beginTransaction();

            Query<Student> query = session.createQuery("FROM Student", Student.class);

            students = query.getResultList();

            transaction.commit();

        }catch (Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }

        return students;
    }

    @Override
    public void createStudent(Student student) {
        Session session = factory.openSession();
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();

            session.persist(student);

            transaction.commit();

        }catch (Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }
    }

    @Override
    public Student getStudentByEmail(String email) {
        Session session = factory.openSession();
        Transaction transaction = null;
        Student student = new Student();
        try{
            transaction = session.beginTransaction();

            Query<Student> query = session.createQuery("FROM Student WHERE email=:email", Student.class);
            query.setParameter("email", email);

            student = query.getSingleResult();

            transaction.commit();

        }catch (Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }

        return student;
    }

    @Override
    public boolean validateStudent(String email, String password) {
        Student student = getStudentByEmail(email);
        if(student != null && student.getEmail().equals(email)){
            return true;
        }
        return false;
    }

    @Override
    public void registerStudentToCourse(String email, int courseId) {
        Session  session = factory.openSession();
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();

            Student student = getStudentByEmail(email);
            Course course = service.getCourseById(courseId);

            student.addCourse(course);

            session.merge(student);
            transaction.commit();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
    }

    @Override
    public List<Course> getStudentCourses(String email) {
        Session session = factory.openSession();
        Transaction transaction = null;
        List<Course> courses = new ArrayList<>();

        try{

            transaction = session.beginTransaction();

            String sql = "SELECT course.courses_id, course_name, instructor_name" +
                    " FROM course INNER JOIN student_courses ON course.courses_id = student_courses.courses_id WHERE student_email = :email";

            NativeQuery<Course> query = session.createNativeQuery(sql, Course.class);
            query.setParameter("email", email);

            courses = query.getResultList();

            transaction.commit();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }

        return courses;
    }
}