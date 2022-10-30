package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.HibernateError;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util implements AutoCloseable {

    private final String URL =
            "jdbc:mysql://localhost:3306/testbase";
    private final String USERNAME = "root";
    private final String PASSWORD = "Gaik-20011412";

    private Connection connection;

    private SessionFactory sessionFactory;


    public Util() {
        // === JDBC ===
        /*try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);
        } catch (SQLException e) {
            System.out.println("Драйвер не зарегестрировался!");
        }*/

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if (connection.isClosed()) {
                System.out.println("Соединение не установлено!");
            }
        } catch (SQLException e) {
            System.out.println("Соединение не установлено!");
        }

        // === Hibernate ===
        try {
            sessionFactory = new Configuration()
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory();
        } catch (HibernateException e) {
            System.out.println("Соединение не установлено!");
        }

        /*final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.properties")
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (HibernateException e) {
            StandardServiceRegistryBuilder.destroy(registry);
            System.out.println("Соединение не установлено!");
        }*/
    }

    public Connection getConnection() { return connection; }

    public SessionFactory getSessionFactory() { return sessionFactory; }

    @Override
    protected void finalize() {
        //connection.close();
        sessionFactory.close();
    }

    @Override
    public void close() {
        //connection.close();
        sessionFactory.close();
    }
}
