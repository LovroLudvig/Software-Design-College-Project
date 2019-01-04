package hr.fer.handMadeShopBackend.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.handMadeShopBackend.Constants.Constants;
import hr.fer.handMadeShopBackend.domain.Advertisement;
import hr.fer.handMadeShopBackend.domain.User;
import hr.fer.handMadeShopBackend.service.AdService;
import hr.fer.handMadeShopBackend.service.DimensionService;
import hr.fer.handMadeShopBackend.service.StyleService;

@RestController
@RequestMapping("/advertisement")
@Lazy
public class AdConroller {

    @Autowired
    private  AdService adService;
    
    @Autowired
    private StyleService styleService;
    
    @Autowired 
    private DimensionService dimensionService;
    
    
    
    @PostMapping("/publish")
    @ResponseStatus(HttpStatus.CREATED)
    public Advertisement register(@RequestBody Advertisement advertisement) {
    	
    	advertisement.getDimensions()
    				 .stream()
    				 .forEach(dimension->{	
    					 dimensionService.save(dimension);
    				 });
    	
    	advertisement.getStyles()
    				 .stream()
    				 .forEach(style->{
    					 styleService.save(style);
    				 }); 
    	
        return adService.save(advertisement);
    }
	
	@GetMapping("/all")
	public List<Advertisement> getAllAds() {
	    return adService.fetchAll(); 
	}
}


