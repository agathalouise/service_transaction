package com.caju.service_transaction.repository;

import com.caju.service_transaction.entities.TransactionCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionCard, Long> {
}
