package com.elorserv.controllers;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.elorserv.model.Tipos;
import com.elorserv.model.Users;

public class HibernateUtil {
	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {
            Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
            configuration.getProperties().remove("hibernate.session_factory_name");
            configuration.addAnnotatedClass(Users.class);
            configuration.addAnnotatedClass(Tipos.class);

            // 3. Construimos la factoría de sesiones
            return configuration.buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
