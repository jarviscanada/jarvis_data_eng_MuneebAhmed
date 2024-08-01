package com.example.trading_backend.service;

import com.example.trading_backend.model.Quote;
import com.example.trading_backend.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuoteService {
    @Autowired
    private QuoteRepository quoteRepository;

    public List<Quote> getAllQuotes() {
        return quoteRepository.findAll();
    }

    public Quote saveQuote(Quote quote) {
        return quoteRepository.save(quote);
    }
}