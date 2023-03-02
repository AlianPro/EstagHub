package br.com.estaghub.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateUtil {
    public static EntityManagerFactory emf = Persistence.createEntityManagerFactory("estaghub");

}
