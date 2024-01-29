package com.gfa.tribesvibinandtribinotocyon.services;

import com.gfa.tribesvibinandtribinotocyon.dtos.StockpileDTO;
import com.gfa.tribesvibinandtribinotocyon.dtos.StockpileOverviewDto;
import com.gfa.tribesvibinandtribinotocyon.exceptions.TribesNotFoundException;
import com.gfa.tribesvibinandtribinotocyon.models.Kingdom;
import com.gfa.tribesvibinandtribinotocyon.models.Stockpile;
import com.gfa.tribesvibinandtribinotocyon.repositories.StockpileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockpileService {

    private final StockpileRepository stockpileRepository;

    public StockpileOverviewDto getStockpileOverviewByEmail(String email) throws TribesNotFoundException {
        Stockpile stockpile = stockpileRepository.findStockpileByEmail(email);
        if (stockpile != null) {
            return new StockpileOverviewDto(
                    stockpile.getFood(),
                    stockpile.getWood(),
                    stockpile.getStone(),
                    stockpile.getIron()
            );
        }
        throw new TribesNotFoundException();
    }

    public boolean consumeResources(Long stockpileId,
                                    Integer foodToConsume,
                                    Integer woodToConsume,
                                    Integer stoneToConsume,
                                    Integer ironToConsume) throws TribesNotFoundException {

        Stockpile stockpile = checkIfStockpileExists(stockpileId);
        if (hasEnoughResources(stockpile, foodToConsume, woodToConsume, stoneToConsume, ironToConsume)) {
            stockpile.setFood(stockpile.getFood() - foodToConsume);
            stockpile.setWood(stockpile.getWood() - woodToConsume);
            stockpile.setStone(stockpile.getStone() - stoneToConsume);
            stockpile.setIron(stockpile.getIron() - ironToConsume);
            stockpileRepository.save(stockpile);
            return true;
        }
        return false;
    }

    public boolean hasEnoughResources(Stockpile stockpile,
                                      Integer foodToConsume,
                                      Integer woodToConsume,
                                      Integer stoneToConsume,
                                      Integer ironToConsume) {
        return stockpile.getFood() >= foodToConsume && stockpile.getWood() >= woodToConsume && stockpile.getStone() >= stoneToConsume && stockpile.getIron() >= ironToConsume;
    }

    public void gainResources(Long stockpileId,
                              Integer gainedFood,
                              Integer gainedWood,
                              Integer gainedStone,
                              Integer gainedIron) throws TribesNotFoundException {

        Stockpile stockpile = checkIfStockpileExists(stockpileId);
        int currentCapacity = getCurrentStockpileCapacity(stockpileId);

        stockpile.setFood(Math.min(stockpile.getFood() + gainedFood, currentCapacity));
        stockpile.setWood(Math.min(stockpile.getWood() + gainedWood, currentCapacity));
        stockpile.setStone(Math.min(stockpile.getStone() + gainedStone, currentCapacity));
        stockpile.setIron(Math.min(stockpile.getIron() + gainedIron, currentCapacity));

        stockpile.setWood(stockpile.getWood() + gainedWood);
        stockpile.setStone(stockpile.getStone() + gainedStone);
        stockpile.setIron(stockpile.getIron() + gainedIron);
        stockpileRepository.save(stockpile);
    }

    public Stockpile checkIfStockpileExists(Long stockpileId) throws TribesNotFoundException {
        Optional<Stockpile> stockpileOpt = stockpileRepository.findById(stockpileId);
        if (stockpileOpt.isEmpty()) {
            throw new TribesNotFoundException(String.format("There is no stockpile with ID " + stockpileId));
        }
        return stockpileOpt.get();
    }

    public int getCurrentStockpileCapacity(Long stockpileId) throws TribesNotFoundException {
        Stockpile stockpile = checkIfStockpileExists(stockpileId);
        return BuildingLvlInfoService.getStockpileCapacityForLvl(stockpile.getStockpileLevel());
    }

    public StockpileDTO getStockpileOfKingdom(Kingdom kingdom) {
        return new StockpileDTO(stockpileRepository.findByKingdom(kingdom).get());
    }
}
