package com.ctoboost.cassandra.whoop.key;

import com.ctoboost.cassandra.whoop.AppConfiguration;

import java.nio.ByteBuffer;

public interface iKeyGenerator {
    void init(AppConfiguration config);
    ByteBuffer getKey();
    ByteBuffer[] getBatchKeys();
}
