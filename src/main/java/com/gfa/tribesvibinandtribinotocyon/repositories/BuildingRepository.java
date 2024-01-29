package com.gfa.tribesvibinandtribinotocyon.repositories;

import com.gfa.tribesvibinandtribinotocyon.models.Building;
import com.gfa.tribesvibinandtribinotocyon.models.Kingdom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuildingRepository extends JpaRepository<Building, Long> {

    public List<Building> findAllByKingdom(Kingdom kingdom);
}