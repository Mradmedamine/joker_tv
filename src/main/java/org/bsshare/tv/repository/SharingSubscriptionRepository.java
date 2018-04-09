package org.bsshare.tv.repository;

import java.util.List;

import org.bsshare.tv.model.entity.SharingSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SharingSubscriptionRepository
		extends JpaRepository<SharingSubscription, Long>, BaseSubscriptionRepository {//

	// @Query("SELECT e FROM SharingSubscription e WHERE e.activeCode = :activeCode
	// AND e.device.macAddress = :macAddress "
	// + "AND e.device.serialNumber = :serialNumber")
	@Query("SELECT e FROM SharingSubscription e WHERE e.activeCode = :activeCode")
	List<SharingSubscription> findOneByCriteria(@Param("activeCode") String activeCode);

}
