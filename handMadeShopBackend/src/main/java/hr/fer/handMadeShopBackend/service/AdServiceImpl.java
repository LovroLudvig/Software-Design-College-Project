package hr.fer.handMadeShopBackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.fer.handMadeShopBackend.dao.AdvertisementRepository;
import hr.fer.handMadeShopBackend.domain.Advertisement;

@Service
public class AdServiceImpl implements AdService{
	
	@Autowired
    private AdvertisementRepository adRepo;

	@Override
	public List<Advertisement> fetchAll() {
		return adRepo.findAll();
	}

	@Override
	public Advertisement save(Advertisement advertisement) { 
        return adRepo.save(advertisement);
	}
	


}
