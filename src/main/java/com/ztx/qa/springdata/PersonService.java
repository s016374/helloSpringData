package com.ztx.qa.springdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by s016374 on 15/9/2.
 */
@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository = null;

    @Transactional
    public int updatePersonEmail(String email, Integer id) {
        return personRepository.updatePersonEmail(email, id);
    }

    @Transactional
    public void addPersonEntities(List<Person> personList) {
        personRepository.save(personList);
    }
}
