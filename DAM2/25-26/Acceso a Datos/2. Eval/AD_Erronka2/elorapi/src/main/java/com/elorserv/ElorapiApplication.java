package com.elorserv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.elorserv.interfaces")
@EntityScan(basePackages = "com.elorserv.model")
public class ElorapiApplication {

	public static void main(String[] args) {
		//AQUI HAY QUE INICIALIZAR SPRINGBOOT, SOCKET, HILOS, HIBERNATE Y TODO
		SpringApplication.run(ElorapiApplication.class, args);
	}

}
