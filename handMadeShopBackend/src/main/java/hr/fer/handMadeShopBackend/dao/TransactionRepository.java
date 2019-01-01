package hr.fer.handMadeShopBackend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.fer.handMadeShopBackend.domain.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}