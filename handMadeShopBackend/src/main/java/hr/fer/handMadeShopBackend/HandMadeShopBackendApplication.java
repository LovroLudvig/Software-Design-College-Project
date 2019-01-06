package hr.fer.handMadeShopBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;


@SpringBootApplication
public class HandMadeShopBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(HandMadeShopBackendApplication.class, args);
	}
}
