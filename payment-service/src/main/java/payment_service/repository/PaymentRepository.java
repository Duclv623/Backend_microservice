package payment_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import payment_service.entity.Payment;
import payment_service.enums.PaymentStatus;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByOrderIdOrderByCreatedAtDesc(Long orderId);

    Optional<Payment> findByTransactionCode(String transactionCode);

    boolean existsByOrderIdAndStatus(Long orderId, PaymentStatus status);
}
