package com.uu.searchengine.spellcorrection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GFG {
    // Find the entry with highest value
    public static <K, V extends Comparable<V> > Map.Entry<K, V>
    getMaxEntryInMapBasedOnValue(Map<K, V> map)
    {
        // To store the result
        Map.Entry<K, V> entryWithMaxValue = null;

        // Iterate in the map to find the required entry
        for (Map.Entry<K, V> currentEntry : map.entrySet()) {

            if (
                // If this is the first entry, set result as this
                    entryWithMaxValue == null

                            // If this entry's value is more than the max value
                            // Set this entry as the max
                            || currentEntry.getValue()
                            .compareTo(entryWithMaxValue.getValue())
                            > 0) {

                entryWithMaxValue = currentEntry;
            }
        }

        // Return the entry with highest value
        return entryWithMaxValue;
    }

//    public static void main(String[] args) {
//        HashMap<String,Integer> h = new HashMap<>();
//        h.put("d",1);
//        h.put("sa",4);
//        h.put("c",4);
//        h.put("v",4);
//
//
//        List<String> l = new ArrayList<>();
//
//        for(int i=0; i<4 ; i++) {
//            String max = GFG.getMaxEntryInMapBasedOnValue(h).getKey();
//            l.add(max);
//            h.remove(max);
//        }
//        System.out.println(
//                "Entry with highest value: "
//                        + getMaxEntryInMapBasedOnValue(h).getKey());
//
//    }
}
