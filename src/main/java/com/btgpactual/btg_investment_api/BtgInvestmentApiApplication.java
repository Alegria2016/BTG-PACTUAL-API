package com.btgpactual.btg_investment_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.btgpactual.btg_investment_api.model.Fund;
import com.btgpactual.btg_investment_api.repository.FundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BtgInvestmentApiApplication {

	@Autowired
	private FundRepository fundRepository;

	public static void main(String[] args) {
		SpringApplication.run(BtgInvestmentApiApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData() {
		return args -> {
			// Inicializar fondos predefinidos si no existen
			if (fundRepository.count() == 0) {
				fundRepository.save(new Fund("FPV_BTG_PACTUAL_RECAUDADORA", 75000.0, "FPV"));
				fundRepository.save(new Fund("FPV_BTG_PACTUAL_ECOPETROL", 125000.0, "FPV"));
				fundRepository.save(new Fund("DEUDAPRIVADA", 50000.0, "FIC"));
				fundRepository.save(new Fund("FDO-ACCIONES", 250000.0, "FIC"));
				fundRepository.save(new Fund("FPV_BTG_PACTUAL_DINAMICA", 100000.0, "FPV"));

				System.out.println("Datos iniciales de fondos creados exitosamente");
			}
		};
	}
}
