
# 재고 시스템 동시성 문제 해결 방법

## 1. Race Condition 문제

동시에 여러 개의 요청이 들어올 때, **Race Condition**이 발생할 수 있다. 두 개 이상의 스레드가 동시에 접근하여 데이터를 갱신하려고 할 때, 특정 갱신이 누락되는 상황을 의미한다. 이를 방지하기 위해서는 하나의 스레드가 작업을 완료한 후에만 데이터가 갱신되도록 해야 한다.

## 2. Synchronized 키워드

### 해결 방법:
하나의 데이터에 한 번에 한 개의 스레드만 접근할 수 있도록 **synchronized** 키워드를 사용하여 해결할 수 있다. 그러나 이 방법은 다음과 같은 문제가 있다:

- **문제점**: Synchronized는 하나의 프로세스 안에서만 동시성을 보장한다. 만약 서버가 두 대 이상이라면, 각 프로세스 안에서만 동시성이 보장되기 때문에 여전히 Race Condition이 발생할 수 있다.

## 3. 데이터베이스를 활용한 동시성 제어

### 1) Pessimistic Lock

**Pessimistic Lock**은 실제 데이터에 락을 걸어 점유하는 방식으로, 정합성을 보장하는 방법이다.

- **특징**:
    - `FOR UPDATE` 구문을 사용하여 데이터베이스 레벨에서 락을 건다.
    - 충돌이 빈번한 경우, Optimistic Lock보다 성능이 좋을 수 있다.
    - 별도의 락을 잡기 때문에 성능 저하가 발생할 수 있다.

### 2) Optimistic Lock

**Optimistic Lock**은 실제로 락을 사용하지 않고, **version** 필드를 이용해 정합성을 유지하는 방법이다.

- **특징**:
    - 별도의 락을 잡지 않으므로 Pessimistic Lock보다 성능상의 이점이 있다.
    - 충돌이 발생할 경우, JPA에서 기본적인 충돌 관리를 자동으로 처리해주며, 재시도 로직을 직접 작성할 필요가 거의 없다.
    - 충돌이 빈번하지 않은 경우에 더 나은 성능을 발휘한다.

### 3) Named Lock

**Named Lock**은 별도의 공간에 이름을 붙여 락을 거는 메타데이터 락이다.

- **특징**:
    - 트랜잭션이 종료될 때 자동으로 해제되지 않으며, 해제를 직접 수행하거나 선점 시간이 지나길 기다려야 한다.
    - 주로 분산 락을 구현할 때 사용하며, 타임아웃도 손쉽게 구현할 수 있다.
    - 데이터 삽입 시에도 사용할 수 있으나, 트랜잭션 세션 관리를 잘해줘야 하므로 복잡할 수 있다.
  

## 4. Lettuce vs Redis 
### Lettuce
1) setnx 명령어를 활용하여 분산락 구현
2) spin lock 방식 -> 자원 낭비가 심하다.
3) 구현이 간단하다.

### Redis
1) pub-sub 기반으로 lock구현 제공 
2)  Named Lock과 유사하지만 세션관리에 신경쓸 필요가 없다는 점이 다름.