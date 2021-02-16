package com.uu.searchengine.spellcorrection;

import com.uu.searchengine.entity.Bigram;
import com.uu.searchengine.entity.Term;
import com.uu.searchengine.handlers.Provider;
import com.uu.searchengine.utils.Normalizer;
import org.springframework.data.domain.Page;

import java.util.*;

public class KgramIndexing {

    public static List<Term> docTerm = Provider.INSTANCE.getAllTerms();

    public Map<Integer,List<Term>> answermap = new HashMap<>();
    int index=0;

    private static Map<String, Set<Term>> bigramsToDatabase = new HashMap<String, Set<Term>>();
    private static Map<String, Set<Term>> bigramsToWords = new HashMap<String, Set<Term>>();
    public List<String> tokenizing(String text){
        return Normalizer.normalize(text);
    }

    public static void bigram(Term term){
        String str = term.getTermString();
//        if(str.length() == 1) {
//            bigramsToDatabase.putIfAbsent(str, new HashSet<>());
//            bigramsToDatabase.get(str).add(term);
//        }
        for (int i=0 ; i<str.length()-2+1 ; i++){
            String s = str.substring(i, i+2);
            bigramsToDatabase.putIfAbsent(s,new HashSet<>());
            bigramsToDatabase.get(s).add(term);
        }
    }

    public Set<String> singleWordBigram(String str){
        Set<String> wordBigrams = new HashSet<>();
        if (str.length() == 1)
        {
            wordBigrams.add(str);
        }
        for (int i=0; i<str.length()-2 +1 ; i++){
            String s = str.substring(i, i+2);
            wordBigrams.add(s);
        }
        return wordBigrams;
    }

    public void initialize() {

        HashSet<Bigram> bigrams = new HashSet<>();
//        double termCounts = Provider.INSTANCE.getNumberOfTerms();
//        for (int i = 0; i < Math.ceil(termCounts/10000); i++) {
//            bigrams.clear();
//            System.out.println("starting from page "+ i);
//            Page<Term> documentTerm = Provider.INSTANCE.getTerms(i, 10000);
//            if (documentTerm != null && documentTerm.hasContent()){
//                List<Term> docTerm = documentTerm.getContent();

                for(Term t: docTerm) {
                    bigram(t);
                }
                for (Map.Entry e: bigramsToDatabase.entrySet()) {
                    if (!bigrams.contains(e.getKey())){
                        System.out.println(e.getKey().toString() + " is ready to add to database");

                        Bigram bigram = new Bigram(e.getKey().toString());

                        for (Term term : (Set<? extends Term>) e.getValue()) {
                            bigram.addTerm(term);
                        }

                        bigrams.add(bigram);
                    }

                }
                System.out.println("adding " + bigrams.size() + " bigrams to database");

                Provider.INSTANCE.saveBigrams(bigrams);

//                System.out.println("page "+ i +" added");
//
//            }else {
//                System.out.println("there is no data in term table!");
//            }
//        }
        System.out.println("finished adding");

    }

    public List<Term> spellCorrectionwithoutJaccard(String query, int n){
        List<Term> correctedWord = new ArrayList<>();

        EditDistance editDistance = new EditDistance();

        Set<String> myQueryBigrams = singleWordBigram(query);
        Map<Integer, HashSet<String>> edit_dist_map = new HashMap<>();
        for (String s : myQueryBigrams){
            Set<Term> wordsForBigram = Provider.INSTANCE.getTermsForBigram(s);
            if (wordsForBigram == null)
                continue;
            for (Term term : wordsForBigram){
                String str = term.getTermString();
                int e_dist = editDistance.editDist(str, query, str.length(), query.length());
                edit_dist_map.putIfAbsent(e_dist, new HashSet<>());
                edit_dist_map.get(e_dist).add(str);
            }
        }


        if(edit_dist_map.containsKey(1) && edit_dist_map.get(1).size()>=n){
            for (String str : edit_dist_map.get(1)){
                correctedWord.add(Provider.INSTANCE.getTermByTermString(str));
            }
        }
        else {
            TreeMap<Integer, HashSet<String>> tm = new TreeMap<>(edit_dist_map);
            Iterator itr = tm.keySet().iterator();
            int count = 0;
            while (itr.hasNext()) {
                int key = (int) itr.next();
                for (String s : edit_dist_map.get(key)) {

                    if (count < n) {
                        correctedWord.add(Provider.INSTANCE.getTermByTermString(s));
                        count++;
                    }
                }
            }
        }

        //foreach s in correctedWord sort by frequency
        Map<Term,Integer> temp = new HashMap<>();
        List<Integer> docfreqList = new ArrayList<>();
        for (Term word : correctedWord) {
            temp.put(word, word.getDocumentFrequency());
        }

        correctedWord.clear();

        /* Find Max Value of Map*/
        while(!temp.isEmpty()) {
            Term max = GFG.getMaxEntryInMapBasedOnValue(temp).getKey();
            correctedWord.add(max);
            temp.remove(max);
        }

        answermap.put(index,correctedWord);
        index++;

        return correctedWord;
    }


    public List<String> correctAndReturnSuggestions(String phrase ,int suggestionCount) {
        boolean AllCorrect = true;
        List<String> tokens = tokenizing(phrase);
        for (String token : tokens) {
            Term term = Provider.INSTANCE.getTermByTermString(token);
            if(term != null){
                List<Term> list = Arrays.asList(term);
                answermap.put(index,list);
                index++;
            }
            else{
                try {
                    AllCorrect = false;
                    List<Term> list = spellCorrectionwithoutJaccard(token, suggestionCount);
                    System.out.println("suggestions for query "+ token +":");
                    for (Term t: list)
                        System.out.print(t.getTermString()+" ");
                    System.out.println();
                }
                catch(NullPointerException e){
                    System.out.println("Nothing found for query " + token);
                }
            }
        }

        System.out.println("this is answer map:");
        for (Map.Entry e : answermap.entrySet()){
            System.out.print(e.getKey().toString()+": ");
            List<Term> list = (List<Term>) e.getValue();
            for (Term t : list)
                System.out.print(t.getTermString()+" ");
            System.out.println();
        }

        System.out.println();
        List<String> output = new ArrayList<>();
        StringBuilder sentence;
        for(int i=0; i< suggestionCount ; i++) {
            sentence = new StringBuilder();
            for (int integer : answermap.keySet()) {
                List<Term> temp = answermap.get(integer);
                if (temp.size() == 1){
                    sentence.append(temp.get(0).getTermString() + " ");
                }
                else if(i<temp.size())
                    sentence.append(temp.get(i).getTermString() + " ");
                else
                    sentence.append(" ");
            }
            output.add(sentence.toString());
            if (AllCorrect){
                sentence = null;
                answermap.clear();
                output.clear();
                break;
            }
        }
        sentence = null;
        answermap.clear();
        return output;
    }


}
