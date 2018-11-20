package com.ctoboost.cassandra.whoop;

import com.ctoboost.cassandra.whoop.token.TokenBalanceValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
public class WhoopApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhoopApplication.class, args);
        TokenBalanceValidator validator = new TokenBalanceValidator();
        validator.run();
	}

}
