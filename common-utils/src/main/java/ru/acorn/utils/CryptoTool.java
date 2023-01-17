package ru.acorn.utils;

import org.hashids.Hashids;

public class CryptoTool {
private final Hashids hashids;

    public CryptoTool(String salt) {
        int lengthOfHash = 10;
        this.hashids = new Hashids(salt, lengthOfHash);
    }

    public String hashFromId(Long id){
        return hashids.encode(id);
    }

    public Long idFromHash (String value){
        var id = hashids.decode(value);
        if(id != null && id.length > 0){
            return id[0];
        }
        return null;
    }
}
