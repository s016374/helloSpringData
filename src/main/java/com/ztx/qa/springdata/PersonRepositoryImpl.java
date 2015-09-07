package com.ztx.qa.springdata;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by s016374 on 15/9/7.
 */
public class PersonRepositoryImpl implements PersonDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void test() {
        Person person = entityManager.find(Person.class, 42);
        System.out.println("test() method: " + person);
    }
}
