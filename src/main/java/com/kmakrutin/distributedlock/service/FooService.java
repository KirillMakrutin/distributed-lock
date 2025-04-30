package com.kmakrutin.distributedlock.service;

import com.kmakrutin.distributedlock.model.Foo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class FooService {

    @Cacheable("foos")
    public List<Foo> getFoos() throws InterruptedException {
        log.info(">>> Getting foos");

        Thread.sleep(2000); // Emulate delay of 2 seconds

        return Arrays.asList(
                new Foo(1, "foo-data-1"),
                new Foo(2, "foo-data-2"),
                new Foo(3, "foo-data-3")
        );
    }

    @CacheEvict(value = "foos", allEntries = true)
    public void evictFoosCache() {
        // no-op
    }
}
