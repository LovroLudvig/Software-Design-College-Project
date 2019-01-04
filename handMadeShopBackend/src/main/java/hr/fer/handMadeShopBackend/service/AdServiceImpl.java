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
		if(advertisement==null) {
			throw new IllegalArgumentException("An advertisement must be provided");
		}
		if(advertisement.getDescription() == null) {
			throw new IllegalArgumentException("Advertisement must have a description");
		}
		if(advertisement.getDimensions() == null || advertisement.getDimensions().size() == 0) {
			throw new IllegalArgumentException("At least one dimension must exist");
		}
		if(advertisement.getName() == null) {
			throw new IllegalArgumentException("Advertisement must have a name");
		}
		if(advertisement.getPrice() == null) {
			throw new IllegalArgumentException("Advertisement must have a price");
		}
		if(advertisement.getStyles() == null || advertisement.getStyles().size() == 0) {
			throw new IllegalArgumentException("At least one style must exist");
		}
		if(advertisement.getSpecification() == null) {
			throw new IllegalArgumentException("Advertisement must have a specification");
		}
        return adRepo.save(advertisement);
	}
}
