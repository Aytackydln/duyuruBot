package com.noname.duyuru.configuration;

import com.noname.duyuru.RestTemplateConfig;
import com.noname.duyuru.app.configuration.TelegramClientConfig;
import com.noname.duyuru.app.setting.ConfigurationSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        TelegramClientConfig.class, RestTemplateConfig.class, TelegramConfigTest.TestService.class
})
public class TelegramConfigTest {

    @MockBean
    ConfigurationSet configurationSet;
    private static final AtomicInteger runCount = new AtomicInteger(0);

    @Service
    static class TestService {
        private static final Semaphore semaphore = new Semaphore(0);

        @Async(TelegramClientConfig.LIMITED_COMMAND_SENDER)
        void waitIndefinetely() {
            runCount.incrementAndGet();

            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                System.out.println("This should not have happened!!!");
            }
        }

        void stop() {
            semaphore.release(Integer.MAX_VALUE);
        }
    }

    @Autowired
    TelegramClientConfig telegramClientConfig;

    @Autowired
    TestService testService;

    @Autowired
    ThreadPoolTaskExecutor telegramLimitedCommandSender;

    @Test
    @Timeout(value = 5)
    public void fullMessageQueueWaits() throws InterruptedException {
        //given
        TelegramClientConfig.maxCommandPerSecond = Integer.MAX_VALUE; //needed, also makes test faster

        System.out.println("running tasks at capacity");
        //Executing task is not in the queue so we run +1 time
        for (int i = 0; i < telegramClientConfig.MAX_MESSAGE_QUEUE + 1; i++) {
            testService.waitIndefinetely();
        }
        while (telegramLimitedCommandSender.getActiveCount() < 1)
            Thread.yield();
        Thread.sleep(1);

        System.out.println("checking task counts");
        assertEquals(1, runCount.get(), "First task did not start yet");

        System.out.println("starting waiting thread");
        Thread waitingThread = new Thread(() -> testService.waitIndefinetely());
        waitingThread.start();
        Thread.yield();
        Thread.sleep(1);   //hard coded wait, is this ok?

        assertTrue(waitingThread.isAlive(), "Task after maximum capacity does not wait.");

        System.out.println("Testing run count before releasing the lock");
        assertEquals(1, runCount.get());

        System.out.println("stopping tasks");
        testService.stop();

        System.out.println("Waiting for 'waiting thread'");
        waitingThread.join(100);
        assertFalse(waitingThread.isAlive(), "All tasks did not finish for test to be correct");
        System.out.println("Waiting ended");

        //telegramLimitedCommandSender.shutdown();
        while (telegramLimitedCommandSender.getActiveCount() > 0) {
            telegramLimitedCommandSender.getThreadPoolExecutor().awaitTermination(2, TimeUnit.SECONDS);
        }
        //+1 is to reach the maximum queue capacity.
        //+1 is for thread that is expected to be blocked.
        assertEquals(telegramClientConfig.MAX_MESSAGE_QUEUE + 2, runCount.get());
    }
}
