package repository;

import domain.Client;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;


public class ClientHBNRepository  implements IClientRepository{

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

    public ClientHBNRepository() {
        initialize();
    }
    @Override
    public Client findById(String s) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Client.class, s);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updatePenaltyValue(int penaltyValue, String idClient) {
        try (Session session = sessionFactory.openSession())
        {
            Transaction transaction = session.beginTransaction();
            String hql = "UPDATE Client SET penalty = :penaltyValue WHERE id = :idClient";
            session.createQuery(hql)
                    .setParameter("penaltyValue", penaltyValue)
                    .setParameter("idClient", idClient)
                    .executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
