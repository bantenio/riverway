package com.yomahub.liteflow.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoanService {
    private static final Logger log = LoggerFactory.getLogger(LoanService.class);

    public String loan(String name) {
        return "LoanService.loan: " + name;
    }
}
