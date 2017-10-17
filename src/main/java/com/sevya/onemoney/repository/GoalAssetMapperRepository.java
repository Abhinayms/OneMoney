package com.sevya.onemoney.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sevya.onemoney.model.GoalAssetMapper;

@Repository
public interface GoalAssetMapperRepository extends CrudRepository<GoalAssetMapper, Long> {
	
	@Query(" FROM GoalAssetMapper mapper"
		 + " LEFT JOIN FETCH mapper.goal AS goal"
		 + " LEFT JOIN FETCH mapper.asset AS asset"
		 + " WHERE goal.isActive = true AND goal.goalAchieved = false AND mapper.isActive = true "
		 + "AND goal.id = :goalId AND mapper.createdBy.id = :userId")
	public List<GoalAssetMapper> getGoalAssetMappersByGoalIdAndUserId(@Param("goalId") Long goalId,@Param("userId") Long userId);
	
	
	@Query(" FROM GoalAssetMapper mapper"
			 + " LEFT JOIN FETCH mapper.goal AS goal"
			 + " LEFT JOIN FETCH mapper.asset AS asset"
			 + " WHERE goal.isActive = true AND goal.goalAchieved = false AND mapper.isActive = true "
			 + "AND goal.id = :goalId AND asset.id = :assetId AND mapper.createdBy.id = :userId")
	public GoalAssetMapper getGoalAssetMappersByGoalIdAssetIdAndUserId(@Param("goalId") Long goalId,@Param("assetId") Long assetId,@Param("userId") Long userId);

	
}
