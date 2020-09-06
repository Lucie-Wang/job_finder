package com.luciewang.job.recommendation;

import com.luciewang.job.db.MySQLConnection;
import com.luciewang.job.entity.Item;
import com.luciewang.job.external.GitHubClient;

import java.util.*;

public class Recommendation {

    public List<Item> recommendItems(String userId, double lat, double lon){
        List<Item> recommendedItems = new ArrayList<>();
        //get all favorite itemIds
        MySQLConnection connection = new MySQLConnection();
        Set<String> favoritedItemIds = connection.getFavoriteItemIds(userId);
        //get all keywords, sort by count
        Map<String, Integer> allKeywords = new HashMap<>();
        for(String itemId : favoritedItemIds){
            Set<String> keywords = connection.getKeywords(itemId);
            for(String keyword : keywords){
                allKeywords.put(keyword, allKeywords.getOrDefault(keyword,0) + 1);
            }
        }
        connection.close();
        List<Map.Entry<String, Integer>> keywordList = new ArrayList<>(allKeywords.entrySet());
        keywordList.sort((Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) -> Integer.compare(e2.getValue(), e1.getValue()));
        //cut down search list only top 3
        if(keywordList.size() > 3){
            keywordList = keywordList.subList(0, 3);
        }
        //search based on keywords, filter out favorite items
        Set<String> visitedItems = new HashSet<>();
        GitHubClient client = new GitHubClient();

        for(Map.Entry<String, Integer> keyword : keywordList){
            List<Item> items = client.search(lat, lon, keyword.getKey());
            for(Item item : items){
                if(!favoritedItemIds.contains(item.getId()) && !visitedItems.contains(item.getId())){
                    recommendedItems.add(item);
                    visitedItems.add(item.getId());
                }
            }
        }
        return recommendedItems;
    }

}
