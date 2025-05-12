package com.kmakrutin.distributedlock.service;

import com.kmakrutin.distributedlock.model.Foo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class FooService {

    @Cacheable(value = "foo", key = "#delay")
    public Foo getFoo(int delay) throws InterruptedException {
        final String id = UUID.randomUUID().toString();

        log.info(">>> Getting foo with id {} and {} seconds delay", id, delay);

        TimeUnit.SECONDS.sleep(delay); // Emulate delay

        return new Foo(id, "foo-with-delay-" + delay);
    }

    @CacheEvict(value = "foo", allEntries = true)
    public void evictFoosCache() {
        // no-op
    }
}
