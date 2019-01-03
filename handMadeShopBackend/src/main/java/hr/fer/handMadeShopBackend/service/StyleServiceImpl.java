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
		if(style.getPrice()==null) {
			throw new IllegalArgumentException("The price must exist");
		} 
		if(style.getDescription()==null) {
			throw new IllegalArgumentException("The description must exist");
		}
		return styleRepo.save(style);
	}

}
