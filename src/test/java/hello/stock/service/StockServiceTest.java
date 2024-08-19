package hello.stock.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import hello.stock.Service.StockService;
import hello.stock.domain.Stock;
import hello.stock.repository.StockRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StockServiceTest {
    @Autowired
    private StockService stockService;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void before(){
        stockRepository.saveAndFlush(new Stock(1L,100L));
    }

    @AfterEach
    public void after(){
        stockRepository.deleteAll();
    }

    @Test
    public void 재고감소(){
        stockService.decrease(1L,1L);
        assertThat(stockRepository.findById(1L).orElseThrow().getQuantity()).isEqualTo(99);
    }

    @Test
    public void 동시에_요청_100개() throws InterruptedException {
      int threadCount= 100;
      ExecutorService executorService= Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decrease(1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        Stock stock = stockRepository.findById(1L).orElseThrow();
        assertEquals(0,stock.getQuantity());
    }
}
