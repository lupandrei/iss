package repository;

import domain.Rental;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;

public class RentalHBNRepository implements IRentalRepository{
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
    public void RentalHBNRepository(){
        initialize();
    }
    @Override
    public void add(Rental entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Rental> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Rental> query = session.createQuery("FROM Rental", Rental.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateRentalStatus(int idRental, String newStatus) {
        try ( Session session = sessionFactory.openSession())
        {
            Transaction transaction = session.beginTransaction();
            String hql = "UPDATE Rental SET status = :newStatus WHERE id = :idRental";
            session.createQuery(hql)
                    .setParameter("newStatus", newStatus)
                    .setParameter("idRental", idRental)
                    .executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
