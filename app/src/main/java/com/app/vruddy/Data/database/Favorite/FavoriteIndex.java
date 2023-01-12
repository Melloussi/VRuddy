package com.app.vruddy.Data.database.Favorite;

import java.util.ArrayList;
import java.util.List;

public class FavoriteIndex {
    private static List<String> idList = new ArrayList<>();
    private static FavoriteIndex instance;

    public static synchronized FavoriteIndex getInstance() {
        if (instance == null) {
            instance = new FavoriteIndex();
        }
        return instance;
    }

    public static void passIds(List<Favorite> localFavoriteList) {
        for (int index = 0; index < localFavoriteList.size(); index++) {
            //check if the id doesn't exist already
            if (!idList.contains(localFavoriteList.get(index).getVideo_id())) {
                idList.add(localFavoriteList.get(index).getVideo_id());
                System.out.println("------------------- [id: "+localFavoriteList.get(index).getVideo_id()+" ]-------------------");
            }
        }
    }
    public static void removeId(String id) {
        if(idList.contains(id)){
            idList.remove(id);
        }
    }
    public static void removeAll() {
        idList.clear();
    }
    public static boolean Id(String id){
        return idList.contains(id);
    }
}
