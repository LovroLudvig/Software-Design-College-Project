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
		
		if(dimension == null) {
			throw new IllegalArgumentException("Dimension cannot be null");
		}
		if(dimension.getDescription() == null) {
			throw new IllegalArgumentException("Dimension must have a description");
		}
		return dimensionRepo.save(dimension);
	}

}
