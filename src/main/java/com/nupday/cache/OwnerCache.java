package com.nupday.cache;
import java.util.ArrayList;
import java.util.List;

import com.nupday.dao.Entity.Owner;

public class OwnerCache {
    private static List<Owner> owners = new ArrayList<>();

    public static Owner getOwnerById(Integer id) {
        for (Owner owner : owners) {
            if (owner.getId().equals(id)) {
                return owner;
            }
        }
        return null;
    }

    public static void setOwnerCache(List<Owner> owners) {
        OwnerCache.owners = owners;
    }

}
