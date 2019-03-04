package com.gc.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by maurice on 6/27/17.
 */
@Entity
@Table(name="students")
public class StudentDto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="first_name")
    private String firstName;
	
	@Column(name="last_name")
    private String lastName;
	
	@Column(name="status")
    private String status;
	
	@Column(name="cohort")
    private String group;

    
    public StudentDto() {
		// TODO Auto-generated constructor stub
	}

	/*
    * Parameterized constructor
     */
    public StudentDto(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = "A";
    }

    /*
    * Parameterized constructor - includes status
     */
    public StudentDto(String firstName, String lastName, String status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
    }

    /*
    * Parameterized constructor - includes status and group
     */
    public StudentDto(String firstName, String lastName, String status, String group) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.group = group;
    }
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "StudentDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
