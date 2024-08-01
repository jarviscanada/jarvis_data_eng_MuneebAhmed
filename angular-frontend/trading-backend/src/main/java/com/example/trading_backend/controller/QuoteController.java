package com.example.trading_backend.controller;

import com.example.trading_backend.model.Quote;
import com.example.trading_backend.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quotes")
public class QuoteController {
    @Autowired
    private QuoteService quoteService;

    @GetMapping("/dailyList")
    public List<Quote> getAllQuotes() {
        return quoteService.getAllQuotes();
    }

    @PostMapping
    public Quote createQuote(@RequestBody Quote quote) {
        return quoteService.saveQuote(quote);
    }
}