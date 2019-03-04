package com.gc.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gc.dto.GroupDto;
import com.gc.dto.StudentDto;
import com.gc.dto.User;

/**
 * Created by maurice on 6/28/17.
 * 
 * This is an example using entityManager criteria/native queries and JQL
 * 
 * Ref: https://www.objectdb.com/java/jpa/query/jpql/structure
 */
@Repository("studentPickerBoot")
@Transactional
public class HibernateDao implements Dao {

	//private static final String UPDATE_STUDENTS_STATUS_SQL = "UPDATE StudentDto AS st SET st.status = :s WHERE st.group = :p";
	private static final String UPDATE_PICK_RANDOM_SQL = "UPDATE students SET status = \"T\" WHERE status = \"A\" AND `cohort` = ? ORDER BY Rand() Limit ?";
	
	/**
	 * Abstraction to interact with the database
	 */
	@PersistenceContext
	EntityManager entityManager;
	
    /*
     * Add student list to DB
     * Ref: https://www.objectdb.com/java/jpa/persistence/store#Batch_Store_
    */
    public void addStudents(List<StudentDto> students) {
        //A loop seems to be the only way to do batch inserts in JPA/Hibernate?
    	for(StudentDto student: students) {
    		entityManager.persist(student);
    	}
    }

    /*
    * Get a random list of students who have not already been selected (status = "X"). The number of students
    * selected is based on the listsize parameter submitted in the form
    * Ref: https://www.thoughts-on-java.org/jpa-native-queries/
    */
    public List<StudentDto> getRandomStudents(int listSize, String group) {
        //3. Create statements
        //Set the status column of a random selection of student to T (=temp)    	
    	
    	//Have to user JQL here or NQL (Native SQL because not sure if JPA supports the Rand() function
    	Query query = entityManager.createNativeQuery(UPDATE_PICK_RANDOM_SQL);
    	query.setParameter(1, group);
    	query.setParameter(2, listSize);
    	
    	query.executeUpdate();
    	
    	//Select the student with a  T (=temp) in the status column - random students
    	//Go back to using JPA/Hibernate Criteria here
    	//SELECT_PICK_RANDOM_SQL = "SELECT * FROM students WHERE status = \"T\" AND `cohort` = ?";
     	CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    	CriteriaQuery<StudentDto> criteriaQuery = criteriaBuilder.createQuery(StudentDto.class);
    	Root<StudentDto> from = criteriaQuery.from(StudentDto.class);
    	
    	//select students from the requested group
    	ParameterExpression<String> statusParamEx = criteriaBuilder.parameter(String.class);
    	ParameterExpression<String> groupParamEx = criteriaBuilder.parameter(String.class);
    	
    	
    	CriteriaQuery<StudentDto> select = criteriaQuery.select(from).where(criteriaBuilder.and(
    			criteriaBuilder.equal(from.get("status"), statusParamEx),
    			criteriaBuilder.equal(from.get("group"), groupParamEx))//end AND Conjunction
    			);//end where clause
    	
    	TypedQuery<StudentDto> query2 = entityManager.createQuery(select);
    	query2.setParameter(statusParamEx, "T");//set where status parameter
    	query2.setParameter(groupParamEx, group);//set where cohort/group parameter  
    	
    	List<StudentDto> selectedStudents = query2.getResultList();
    	
    	//TODO put this part in a separate method at some point
    	//Update the status column of the randomly selected students to X (=disabled/not available)
    	//UPDATE_DISABLE_RANDOM_STUDENTS = "UPDATE students SET status = \"X\" WHERE status = \"T\" AND `cohort` = ?";
    	CriteriaUpdate<StudentDto> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(StudentDto.class);
    	Root<StudentDto> root = criteriaUpdate.from(StudentDto.class);
    	
    	//update the status column/property
    	criteriaUpdate.set(root.get("status"), "X");
    	
    	//where clause - update
    	criteriaUpdate.where(criteriaBuilder.and(
    			criteriaBuilder.equal(root.get("status"), "T"),
    			criteriaBuilder.equal(root.get("group"), group))//end AND Conjunction
    			);//end where clause
    	
    	int affected = entityManager.createQuery(criteriaUpdate).executeUpdate();//Doing nothing with this result
    	
    	return selectedStudents;
    }

    /*
    * Resets all students in the database to status parameter A (=available), T (=temporary), X (=unavailable)
    * Ref: https://www.objectdb.com/java/jpa/query/jpql/update
    * http://dreamand.me/java/jpa-update-selected-fields/
    */
    public void resetStudentListStatus(String status, String group) {
    	
    	CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    	CriteriaUpdate<StudentDto> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(StudentDto.class);
    	Root<StudentDto> root = criteriaUpdate.from(StudentDto.class);
    	
    	//update the status column/property
    	criteriaUpdate.set(root.get("status"), status);
    	
    	//where clause - update
    	criteriaUpdate.where(criteriaBuilder.equal(root.get("group"), group));
    	
    	int affected = entityManager.createQuery(criteriaUpdate).executeUpdate();//maybe this should be returned from this method
    	
    }

    /*
    * Get list of all students from DB.
    * Ref: https://www.objectdb.com/java/jpa/query/jpql/where
     */
    public List<StudentDto> getStudents(String group) {
    	
    	CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    	CriteriaQuery<StudentDto> criteriaQuery = criteriaBuilder.createQuery(StudentDto.class);
    	Root<StudentDto> from = criteriaQuery.from(StudentDto.class);
    	
    	//select students from the requested group
    	ParameterExpression<String> paramEx = criteriaBuilder.parameter(String.class);
    	
    	
    	CriteriaQuery<StudentDto> select = criteriaQuery.select(from).where(criteriaBuilder.equal(from.get("group"), paramEx));
    	
    	TypedQuery<StudentDto> query = entityManager.createQuery(select);
    	query.setParameter(paramEx, group);//set where parameter
    	
    	List<StudentDto> allStudents = query.getResultList();
    	return allStudents;
    }

    /*
     * Get a list of all groups from DB.
     * Ref: https://www.tutorialspoint.com/jpa/jpa_criteria_api.htm
     * (non-Javadoc)
     * @see com.gc.dao.Dao#getGroups()
     */
    public List<GroupDto> getGroups() {
    	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    	//This instance is used to create a query object. This query object’s attributes will be modified with the details of the query.
    	CriteriaQuery<GroupDto> query = cb.createQuery(GroupDto.class);
    	
    	//from method is called to set the query root.
    	Root<GroupDto> from = query.from(GroupDto.class);
    	
    	//select all records
    	//select is called to set the result list type
    	CriteriaQuery<GroupDto> select = query.select(from);
    	
    	//instance is used to prepare a query for execution and specifying the type of the query result
    	TypedQuery<GroupDto> q = entityManager.createQuery(select);
    	
    	//object to execute a query. This query returns a collection of entities, the result is stored in a List.
    	List<GroupDto> allGroups = q.getResultList();
    	
    	return allGroups;
    }

    /* (non-Javadoc)
     * @see com.gc.dao.Dao#findByUserName(java.lang.String)
     */
    public User findByUserName(String username) {
        //TODO
        return null;
    }

}
