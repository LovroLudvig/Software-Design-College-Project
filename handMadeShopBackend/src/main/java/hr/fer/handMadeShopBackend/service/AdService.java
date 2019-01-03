package hr.fer.handMadeShopBackend.service;

import java.util.List;

import hr.fer.handMadeShopBackend.domain.Advertisement;

public interface AdService {
	List<Advertisement> fetchAll();
	Advertisement save(Advertisement advertisement);
}
