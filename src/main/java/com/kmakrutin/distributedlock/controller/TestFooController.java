package com.kmakrutin.distributedlock.controller;

import com.kmakrutin.distributedlock.model.Foo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/test-foo")
public class TestFooController {
    @Value("${server.port:8080}")
    private int serverPort;

    @GetMapping("/{delay}")
    public List<Foo> runParallelFoos(@PathVariable int delay) throws ExecutionException, InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(20);

        try {
            RestTemplate restTemplate = new RestTemplate();

            final List<CompletableFuture<Foo>> futures = IntStream.range(0, 20)
                    .mapToObj(i -> CompletableFuture.supplyAsync(() -> restTemplate.getForEntity("http://localhost:" + serverPort + "/foo/delayed/" + delay, Foo.class).getBody(), executorService))
                    .toList();

            List<Foo> allFoos = new ArrayList<>();
            for (CompletableFuture<Foo> future : futures) {
                allFoos.add(future.get());
            }

            return allFoos;
        } finally {
            executorService.shutdown();
        }
    }
}
