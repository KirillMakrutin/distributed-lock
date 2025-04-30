package com.kmakrutin.distributedlock.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/test-foo")
public class TestFooController {
    @Value("${server.port:8080}")
    private int serverPort;

    @SuppressWarnings("unchecked")
    @GetMapping
    public List<Object> runParallelFoos() throws InterruptedException, ExecutionException {
        RestTemplate restTemplate = new RestTemplate();

        List<CompletableFuture<List<Object>>> futures = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            futures.add(CompletableFuture.supplyAsync(() -> restTemplate.getForEntity("http://localhost:" + serverPort + "/foo", List.class).getBody()));
        }

        List<Object> results = new ArrayList<>();
        for (CompletableFuture<List<Object>> future : futures) {
            results.add(future.get());
        }
        return results;
    }
}
