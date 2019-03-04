package com.gc.dao;

import static java.sql.DriverManager.getConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gc.dto.GroupDto;
import com.gc.dto.StudentDto;
import com.gc.dto.User;

/**
 * Created by maurice on 6/27/17.
 */
@Repository // talks to the DB
//public class JDBCDao implements Dao {
public class JDBCDao {	
    //JDBC Connection details
	// need to use JdbcTemplate to execute a query
	@Autowired // use Spring to autowire it
	private JdbcTemplate jdbcTemplate;
		
    private static final String JDBC_MYSQL_DRIVER_STRING = "com.mysql.jdbc.Driver";
//    private String CONNECTION_URL = "jdbc:mysql://localhost:3306/studentPickerDB";//Use this part to run locally + "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
    private String CONNECTION_URL = "jdbc:mysql://studentpickerdb.cnbrf3dwerbk.us-east-2.rds.amazonaws.com:3306/studentPickerDB";//aws DB
    private String USERNAME = "NoneOfYourBusiness";//THIS IS BAD - but OK for localhost!!! - replace with environment variables latter
    private String PWD = "ReallyNoneOfYourBusiness";//THIS IS BAD - but OK for localhost!!! - replace with environment variables latter

    //SQL statements
    private static final String INSERT_STUDENTS_SQL = "INSERT INTO students (`first_name`,`last_name`,`status`, `cohort`) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_PICK_RANDOM_SQL = "UPDATE students SET status = \"T\" WHERE status = \"A\" AND `cohort` = ? ORDER BY Rand() Limit ?";
    private static final String SELECT_PICK_RANDOM_SQL = "SELECT * FROM students WHERE status = \"T\" AND `cohort` = ?";
    private static final String UPDATE_DISABLE_RANDOM_STUDENTS = "UPDATE students SET status = \"X\" WHERE status = \"T\" AND `cohort` = ?";
    private static final String UPDATE_STUDENTS_STATUS_SQL = "UPDATE students SET status = ? WHERE `cohort` = ?";
    private static final String SELECT_STUDENTS_SQL = "SELECT * FROM students WHERE `cohort` =?";
    private static final String SELECT_GROUPS_SQL = "SELECT * FROM groups";
    private static final String FIND_USER_BY_USERNAME_SQL = "SELECT username,password,enabled, userid FROM users WHERE username=?";
    private static final String AUTHORITIES_BY_USERNAME_SQL = "SELECT role from user_roles where username =?";

    /*
    * Default constructor - Not needed for dependency injection
     */
   

    /*
     * Add student list to DB
    */
    public void addStudents(List<StudentDto> students) {
    
            //3. Create statement    	
    		//batch inputs
    	List<Object[]> batchData = new ArrayList<>();
			for(StudentDto student: students) {//do batch insert
				batchData.add(new Object[] {student.getFirstName(), student.getLastName(), student.getStatus(), student.getGroup()});               
            }
			jdbcTemplate.batchUpdate(INSERT_STUDENTS_SQL, batchData);
            
    }

    /*
    * Get a random list of students who have not already been selected (status = "X"). The number of students
    * selected is based on the listsize parameter submitted in the form
     */
    public List<StudentDto> getRandomStudents(int listSize, String group) {
        
           //3. Create statements
           //Set the status column of a random selection of student to T (=temp)           
           jdbcTemplate.update(UPDATE_PICK_RANDOM_SQL, new Object[] {group, listSize});
            
           //Select the student with a  T (=temp) in the status column - random students          
           List<StudentDto> students = jdbcTemplate.query(SELECT_PICK_RANDOM_SQL, new Object[] {group}, new BeanPropertyRowMapper<>(StudentDto.class));                 

           
           //Update the status column of the student with a T in the status column (randomly selected students) to X (=disabled/not available)
           jdbcTemplate.update(UPDATE_DISABLE_RANDOM_STUDENTS, new Object[] {group});
           
        return students;
    }

    /*
    * Resets all students in the database to status parameter A (=available), T (=temporary), X (=unavailable)
    */
    public void resetStudentListStatus(String status, String group) {
       
        //3. Create statements
    	//Set the status column of a random selection of student to T (=temp)
        //UPDATE_STUDENTS_STATUS_SQL
    	jdbcTemplate.update(UPDATE_STUDENTS_STATUS_SQL, new Object[] {status, group});
           
    }

    /*
    * Get list of all students from DB.
     */    
    public List<StudentDto> getStudents(String group) {
    	
    	
        List<StudentDto> students = jdbcTemplate.query(SELECT_STUDENTS_SQL, new Object[] {group}, new BeanPropertyRowMapper<>(StudentDto.class));
        
        return students;
    }

    /*
    * Get list of groups from groups DB table
    */
    public List<GroupDto> getGroups() {
       
        return jdbcTemplate.query(SELECT_GROUPS_SQL, new BeanPropertyRowMapper<GroupDto>(GroupDto.class));
    }

    /*
    * Get user by user name
    * Ref: http://www.baeldung.com/spring-security-authentication-with-a-database
     */
    /*public com.gc.dto.User findByUserName(String username) {

        return new User();
    }*/
}
