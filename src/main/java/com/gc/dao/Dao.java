package com.gc.dao;

import java.util.List;

import com.gc.dto.GroupDto;
import com.gc.dto.StudentDto;
import com.gc.dto.User;

/**
 * Created by maurice on 6/27/17.
 */
public interface Dao {

    //Add student list to DB
    void addStudents(List<StudentDto> students);

    /*
    * Get a random list of students who have not already been selected (status = "X"). The number of students
    * selected is based on the listsize parameter submitted in the form
     */
    List<StudentDto> getRandomStudents(int listSize, String group);

    /*
    * Resets all students in the database to status parameter A (=available), T (=temporary), X (=unavailable)
     */
    void resetStudentListStatus(String status, String group);

    /*
    * Get list of all students from DB.
    */
    List<StudentDto> getStudents(String group);

    /*
    * Get list of groups from groups DB table
     */
    List<GroupDto> getGroups();

    /*
    * Get user by user name
     */
    User findByUserName(String username);
}
