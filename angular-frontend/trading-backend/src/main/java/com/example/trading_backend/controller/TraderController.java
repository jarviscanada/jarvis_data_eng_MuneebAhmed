package com.example.trading_backend.controller;

import com.example.trading_backend.model.Trader;
import com.example.trading_backend.service.TraderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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

    @PutMapping("/{id}")
    public ResponseEntity<Trader> updateTrader(@PathVariable Long id, @RequestBody Trader updatedTrader) {
        Optional<Trader> trader = traderService.getTraderById(id);
        if (trader.isPresent()) {
            updatedTrader.setId(id);
            return ResponseEntity.ok(traderService.saveTrader(updatedTrader));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<Trader> depositFunds(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        Optional<Trader> trader = traderService.getTraderById(id);
        if (trader.isPresent()) {
            Trader existingTrader = trader.get();
            existingTrader.setAmount(existingTrader.getAmount() + request.get("amount"));
            return ResponseEntity.ok(traderService.saveTrader(existingTrader));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<Trader> withdrawFunds(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        Optional<Trader> trader = traderService.getTraderById(id);
        if (trader.isPresent()) {
            Trader existingTrader = trader.get();
            existingTrader.setAmount(existingTrader.getAmount() - request.get("amount"));
            return ResponseEntity.ok(traderService.saveTrader(existingTrader));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrader(@PathVariable Long id) {
        traderService.deleteTrader(id);
        return ResponseEntity.noContent().build();
    }
}