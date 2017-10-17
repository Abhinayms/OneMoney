package com.sevya.launchpad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sevya.launchpad.model.Department;

@Repository
public interface DepartmentRepository extends CrudRepository<Department, Long> {

	@Query("FROM Department department WHERE department.id = :departmentId "
			+ "AND department.isActive = true")
	public Department getDepartmentById(@Param("departmentId") Long departmentId);
	
	@Query("FROM Department department "
			+ "LEFT JOIN FETCH department.division AS division "
			+ "WHERE division.id = :divisionId AND division.isActive = true "
			+ "AND department.isActive = true")
	public List<Department> getAllDepartmentsByDivisionId(@Param("divisionId") Long divisionId);

	@Query("FROM Department department WHERE department.uuid = :uuid "
			+ "AND department.isActive = true")
	public Department getDepartmentByUUID(@Param("uuid")String uuid);

}
