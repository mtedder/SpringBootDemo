/**
 * 
 */
package com.gc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gc.dto.GroupDto;

/**
 * This is an example using JpaRepository for the groups table.
 * @author maurice tedder
 *
 */
public interface GroupRepository extends JpaRepository<GroupDto, Integer> {

	/* 
	 * Get all groups
	 * 
	 * (non-Javadoc)
	 * @see org.springframework.data.jpa.repository.JpaRepository#findAll()
	 */
	public List<GroupDto> findAll();
}
