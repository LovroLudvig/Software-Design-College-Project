package hr.fer.handMadeShopBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.fer.handMadeShopBackend.dao.DimensionRepository;
import hr.fer.handMadeShopBackend.domain.Dimension;

@Service
public class DimensionServiceImpl implements DimensionService{
	
	@Autowired
	private DimensionRepository dimensionRepo;
	
	@Override
	public Dimension save(Dimension dimension) {
		if(dimension.getDescription() == null) {
			throw new IllegalArgumentException("The dimension must exist");
		}
		return dimensionRepo.save(dimension);
	}

}
