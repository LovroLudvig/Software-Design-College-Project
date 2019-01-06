package hr.fer.handMadeShopBackend.service;

import java.util.List;

import hr.fer.handMadeShopBackend.domain.Advertisement;

public interface AdService {

	Advertisement fetch(Long advertisementId);
	List<Advertisement> fetchAll();
	Advertisement save(Advertisement advertisement);
}
