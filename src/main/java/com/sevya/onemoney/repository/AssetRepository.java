package com.sevya.onemoney.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sevya.onemoney.model.Asset;

@Repository
public interface AssetRepository extends CrudRepository<Asset, Long> {
	
	@Query("FROM Asset asset WHERE asset.id = :assetId AND asset.isActive = true")
	public Asset getAssetById(@Param("assetId") Long assetId);

}
