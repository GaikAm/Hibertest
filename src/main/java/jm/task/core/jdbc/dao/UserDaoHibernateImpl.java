package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.*;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao, AutoCloseable {

    private static String CREATE_TABLE = "CREATE TABLE `testbase`.`users` (\n" +
            "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
            "  `name` VARCHAR(45) NULL,\n" +
            "  `lastname` VARCHAR(45) NULL,\n" +
            "  `age` TINYINT(1) UNSIGNED NULL,\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);";
    private static String DROP_TABLE = "DROP TABLE users;";

    private Util util = new Util();

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        try (Session session = util.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            session.createSQLQuery(CREATE_TABLE).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Ошибка создания таблицы");
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = util.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            session.createSQLQuery(DROP_TABLE).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Ошибка удаления таблицы");
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = util.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
            session.getTransaction().commit();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (HibernateException e) {
            System.out.println("Ошибка добавления пользователя");
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = util.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
            }
            session.getTransaction().commit();
        } catch (MappingException e) {
            System.out.println("Ошибка удаления пользователя");
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> resultList = null;
        try (Session session = util.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            resultList = session.createQuery("select i from User i", User.class)
                    .getResultList();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("Не удалось получить всех пользователей (Таблицы нет)");
        }
        return resultList;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = util.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            session.createQuery("delete from User u").executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("Не удалось очистить таблицу");
        }
    }

    @Override
    public void close() throws Exception {
        util.close();
    }
}
