package com.kmakrutin.distributedlock.service;

import com.kmakrutin.distributedlock.model.Foo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncFooService {
    private final FooService fooService;
    private final RedissonClient redissonClient;

    @Value("${enable.distributed.lock:false}")
    private boolean enableDistributedLock;

    public Foo getFoo(int delay) throws InterruptedException {
        if (!enableDistributedLock) {
            return fooService.getFoo(delay);
        }

        RLock lock = redissonClient.getLock("fooServiceLock" + ":" + delay);
        boolean acquired = lock.tryLock(10, 30, TimeUnit.SECONDS);
        try {
            if (!acquired) {
                TimeUnit.SECONDS.sleep(5);
            }

            return fooService.getFoo(delay);
        } finally {
            if (acquired && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
