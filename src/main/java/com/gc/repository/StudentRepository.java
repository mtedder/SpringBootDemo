/**
 * 
 */
package com.gc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gc.dto.StudentDto;

/**
 * This is an example using JpaRepository for the students table.
 * 
 * @author maurice tedder
 *
 */
public interface StudentRepository extends JpaRepository<StudentDto, Integer> {

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.jpa.repository.JpaRepository#findAll()
	 */
	public List<StudentDto> findAll();
	
	/**
	 * @param group - String value of student group/cohort
	 * @return List of StudentDto's
	 */
	public List<StudentDto> findByGroup(String group);
	
    /*
    * Resets all students in the database to status parameter A (=available), T (=temporary), X (=unavailable)
    * Need to use JPQL here. Not sure if there is a better way to do batch updates using JpaRepository
    * Ref: https://codingexplained.com/coding/java/spring-framework/updating-entities-with-update-query-spring-data-jpa
    */
	@Transactional
	@Modifying
	@Query("UPDATE StudentDto s SET s.status = :status WHERE s.group = :group")
	public void resetStudentListStatus(@Param("status") String status, @Param("group") String group);
	

    /*
    * Get a random list of students who have not already been selected (status = "X"). The number of students
    * selected is based on the listsize parameter submitted in the form
    * 
    * Set the status column of a random selection of student to T (=temp)    	
    * Have to user JQL here or NQL (Native SQL because not sure if JPA supports the Rand() function
    * 
    * Ref: https://codingexplained.com/coding/java/spring-framework/updating-entities-with-update-query-spring-data-jpa
    * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query
    */
	@Transactional
	@Modifying
	@Query(value = "UPDATE students SET status = 'T' WHERE status = 'A' AND `cohort` = :group ORDER BY Rand() Limit :listsize", nativeQuery = true)	
	public void updatePickRandom(@Param("listsize") int listSize, @Param("group") String group);
	
	
	/**
	 * Select the student with a  T (=temp) in the status column - random students
	 * updatePickRandom must be called first
	 * Ref: https://codingexplained.com/coding/java/spring-framework/updating-entities-with-update-query-spring-data-jpa
	 */
	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM students WHERE status = 'T' AND `cohort` = :group", nativeQuery = true)			
	public List<StudentDto> pickRandomStudents(@Param("group") String group);
	
	/**
	 * Update the status column of the randomly selected students to X (=disabled/not available)
	 * @param group - String - cohort name 
	 */
	@Transactional
	@Modifying
	@Query("UPDATE StudentDto s SET s.status = 'X' WHERE s.status = 'T' AND s.group = :group")
	public void updateDisableRandowStudents(@Param("group") String group);
	
}
