package com.commsignia.backend.vehicle.repository;

import java.util.List;

import com.commsignia.backend.vehicle.entity.VehicleEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends ListCrudRepository<VehicleEntity, Long> {

	@Query("""
				select v from VehicleEntity v 
				where st_distancesphere(st_point(:longitude, :latitude), st_point(v.longitude, v.latitude)) < :radius
			""")
	List<VehicleEntity> getVehiclesInRadius(
			@Param("latitude") Double latitude,
			@Param("longitude") Double longitude,
			@Param("radius") Long radius);
}
