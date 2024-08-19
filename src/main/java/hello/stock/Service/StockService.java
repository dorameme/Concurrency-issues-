package hello.stock.Service;

import hello.stock.domain.Stock;
import hello.stock.repository.StockRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

//    @Transactional
    public synchronized void decrease(Long id, Long quantity) {
        Stock stock = stockRepository.findById(id).orElseThrow();

        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }
}