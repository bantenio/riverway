package com.yomahub.liteflow.example.service;

import com.yomahub.liteflow.example.config.IsolationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LoanService {
    private static final Logger log = LoggerFactory.getLogger(LoanService.class);

    @Autowired
    private IsolationConfig config;

    public String loan(String name) {
        return "LoanService.loan: " + config.getUrl();
    }
}
