package com.ztx.qa.springdata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by s016374 on 15/9/1.
 */
//@RepositoryDefinition(domainClass = Person.class, idClass = Integer.class)
public interface PersonRepository extends JpaRepository<Person, Integer>, JpaSpecificationExecutor, PersonDao {
    Person getByLastName(String lastName);

    /*方法名作为关键字*/
    List<Person> getByLastNameStartingWithAndIdLessThan(String lastName, Integer id);

    List<Person> getByEmailInAndBirthLessThan(List<String> emails, Date birth);

    List<Person> getByAddress_IdGreaterThan(Integer id);

    /*通过注解*/
    /* ?1和?2 占位符形式*/
    @Query("select p from Person p where p.id = (select max(p2.id) from Person p2)")
    List<Person> getMaxIdPerson();

    /* :para，@param("para") 注解形式 */
    @Query("select p from Person p where p.lastName = ?1 and p.email = ?2")
    List<Person> getByLastNameAndEmail(String lastName, String email);

    @Query("select p from Person p where p.email = :email and p.lastName = :lastName")
    List<Person> getByEmailAndLastName(@Param(value = "lastName")String lastName, @Param(value = "email")String email);

    /* like 用%%*/
    @Query("select p from Person p where p.lastName like %?1% and p.email like %?2%")
    List<Person> getByLastNameAndEmailLike(String lastName, String email);

    @Query("select p from Person p where p.email like %:email% and p.lastName like %:lastName%")
    List<Person> getByEmailAndLastNameLike(@Param("lastName")String lastName, @Param("email")String email);

    /*nativeSql 通过nativeQuery=ture，直接写sql，如表名写成数据库里的表名*/
    @Query(value = "select count(id) from jpa_person", nativeQuery = true)
    long getTotalCount();

    /* @Modifying来做修改，修改需要有事务，所以需要有PersonService，有@Transactional才行*/
    @Modifying
    @Query("update Person p set p.email = :email where id = :id")
    int updatePersonEmail(@Param("email") String email, @Param("id") Integer id);

}
//public interface PersonRepository extends Repository<Person, Integer> {
//    Person getByLastName(String lastName);
//}

