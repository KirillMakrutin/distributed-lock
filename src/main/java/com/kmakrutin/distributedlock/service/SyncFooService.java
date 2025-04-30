package com.kmakrutin.distributedlock.service;

import com.kmakrutin.distributedlock.model.Foo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncFooService {
    private final FooService fooService;
    private final RedissonClient redissonClient;

    private static final String LOCK_NAME = "fooServiceLock";

    public List<Foo> getFoos() throws InterruptedException {

        RLock lock = redissonClient.getLock(LOCK_NAME);
        boolean acquired = lock.tryLock(10, 30, TimeUnit.SECONDS);
        try {
            if (!acquired) {
                TimeUnit.SECONDS.sleep(5);
            }

            return fooService.getFoos();
        } finally {
            if (acquired && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
