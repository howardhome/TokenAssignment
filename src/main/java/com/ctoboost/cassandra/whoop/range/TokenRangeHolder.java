package com.ctoboost.cassandra.whoop.range;

import com.ctoboost.cassandra.whoop.AppConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class TokenRangeHolder implements iRangeHolder {
    private final Logger logger = LoggerFactory.getLogger(TokenRangeHolder.class);

    private AtomicInteger[] ranges;
    private long[] tokenRanges = {-9073506518853040000L,
            -8928103822305500000L,
            -8841572904265490000L,
            -8824891647654790000L,
            -8543006982799750000L,
            -8148294636355550000L,
            -7986066504151760000L,
            -7623102506389960000L,
            -7616190319451910000L,
            -7558988593921040000L,
            -7419912859888750000L,
            -7162990682610950000L,
            -7098132263645080000L,
            -7065907869477810000L,
            -6769147967992160000L,
            -6673805610561450000L,
            -6442404938245050000L,
            -6434324449496900000L,
            -5866341422121560000L,
            -5743972706774030000L,
            -5322282596790480000L,
            -5257583272905730000L,
            -5146335352763590000L,
            -4923629502028450000L,
            -4743112914819330000L,
            -4620550966659150000L,
            -4579411328118210000L,
            -4487636918523560000L,
            -4234488337837450000L,
            -3872016530772710000L,
            -3790130162653480000L,
            -3707971952293260000L,
            -3559478265772320000L,
            -2879611245757570000L,
            -2312827745389230000L,
            -2296201264469200000L,
            -1996789370235100000L,
            -1157435492332570000L,
            -785787031062479000L,
            -500787624423518000L,
            -210550141734879000L,
            155417801597380000L,
            171873557855147000L,
            707700555970855000L,
            885719457179790000L,
            938028504251887000L,
            1023496201141410000L,
            1074513711935570000L,
            1169340035957160000L,
            1362832913957740000L,
            1811706728554160000L,
            2347261311547570000L,
            2482344216865040000L,
            2547514108085600000L,
            2584919269307560000L,
            2770751352942770000L,
            2889883940468990000L,
            3008380210555990000L,
            3225000800473840000L,
            3231899366414130000L,
            3368508390220160000L,
            4204028089413480000L,
            4373893381536100000L,
            5345670895712410000L,
            5363665538779750000L,
            5749346280507440000L,
            5979761164243650000L,
            6332552075614760000L,
            6375261851983460000L,
            6437706484889170000L,
            6440715320688930000L,
            6454401349924190000L,
            6533498309254070000L,
            7039924886051460000L,
            7566997488109530000L,
            7575451586078400000L,
            7750403539722940000L,
            8922355556162640000L,
            9182654739287410000L,
            9197258224022350000L
    };
    @Override
    public void init(AppConfiguration config) {

        ranges = new AtomicInteger[config.getNumVNode()];
        //calculate ranges
        for(int i = 1; i <= config.getNumVNode(); i++ ){
            ranges[i-1] = new AtomicInteger(0);
        }
    }

    @Override
    public void put(long value) {

        //binary search
        int index = findIndex(value);
        ranges[index].incrementAndGet();

    }

    @Override
    public void putCollections(long[] values) {

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Token ranges: \n");
        for(int i = 0; i < tokenRanges.length; i++){
            sb.append(tokenRanges[i] + ":" + ranges[i].get() + "\n");

        }
        sb.append("\n");

        for(int i = 1; i < tokenRanges.length; i++){
            sb.append(tokenRanges[i] + ":" + (tokenRanges[i] - tokenRanges[i-1]) + "\n");

        }

        return sb.toString();
    }

    private int findIndex(long value){
        //find the one is small than the value but the value small than next one
        int result = 0;
        if (value <= tokenRanges[0]){
            return result;
        }
        if (value > tokenRanges[tokenRanges.length-1]){
            return result;
        }
        for(int i = 1; i< tokenRanges.length; i++){
            if (value > tokenRanges[i-1] && value <= tokenRanges[i]){
                return i;
            }
        }
        logger.error("Could not find the index");
        return result;
    }
}
