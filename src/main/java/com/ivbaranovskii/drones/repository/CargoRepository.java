package com.ivbaranovskii.drones.repository;

import com.ivbaranovskii.drones.entity.Cargo;
import com.ivbaranovskii.drones.entity.DroneMedicineKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CargoRepository extends JpaRepository<Cargo, DroneMedicineKey> {

    List<Cargo> findByDroneId(Long droneId);

    @Query("SELECT COALESCE(SUM(m.weight), 0) FROM Cargo c JOIN c.medicine m WHERE c.drone.id = :droneId")
    Double findSumOfMedicineWeightForDrone(@Param("droneId") Long droneId);

}
