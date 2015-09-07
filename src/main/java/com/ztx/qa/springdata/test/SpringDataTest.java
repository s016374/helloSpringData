package com.ztx.qa.springdata.test;

import com.ztx.qa.springdata.Person;
import com.ztx.qa.springdata.PersonRepository;
import com.ztx.qa.springdata.PersonService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * Created by s016374 on 15/9/1.
 */
public class SpringDataTest {

    private ApplicationContext applicationContext = null;
    private PersonRepository personRepository = null;
    private PersonService personService = null;

    @Before
    public void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        personRepository = applicationContext.getBean(PersonRepository.class);
        personService = applicationContext.getBean(PersonService.class);
    }

    @Test
    public void testDataSource() throws SQLException {
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        System.out.println(dataSource.getConnection());
    }

    @Test
    public void testJPA() {

    }

    @Test
    public void testHelloWorldSpringData() {
        Person person = personRepository.getByLastName("A");
        System.out.println(person);
    }

    @Test
    public void testKeyWords() {
        List<Person> personList = personRepository.getByLastNameStartingWithAndIdLessThan("AA", 5);
        System.out.println(personList.size());
        System.out.println(personList);

        personList = personRepository.getByEmailInAndBirthLessThan
                (Arrays.asList("A1@mail.com", "B1@mail.com"), new Date());
        System.out.println(personList.size());
        System.out.println(personList);
    }

    @Test
    public void testKeyWords2() {
        List<Person> personList = personRepository.getByAddress_IdGreaterThan(1);
        System.out.println(personList);
    }

    @Test
    public void testJpqlQuery() {
        List<Person> personList = personRepository.getMaxIdPerson();
        System.out.println(personList);

        personList = personRepository.getByLastNameAndEmail("B2", "B2@mail.com");
        System.out.println(personList);

        personList = personRepository.getByEmailAndLastName("A2", "A2@mail.com");
        System.out.println(personList);

        personList = personRepository.getByLastNameAndEmailLike("A1", "A1@mail.com");
        System.out.println(personList);

        personList = personRepository.getByEmailAndLastNameLike("B1", "B1@mail.com");
        System.out.println(personList);
    }

    @Test
    public void testNativeSql() {
        long totalCount = personRepository.getTotalCount();
        System.out.println(totalCount);
    }

    @Test
    public void testModify() {
        int updateAccount = 0;
        updateAccount = personService.updatePersonEmail("A3@mail.com", 11);
        System.out.println(updateAccount);
    }

    @Test
    public void testCrud() {
        List<Person> personList = new ArrayList<Person>();
        for (int i = 'a'; i <= 'z'; i++) {
            Person person = new Person();
            person.setId(i + 1);
            person.setBirth(new Date());
            person.setLastName((char) i + "" + (char) i);
            person.setEmail((char) i + "" + (char) i + "@mail.com");
            personList.add(person);
        }
        personService.addPersonEntities(personList);
    }

    @Test
    public void testPagingAndSorting() {
        //页数从0开始计算
        int pageNumber = 3;
        int pageSize = 5;

        Sort.Order order1 = new Sort.Order(Sort.Direction.DESC, "id");
        Sort.Order order2 = new Sort.Order(Sort.Direction.ASC, "email");
        Sort sort = new Sort(order1, order2);

        PageRequest pageRequest = new PageRequest(pageNumber, pageSize, sort);
        Page<Person> personPage = personRepository.findAll(pageRequest);

        System.out.println("total elements: " + personPage.getTotalElements());
        System.out.println("total pages: " + personPage.getTotalPages());
        System.out.println("current page: " + personPage.getNumber());
        System.out.println("current elements:" + personPage.getContent());
        System.out.println("count current number: " + personPage.getNumberOfElements());
    }

    @Test
    public void testJpa() {
        Person person = new Person();
        person.setBirth(new Date());
        person.setEmail("jpa@mail.com");
        person.setLastName("jpa");

        personRepository.saveAndFlush(person);
    }

    @Test
    public void testJpaExecutionE() {
        int pageNum = 4;
        int pageSize = 8;

        PageRequest pageRequest = new PageRequest(pageNum, pageSize);
        Specification<Person> personSpecification = new Specification<Person>() {
            public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Path path = root.get("id");
                Predicate predicate = criteriaBuilder.gt(path, 21);
                return predicate;
            }
        };

        Page<Person> personPage = personRepository.findAll(personSpecification, pageRequest);

        System.out.println("total elements: " + personPage.getTotalElements());
        System.out.println("total pages: " + personPage.getTotalPages());
        System.out.println("current page: " + personPage.getNumber());
        System.out.println("current elements:" + personPage.getContent());
        System.out.println("count current number: " + personPage.getNumberOfElements());
    }

    @Test
    public void testCustomRepositoryMethod() {
        personRepository.test();
    }

    @After
    public void tearDown() throws Exception {

    }
}