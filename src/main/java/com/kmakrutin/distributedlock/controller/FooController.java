package com.kmakrutin.distributedlock.controller;

import com.kmakrutin.distributedlock.model.Foo;
import com.kmakrutin.distributedlock.service.FooService;
import com.kmakrutin.distributedlock.service.SyncFooService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/foo")
public class FooController {
    private final SyncFooService syncFooService;
    private final FooService fooService;

    @GetMapping("/delayed/{seconds}")
    public Foo getFoo(@PathVariable(name = "seconds") int delay) throws InterruptedException {
        return syncFooService.getFoo(delay);
    }

    @PostMapping("/evict")
    public void evictCache() {
        fooService.evictFoosCache();
    }
}
