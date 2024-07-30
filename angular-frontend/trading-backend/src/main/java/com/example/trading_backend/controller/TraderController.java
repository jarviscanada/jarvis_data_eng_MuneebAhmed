package com.example.trading_backend.controller;

import com.example.trading_backend.model.Trader;
import com.example.trading_backend.service.TraderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/traders")
public class TraderController {
    @Autowired
    private TraderService traderService;

    @GetMapping
    public List<Trader> getAllTraders() {
        return traderService.getAllTraders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trader> getTraderById(@PathVariable Long id) {
        Optional<Trader> trader = traderService.getTraderById(id);
        return trader.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Trader createTrader(@RequestBody Trader trader) {
        return traderService.saveTrader(trader);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrader(@PathVariable Long id) {
        traderService.deleteTrader(id);
        return ResponseEntity.noContent().build();
    }
}