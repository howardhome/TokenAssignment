package com.ctoboost.cassandra.whoop.key;

import com.ctoboost.cassandra.whoop.AppConfiguration;
import com.datastax.driver.core.LocalDate;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.TypeCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

public class WhoopKeyGenerator implements iKeyGenerator{
    private final Logger logger = LoggerFactory.getLogger(WhoopKeyGenerator.class);

    private int totalUsers;
    private int totalKeys;
    private AtomicInteger elapsedDays = new AtomicInteger();
    private Date startDay;
    private AppConfiguration config;

    @Override
    public ByteBuffer getKey() {
        return null;
    }

    @Override
    public  ByteBuffer[] getBatchKeys() {
        int day = elapsedDays.getAndAdd(1);
        if (day >= config.getMaxDays()) {
            logger.info("Enough days generated: " + day + " for " + config.getMaxUsers() + " users.");
            return null;
        }
        List<ByteBuffer> result = new ArrayList<ByteBuffer>();

        for(int i = 0; i<config.getMaxUsers(); i++ ){
            LocalDate dt =  LocalDate.fromMillisSinceEpoch(startDay.getTime()+ day * 24 * 60 * 60 * 1000);
            ByteBuffer part2 = TypeCodec.date().serialize(dt, ProtocolVersion.V5);
            ByteBuffer part1 = TypeCodec.cint().serialize(config.getStartUser() + i, ProtocolVersion.V5);
            ByteBuffer rc = compose(part1, part2);
            result.add(rc);
        }

        return result.toArray(new ByteBuffer[0]);
    }

    @Override
    public void init(AppConfiguration config) {
        elapsedDays.set(0);
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            startDay = isoFormat.parse("2018-08-01");
        }
        catch(Exception ex){
            logger.error(ex.getMessage());
        }
        this.config = config;
    }

    public static ByteBuffer compose(ByteBuffer... buffers) {
        if (buffers.length == 1) return buffers[0];

        int totalLength = 0;
        for (ByteBuffer bb : buffers) totalLength += 2 + bb.remaining() + 1;

        ByteBuffer out = ByteBuffer.allocate(totalLength);
        for (ByteBuffer buffer : buffers) {
            ByteBuffer bb = buffer.duplicate();
            putShortLength(out, bb.remaining());
            out.put(bb);
            out.put((byte) 0);
        }
        out.flip();
        return out;
    }

    static void putShortLength(ByteBuffer bb, int length) {
        bb.put((byte) ((length >> 8) & 0xFF));
        bb.put((byte) (length & 0xFF));
    }
}
