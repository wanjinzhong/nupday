package com.nupday.cache;
import java.util.ArrayList;
import java.util.List;

import com.nupday.dao.Entity.Visitor;

public class VisitorCache {

    private static List<Visitor> visitors = new ArrayList<>();

    public static Visitor getVisitorByCode(String accessCode) {
        for (Visitor visitor : visitors) {
            if (visitor.getCode().equals(accessCode)) {
                return visitor;
            }
        }
        return null;
    }

    public static Visitor popVisitor(Integer id) {
        int index = -1;
        for (int i = 0; i < visitors.size(); i ++) {
            if (visitors.get(i).getId().equals(id)) {
                index = i;
            }
        }
        Visitor item = visitors.get(index);
        if (index >= 0) {
            visitors.remove(index);
        }
        return item;
    }

    public static void pushVisitor(Visitor item) {
        popVisitor(item.getId());
        visitors.add(item);
    }

    public static void setVisitorCache(List<Visitor> visitors) {
        VisitorCache.visitors = visitors;
    }
}
