package com.gfa.tribesvibinandtribinotocyon.services;

import com.gfa.tribesvibinandtribinotocyon.dtos.BuildingDTO;
import com.gfa.tribesvibinandtribinotocyon.models.Building;
import com.gfa.tribesvibinandtribinotocyon.models.Kingdom;
import com.gfa.tribesvibinandtribinotocyon.repositories.BuildingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuildingService {

    private final BuildingRepository buildingRepository;

    public void actualUpgradeBuilding(Long userId, String buildingName) {
    }

    public List<BuildingDTO> getBuildingList(Kingdom kingdom){
        return buildingRepository.findAllByKingdom(kingdom).stream().map(BuildingDTO::new).toList();
    }
}