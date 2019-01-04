package hr.fer.handMadeShopBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.fer.handMadeShopBackend.dao.StyleRepository;
import hr.fer.handMadeShopBackend.domain.Style;

@Service
public class StyleServiceImpl implements StyleService {

	@Autowired
	private StyleRepository styleRepo;
	
	@Override
	public Style save(Style style) {
		
		if(style == null) {
			throw new IllegalArgumentException("Style cannot be null");
		}
		if(style.getPrice()==null) {
			throw new IllegalArgumentException("Styles must have a price");
		} 
		if(style.getDescription()==null) {
			throw new IllegalArgumentException("Styles must have a description");
		}
		return styleRepo.save(style);
	}

}
