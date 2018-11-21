package com.ctoboost.cassandra.whoop.token;


import com.ctoboost.cassandra.whoop.AppConfiguration;
import com.ctoboost.cassandra.whoop.MurmurHash;
import com.ctoboost.cassandra.whoop.key.WhoopKeyGenerator;
import com.ctoboost.cassandra.whoop.key.iKeyGenerator;
import com.ctoboost.cassandra.whoop.range.TokenRangeHolder;
import com.ctoboost.cassandra.whoop.range.iRangeHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TokenBalanceValidator {
    private final Logger logger = LoggerFactory.getLogger(TokenBalanceValidator.class);


    iKeyGenerator keyGenerator;

    iRangeHolder rangeHolder;



    private AppConfiguration myConfig = new AppConfiguration();

    public TokenBalanceValidator(){

        keyGenerator = new WhoopKeyGenerator();
        rangeHolder = new TokenRangeHolder();
    }

    public void run(){
        keyGenerator.init(myConfig);
        rangeHolder.init(myConfig);
        ExecutorService executor = Executors.newFixedThreadPool(myConfig.getMaxThread());

        AtomicInteger validCount = new AtomicInteger(0);
        Runnable validTask = () -> {
            try {
                while(true) {

                    ByteBuffer[] buf = keyGenerator.getBatchKeys();
                    if (buf!=null) {
                        for(ByteBuffer item : buf){
                            rangeHolder.put(getToken(item));
                        }
                    }
                    else{
                        logger.info("No more key generated. ");
                        validCount.addAndGet(1);
                        break;
                    }
                }
            } catch (Exception e) {
                logger.error("Exception in sending data " + e.getMessage());
                e.printStackTrace();
            }
        };

        for(int i = 0; i< myConfig.getMaxThread(); i++){
            executor.submit(validTask);
        }

        while (true){
            try {
                Thread.sleep(1000);
                if (validCount.get()==myConfig.getMaxThread()){
                    executor.shutdown();
                    break;
                }

            }
            catch(Exception ex){

            }
        }

        logger.info(rangeHolder.toString());
    }

    public static long getToken(ByteBuffer key){

        final long MIN_VALUE = 0x8000000000000000L;
        if (key.remaining()==0){
            return MIN_VALUE;
        }
        long[] hash = new long[2];
        MurmurHash.hash3_x64_128(key, key.position(), key.remaining(), 0, hash);
        return hash[0];
    }
}
