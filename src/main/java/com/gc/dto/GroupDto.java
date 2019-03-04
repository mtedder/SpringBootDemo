package com.gc.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by maurice on 8/20/17.
 * Dto for groups DB table.
 */
@Entity
@Table(name="groups")
public class GroupDto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int groupid;
	
	@Column(name="name")
    private String name;
	
	@Column(name="displayname")
    private String displayName;

    /*
    * Default constructor
     */
    public GroupDto() {
    }

    /*
    * Parameterized constructor
     */
    public GroupDto(int groupid, String name, String displayName) {
        this.groupid = groupid;
        this.name = name;
        this.displayName = displayName;
    }

    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "GroupDto{" +
                "groupid=" + groupid +
                ", name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
