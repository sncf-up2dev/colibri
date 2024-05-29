package fr.sncf.d2d.colibri;

import fr.sncf.d2d.colibri.domain.common.Service;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@ComponentScan(includeFilters = @ComponentScan.Filter(Service.class))
@EnableAspectJAutoProxy
public class ColibriApplication {

	public static void main(String[] args) {
		SpringApplication.run(ColibriApplication.class, args);
	}

}
