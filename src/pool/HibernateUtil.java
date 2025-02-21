package model;

import org.hibernate.SessionFactory; 
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import pojos.Alumno;
import pojos.Grupo;


public class HibernateUtil {
    private static  SessionFactory sessionFactory ;

     static {
        try {
            Configuration configuration=new Configuration().configure("hibernate.cfg.xml");
            
            configuration.addAnnotatedClass(Alumno.class);
            configuration.addAnnotatedClass(Grupo.class); 
            
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            
            sessionFactory = configuration.buildSessionFactory(registry);
           
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("Error al inicializar Hibernate: " + ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
