## 1) 섹션1 - 재고시스템 만들어보기
동시에 100개의 요청이온다면?

Race condition이 생긴다. 
두개이상의  스레드가 동시에 접근하여 갱신이 누락된 것이다. 두개이상의 스레드가 공유데이터에 액세스할수있고 동시에 변경하려고 할때 발생하는 문제를 레이스 컨디션이라고 한다. 
코드가 제대로 돌아가려면 하나의 스레드가작업을 완료한 이후에 갱신이 이루어져야한다.

## 2) synchronized
### 해결:
데이터에 한개의 스레드만 접근할 수 있어야한다.
synchronized 로 해결가능할까? -> 실패!
스프링에서 Transactional Annotation을 이용하면 클래스를 랩핑하여 새로 실행한다.
트랜잭션을 시작한다음에 종료하면 데이터베이스에 업데이트하는데
실제 데이터베이스가 업데이트되기전에 다른스레드가 끼어들 수 있다.
따라서 이전과 동일한 문제가 발생할 수있다.
따라서 @Transactional 어노테이션을 주석처리해야한다. -> 성공!

### 문제점:
하나의 프로세스 안에서만 보장된다. 서버가 2대 혹은 그이상은 적용할 수 없다.
각 프로세스 안에서만 보장되고 결국 여러스레드에서 동시에 데이터접근이 가능해 레이스컨디션이 발생한다.

## 2) database 
### 해결: 
1) Pessimistic Lock : 실제 데이터에 락을 걸어 점유하는 방식으로 정합성을 맞춘다.
    쿼리 insert - for update 구문을 사용한다!
    충돌이 빈번하면 Optimistic Lock보다 성능이 좋다.
    락을 통해 업데이터를 제어하기 때문에 데이터 정합성이 보장된다.
    별도로 락을 잡아서 성능 저하가 있을 수 있다.
2) Optimistic Lock  :
    
3) Named Lock :
