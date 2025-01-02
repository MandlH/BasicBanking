package org.mandl.database;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.mandl.entities.BankAccount;
import org.mandl.entities.BankAccountType;
import org.mandl.entities.Transaction;
import org.mandl.entities.TransactionType;
import org.mandl.identity.IdentityRole;
import org.mandl.identity.IdentityUser;

@ApplicationScoped
public class DatabaseConnection {

    @Produces
    @ApplicationScoped
    public SessionFactory produceSessionFactory() {
        var configuration = new Configuration();

        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:~/banking_app;AUTO_SERVER=TRUE");
        configuration.setProperty("hibernate.connection.username", "sa");
        configuration.setProperty("hibernate.connection.password", "");
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");

        configuration.setProperty("hibernate.use_sql_comments", "true");  // Add comments in the SQL for context

        // Disable direct Hibernate SQL output
        configuration.setProperty("hibernate.show_sql", "false");
        configuration.setProperty("hibernate.format_sql", "false");

        configuration.addAnnotatedClass(IdentityUser.class);
        configuration.addAnnotatedClass(IdentityRole.class);
        configuration.addAnnotatedClass(BankAccount.class);
        configuration.addAnnotatedClass(BankAccountType.class);
        configuration.addAnnotatedClass(Transaction.class);
        configuration.addAnnotatedClass(TransactionType.class);

        var serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    @PostConstruct
    public void initializeSessionFactory() {
        // Force initialization of the SessionFactory
        produceSessionFactory();
    }
}
