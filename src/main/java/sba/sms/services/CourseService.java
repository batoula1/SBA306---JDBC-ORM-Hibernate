package sba.sms.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import sba.sms.dao.CourseI;
import sba.sms.models.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * CourseService is a concrete class. This class implements the
 * CourseI interface, overrides all abstract service methods and
 * provides implementation for each method.
 */
public class CourseService implements CourseI {

    SessionFactory factory = new Configuration().configure().buildSessionFactory();


    @Override
    public void createCourse(Course course) {
        Session session = factory.openSession();
        Transaction transaction = null;
        try{

            transaction = session.beginTransaction();

            session.persist(course);

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
    public Course getCourseById(int courseId) {

        Course course = null;
        Session   session = factory.openSession();
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();

            Query<Course> query = session.createQuery("From Course WHERE id=:id", Course.class);
            query.setParameter("id", courseId);

            course = query.getSingleResult();
            transaction.commit();

        }catch (Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }

        return course;
    }

    @Override
    public List<Course> getAllCourses() {
        Session session = factory.openSession();
        Transaction transaction = null;

        List<Course> courses = new ArrayList<>();
        try{
            transaction = session.beginTransaction();

            String hql = "FROM Course";
            Query<Course> query = session.createQuery(hql,
                    Course.class);
            courses = query.getResultList();

            transaction.commit();

        }catch (Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }

        return courses;
    }
}