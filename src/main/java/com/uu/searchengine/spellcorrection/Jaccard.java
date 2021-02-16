package com.uu.searchengine.spellcorrection;

import java.util.HashSet;
import java.util.Set;

public class Jaccard {
    public Set<String> intersection(Set<String> s1, Set<String> s2){
        Set<String> intersection = new HashSet<String>(s1); // use the copy constructor
        intersection.retainAll(s2);
        return intersection;
    }

    public double jaccard_index(Set<String> s1, Set<String> s2){
        double size_s1 = s1.size();
        double size_s2 = s2.size();

        Set<String> intersect = intersection(s1,s2);
        double size_intersect = intersect.size();

        double jaccard_in = size_intersect / (size_s1 + size_s2 - size_intersect);

        return jaccard_in;
    }

    public double jaccard_distance(double jaccardIndex){
        double jaccardDistance = 1 - jaccardIndex;
        return jaccardDistance;
    }

//    public static void main(String[] args) {
//        Set<Integer> s1 = new HashSet<>();
//        s1.add(1);
//        s1.add(2);
//        s1.add(3);
//        s1.add(4);
//        s1.add(5);
//        s1.add(6);
//        Set<Integer> s2 = new HashSet<>();
//        s2.add(5);
//        s2.add(6);
//        s2.add(7);
//        s2.add(8);
//        s2.add(9);
//        s2.add(10);
//
//        Jaccard jaccard = new Jaccard();
//        double jaccardIndex = jaccard.jaccard_index(s1,s2);
//        System.out.println(jaccardIndex);
//        System.out.println(jaccard.jaccard_distance(jaccardIndex));
//    }
}
