package hr.fer.handMadeShopBackend.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.handMadeShopBackend.domain.Transaction;
import hr.fer.handMadeShopBackend.service.TransactionService;

@RestController
@RequestMapping("/transactions")
@Lazy
public class TransactionsController {
	
	@Autowired
    private TransactionService service;
	
	@GetMapping("/all")
    public List<Transaction> getAll() {
        return service.fetchAll();
    }
}
