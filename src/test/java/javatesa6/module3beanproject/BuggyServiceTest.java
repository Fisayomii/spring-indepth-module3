package javatesa6.module3beanproject;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class BuggyServiceTest {

//    @Autowired
    private BuggySettlementService service;

    @Autowired
    private PaymentRepo repository;

    @Test
    void proveTheBug_rollbackFails() {
        repository.deleteAll();
        List<String> batch = List.of("Payment-1", "Force-Failure-Payment");

        // The batch throws the expected exception
        assertThatThrownBy(() -> service.recordBatch(batch))
                .isInstanceOf(RuntimeException.class);

        long count = repository.count();
        System.out.println(">>> [BUGGY APP] Records left in database: " + count);

        // 💥 THIS WILL CRASH RED: Expecting 0, but self-invocation saved Payment-1 (count is 1)
        assertThat(count).isEqualTo(0);
    }
}

@Entity
class PaymentRecord {
    @Id private String id;
    public PaymentRecord() {}
    public PaymentRecord(String id) { this.id = id; }
}

@Repository
interface PaymentRepo extends JpaRepository<PaymentRecord, String> {}

@SpringBootApplication
class BuggyAppConfig {}

@Service
class BuggySettlementService {
    @Autowired private PaymentRepo repo;

    @Transactional
    public void recordBatch(List<String> payments) {
        for (String p : payments) {
            this.record(p); //  Self-invocation bypasses proxy!
        }
    }

    @Transactional
    public void record(String paymentId) {
        repo.save(new PaymentRecord(paymentId));
        if (paymentId.contains("Force-Failure")) {
            throw new RuntimeException("Ledger rejection");
        }
    }
}
