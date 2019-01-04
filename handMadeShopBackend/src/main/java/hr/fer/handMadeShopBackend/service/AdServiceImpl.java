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
		if(advertisement.getDescription() == null) {
			throw new IllegalArgumentException("The description must exist");
		}
		if(advertisement.getDimensions() == null) {
			throw new IllegalArgumentException("The dimensions must exist");

		}
		if(advertisement.getName() == null) {
			throw new IllegalArgumentException("The name must exist");
		}
		if(advertisement.getPrice()==null) {
			throw new IllegalArgumentException("The price must exist");
		}
		if(advertisement.getStyles() == null) {
			throw new IllegalArgumentException("The styles must exist");
		}
		if(advertisement.getSpecification() == null) {
			throw new IllegalArgumentException("The specification must exist");
		}
        return adRepo.save(advertisement);
	}
}
