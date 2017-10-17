package com.sevya.onemoney.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sevya.onemoney.model.GoalAccountMapper;

@Repository
public interface GoalAccountMapperRepository extends CrudRepository<GoalAccountMapper, Long> {  
	
	@Query(value = " select goalId,accountId from goalAccountMapper g,user u where"
				 + " g.createdBy = u.id and g.goalAchieved = false and u.id = :userId ",nativeQuery=true)
	public List<Object[]> getGoalAccountMappersByUserId(@Param("userId") Long userId);
	
	@Query(value = "select a.accountCode,a.purpose from goalAccountMapper gam,goal g,account a, user u where gam.accountId = a.id and"
			 + " gam.createdBy = u.id and u.id = :userId and gam.goalId = g.id and g.id = :goalId and g.goalAchieved = false and a.isActive=true and g.isActive = true",nativeQuery=true)
	public List<Object[]> getAccountCodesByUserIdAndGoalId(@Param("userId") Long userId,@Param("goalId") Long goalId);
	
	@Query(" FROM GoalAccountMapper mapper"
		 + " LEFT JOIN FETCH mapper.goal AS goal"
		 + " LEFT JOIN FETCH mapper.account AS account"
		 + " WHERE goal.isActive = true AND goal.goalAchieved = false AND mapper.isActive = true"
		 + " AND goal.id = :goalId AND mapper.createdBy.id = :userId AND account.isActive = true")
	public List<GoalAccountMapper> getGoalAccountMappersByGoalIdAndUserId(@Param("goalId") Long goalId,
			@Param("userId") Long userId);

	@Query(" FROM GoalAccountMapper mapper"
		 + " LEFT JOIN FETCH mapper.goal AS goal"
		 + " LEFT JOIN FETCH mapper.account AS account"
		 + " WHERE goal.isActive = true AND goal.goalAchieved = false AND mapper.isActive = true"
		 + " AND goal.id = :goalId AND account.id = :accountId AND mapper.createdBy.id = :userId AND account.isActive = true")
	public GoalAccountMapper getGoalAccountMappersByGoalIdAccountIdAndUserId(@Param("goalId") Long goalId,
			@Param("accountId") Long accountId,@Param("userId") Long userId);

	@Query(" FROM GoalAccountMapper mapper"
		 + " LEFT JOIN FETCH mapper.goal AS goal"
		 + " LEFT JOIN FETCH mapper.account AS account"
		 + " WHERE goal.isActive = true AND goal.goalAchieved = false AND mapper.isActive = true"
		 + " AND goal.id = :goalId AND account.accountCode = :accountCode"
		 + " AND mapper.createdBy.id = :userId AND account.isActive = true")
	public GoalAccountMapper getGoalAccountMapperByGoalIdAccountCodeAndUserId(@Param("goalId") Long goalId,
			@Param("accountCode") String accountCode,@Param("userId") Long userId);
	
	

}
