package com.ctoboost.cassandra.whoop;

import com.ctoboost.cassandra.whoop.key.WhoopKeyGenerator;
import com.ctoboost.cassandra.whoop.token.TokenBalanceValidator;
import com.datastax.driver.core.LocalDate;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.TypeCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


@SpringBootApplication
public class WhoopApplication {
    private final static Logger logger = LoggerFactory.getLogger(WhoopApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(WhoopApplication.class, args);
		//test();
        TokenBalanceValidator validator = new TokenBalanceValidator();
        validator.run();
	}

	private static void test(){
	    try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd");
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date startDay = isoFormat.parse("2018-10-24");

            int user = 33803;
            long elapsed = startDay.getTime();
            LocalDate dt = LocalDate.fromMillisSinceEpoch(elapsed);
            ByteBuffer part2 = TypeCodec.date().serialize(dt, ProtocolVersion.V5);
            ByteBuffer part1 = TypeCodec.cint().serialize(user, ProtocolVersion.V5);
            ByteBuffer rc = WhoopKeyGenerator.compose(part1, part2);
            logger.info("Token: " + TokenBalanceValidator.getToken(rc));
        }
        catch(Exception ex){

        }
    }

}
