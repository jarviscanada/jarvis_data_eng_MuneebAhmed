package com.example.trading_backend.service;

import com.example.trading_backend.model.Trader;
import com.example.trading_backend.repository.TraderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TraderService {
    @Autowired
    private TraderRepository traderRepository;

    public List<Trader> getAllTraders() {
        return traderRepository.findAll();
    }

    public Optional<Trader> getTraderById(Long id) {
        return traderRepository.findById(id);
    }

    public Trader saveTrader(Trader trader) {
        return traderRepository.save(trader);
    }

    public void deleteTrader(Long id) {
        traderRepository.deleteById(id);
    }
}