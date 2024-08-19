package hello.stock.facade;

import hello.stock.Service.StockService;
import hello.stock.repository.RedisLockRepository;
import org.springframework.stereotype.Component;

@Component
public class LettuceLockFacade {

    private final RedisLockRepository repository;
    private final StockService stockService;

    public LettuceLockFacade(RedisLockRepository repository, StockService stockService) {
        this.repository = repository;
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (!repository.lock(id)) {
            Thread.sleep(100);//락획득을 실패하면... 재시도!
        }
        try {
            stockService.decrease(id, quantity);
        } finally {
            repository.unLock(id);
        }
    }
}
