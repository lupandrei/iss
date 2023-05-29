package repository;

import domain.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;

public class BookHBNRepository implements IBookRepository{
    private static SessionFactory sessionFactory;

    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("Exceptie "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    static void close() {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }

    public BookHBNRepository() {
        initialize();
    }

    @Override
    public void add(Book entity) {
        try (Session session = sessionFactory.openSession()) {
            Book existingBook = findBookByName(entity.getName());
            if (existingBook != null) {
                existingBook.setCopies(existingBook.getCopies() + entity.getCopies());
                session.beginTransaction();
                session.update(existingBook);
                session.getTransaction().commit();
            } else {
                session.beginTransaction();
                session.save(entity);
                session.getTransaction().commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Book> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Book> query = session.createQuery("FROM Book", Book.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Book> getAllFilteredByAuthor(String author) {
        try (Session session = sessionFactory.openSession()) {
            Query<Book> query = session.createQuery("FROM Book WHERE author = :author", Book.class);
            query.setParameter("author", author);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<String> getAllAuthors() {
        try (Session session = sessionFactory.openSession()) {
            Query<String> query = session.createQuery("SELECT DISTINCT author FROM Book", String.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Book findBookByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Book> query = session.createQuery("FROM Book WHERE name = :name", Book.class);
            query.setParameter("name", name);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateNoCopies(int id, int copies) {
        try (Session session = sessionFactory.openSession()) {
            Book book = session.get(Book.class, id);
            if (book != null) {
                book.setCopies(copies);
                session.beginTransaction();
                session.update(book);
                session.getTransaction().commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Book findBookById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Book.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
