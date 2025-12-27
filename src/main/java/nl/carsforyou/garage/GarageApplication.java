package nl.carsforyou.garage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GarageApplication {

	public static void main(String[] args) {
		SpringApplication.run(GarageApplication.class, args);
	}

}

//notes
// Controller -> talks to service
// Service -> talks to repository
// Repository -> returns entities
// Mapper -> converts entities to DTOs
    //Request DTOs model the data that the client is allowed to send
    //Response DTOs model the data that the client is allowed to see
// Controller -> returns DTOs to client like Postman, Swagger, Frontend
