package com.projetoPicPaySimplificado.repositories;

import com.projetoPicPaySimplificado.domain.transactions.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
}
