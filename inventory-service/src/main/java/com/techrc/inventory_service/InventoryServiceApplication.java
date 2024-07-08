package com.techrc.inventory_service;

import com.techrc.inventory_service.model.Inventory;
import com.techrc.inventory_service.repositories.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}
	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository){
		return args -> {
			Inventory inventory = new Inventory();
			inventory.setSkuCode("IQOO Neo 6");
			inventory.setQuantity(10);

			Inventory inventory1 = new Inventory();
			inventory1.setSkuCode("IQOO Neo 6 Blue");
			inventory1.setQuantity(5);
			inventoryRepository.save(inventory);
			inventoryRepository.save(inventory1);
		};
	}

}
