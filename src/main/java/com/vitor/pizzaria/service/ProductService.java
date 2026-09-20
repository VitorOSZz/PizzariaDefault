package com.vitor.pizzaria.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ThreadPoolTaskExecutor executor;

    public ProductService(@Qualifier("productExecutor") ThreadPoolTaskExecutor executor) {
        this.executor = executor;
    }

}
