package com.caju.service_transaction.repository;

import com.caju.service_transaction.entities.Wallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {


    //L4 - garante que apenas um acesso por conta seja permitido por vez.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w JOIN w.account a WHERE a.id = :accountId")
    Optional<Wallet> findWalletForUpdate(@Param("accountId") Long accountId);
}
