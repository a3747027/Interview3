package com.billy.interview2.solution3;

import com.billy.interview2.SongCache;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class SongCacheImpl3 implements SongCache {
    private final ConcurrentHashMap<String, AtomicLong> songPlayNum;
    public SongCacheImpl3() {
        this.songPlayNum = new ConcurrentHashMap<>();
    }

    @Override
    public void recordSongPlays(String songId, int numPlays) {
        if(!songPlayNum.containsKey(songId)) {
            synchronized (songId.intern()) {
                if(!songPlayNum.containsKey(songId)) {
                    AtomicLong val = new AtomicLong(0);
                    songPlayNum.put(songId, val);
                }
            }
        }
        songPlayNum.get(songId).addAndGet(numPlays);
    }

    @Override
    //using getOrDefault to simplify the steps.
    public int getPlaysForSong(String songId) {
        return songPlayNum.getOrDefault(songId,new AtomicLong(-1)).intValue();
    }

    @Override
    // using stream to simplify the coding by sacrificing a little efficiency.
    public List<String> getTopNSongsPlayed(int n) {
        Object[] arrayAllSongs =
                songPlayNum.entrySet().stream().sorted((e1, e2) -> e2.getValue().intValue() - e1.getValue().intValue())
                .map(Map.Entry::getKey).toArray();
        List<String> ans = new ArrayList<>();
        if (n==0) return ans;
        for (Object arrayAllSong : arrayAllSongs) {
            ans.add(arrayAllSong.toString());
            if(--n <=0) {
                break;
            }
        }
        return ans;
    }
}
