package hr.fer.handMadeShopBackend.service;

import java.util.List;

import hr.fer.handMadeShopBackend.domain.Transaction;

public interface TransactionService {
	List<Transaction> fetchAll();
}
