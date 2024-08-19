package hello.stock.service;

import hello.stock.Service.StockService;
import hello.stock.domain.Stock;
import hello.stock.repository.StockRepository;
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
        Assertions.assertThat(stockRepository.findById(1L).orElseThrow().getQuantity()).isEqualTo(99);
    }
}
