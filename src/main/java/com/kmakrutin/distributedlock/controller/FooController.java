package com.kmakrutin.distributedlock.controller;

import com.kmakrutin.distributedlock.model.Foo;
import com.kmakrutin.distributedlock.service.FooService;
import com.kmakrutin.distributedlock.service.SyncFooService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/foo")
public class FooController {
    private final SyncFooService syncFooService;
    private final FooService fooService;

    @GetMapping
    public List<Foo> getFoos() throws InterruptedException {
        return syncFooService.getFoos();
    }

    @PostMapping("/evict")
    public void evictCache() {
        fooService.evictFoosCache();
    }
}
