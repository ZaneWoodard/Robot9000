package com.beastsmc.robot9000.storage;

import java.util.HashSet;
import java.util.UUID;

public interface PersistentStorage {
    public boolean contains(String hash);
    public void store(String hash);
    public HashSet<String> loadCache(int N);
    public int getViolationCount(UUID pid);
    public long getViolationExpiration(UUID pid);
    public int storeViolation(UUID pid);
}
