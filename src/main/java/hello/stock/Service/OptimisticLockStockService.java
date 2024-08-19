package hello.stock.Service;

import hello.stock.domain.Stock;
import hello.stock.repository.StockRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class OptimisticLockStockService {
 private final StockRepository stockRepository;

    public OptimisticLockStockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }
    @Transactional
    public void decrease(Long id, Long quantity){
        Stock stock = stockRepository.findByIdWithOptimisticLock(id);
        stock.decrease(quantity);
        stockRepository.save(stock);
    }
}
