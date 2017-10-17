package com.sevya.onemoney.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sevya.onemoney.model.Goal;
import com.sevya.onemoney.utility.Purpose;

@Repository
public interface GoalsRepository extends CrudRepository<Goal, Long> {
	
		@Query(value = "FROM Goal goal where goal.createdBy.id = :userId AND goal.isActive = true")
		public List<Goal> getGoalsByUserId(@Param("userId") Long userId);
	
		@Query(value = " select g.* from goal g,user u where g.createdBy = u.id "
				   	 + " and month(g.createdDate) = :month and year(g.createdDate) = :year "
				   	 + " and g.createdBy = :userId and g.goalAchieved = false and g.isActive =true " , nativeQuery = true)
		public List<Goal> getGoals(@Param("userId") Long userId,@Param("month") Integer month,@Param("year")Integer year);
		
		@Query("FROM Goal goal"
				+ " LEFT JOIN FETCH goal.goalType as type"
				+ " LEFT JOIN FETCH goal.frequencyType as frequencyType"
				+ " WHERE goal.id = :goalId AND goal.goalAchieved = false AND goal.isActive = true")
		public Goal getGoalById(@Param("goalId") Long goalId);
		
		@Query("FROM Goal goal"
				+ " LEFT JOIN FETCH goal.goalType as type"
				+ " LEFT JOIN FETCH goal.frequencyType as frequencyType"
				+ " WHERE goal.id = :goalId AND goal.goalAchieved = false AND goal.isActive = true AND goal.purpose = :purpose")
		public Goal getGoalByIdAndPurpose(@Param("goalId") Long goalId,@Param("purpose") Purpose pupose);

		@Query(value = " select count(*) as count,SUM(g.installmentAmount) as amount from goal g where g.createdBy = :userId and g.goalAchieved = false and g.isActive =true", nativeQuery = true )
		public List<Object[]> getTotalGoalsOfUser(@Param("userId")Long userId);
		
		@Query(value = "select id from goal g where g.createdBy = :userId and g.goalAchieved = false and g.isActive =true ", nativeQuery = true )
		public List<BigInteger> getGoalsIdsOfUser(@Param("userId")Long userId);

}
