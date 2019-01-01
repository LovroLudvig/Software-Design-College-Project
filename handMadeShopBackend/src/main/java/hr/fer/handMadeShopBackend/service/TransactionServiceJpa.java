package hr.fer.handMadeShopBackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.fer.handMadeShopBackend.dao.TransactionRepository;
import hr.fer.handMadeShopBackend.domain.Transaction;

@Service
public class TransactionServiceJpa implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepo;

	@Override
	public List<Transaction> fetchAll() {
		return transactionRepo.findAll();
	}
}