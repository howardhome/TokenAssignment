package com.ctoboost.cassandra.whoop.range;

import com.ctoboost.cassandra.whoop.AppConfiguration;

public interface iRangeHolder {

    void init(AppConfiguration config);
    void put (long value);
    void putCollections(long[] values);

    String toString();

}
