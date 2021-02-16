package com.uu.searchengine.utils;

import com.uu.searchengine.entity.Document;
import com.uu.searchengine.entity.DocumentTerm;
import com.uu.searchengine.entity.Term;
import com.uu.searchengine.handlers.Provider;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class VectorSpace {

    HashMap<String, Double> DocTermIDF = new HashMap<>();

    Collection<DocumentTerm> documentTerms;

    Long docsCount = Provider.INSTANCE.getNumberOfDocuments();

    public double cosineScore(List<String> queryTokens , Document document)
    {
        documentTerms = document.getDocumentTerms();

//        double sorat = DotProduct(queryTokens,document);
//        double makhraj = euclideanDistance(document) * euclideanDistanceQuery(queryTokens);
//        return sorat/makhraj;
        return TFIDF(queryTokens, document);
    }

    private double TFIDF(List<String> queryTokens , Document document){

        double sum = 0.0;
        double counter = 0.0;

        String documentTitle = document.getTitle();
        List<String> documentTitleTokens = Normalizer.normalize(documentTitle);

        Set<String> intersection = new HashSet<String>(documentTitleTokens); // use the copy constructor
        intersection.retainAll(queryTokens);

        if (documentTitleTokens.size() != 0)
            counter += 0.6 * (intersection.size());

        double documentTermsSize = documentTerms.size();
        
        double idf = 0.0;

        for (String q: queryTokens){
            DocumentTerm dt = Provider.INSTANCE.findByTermStringAndDocumentUrl(q,document.getId());
            if(dt != null) {
                double tf = dt.getTermFrequency();
                tf = tf / documentTermsSize;
                Term t = dt.getTerm();
                if (t != null){
                    idf = IDF(t);
                    sum += tf * idf;
                }
            }
        }

        System.out.println("url: "+document.getUrl()+ " idf: "+idf+" counter: "+counter+" sum: "+sum * 0.4+" score: "+((sum * 0.4)+counter));

        return (sum * 0.4)+counter;
    }

    private double DotProduct(@NotNull List<String> queryTokens, Document document) {

        double sum = 0;

        for (String q : queryTokens)
        {
            DocumentTerm dt = Provider.INSTANCE.findByTermStringAndDocumentUrl(q,document.getId());
            if(dt != null) {
                double tf = dt.getTermFrequency();

                Term t = dt.getTerm();
                if (t != null)
                    sum += tf;
//                    sum += tf * Math.pow(IDF(t) , 2);
            }
        }
        return sum;
    }

    /** euclideanDistance
     * @param document*/
    private double euclideanDistance(Document document) {
        double result = 0.0;

        String url = document.getUrl();
        /** IDF for all terms of document*/
        for(DocumentTerm doc : documentTerms){
            double IDF = IDF(doc.getTerm());
            double TF = doc.getTermFrequency();
//            result += Math.pow(TF*IDF , 2);
            result += Math.pow(TF, 2);
        }

        return Math.sqrt(result);
    }

    private double euclideanDistanceQuery(List<String> queryTokens){
//        double result = 0.0;
//        for(String q : queryTokens)
//        {
//            Term t = Provider.INSTANCE.getTermByTermString(q);
//            if (t != null)
//                result += Math.pow(IDF(t) , 2);
//        }
//
//        return Math.sqrt(result);
        return Math.sqrt(queryTokens.size());
    }

    /** inverseDocumentFrequency */
    private double IDF(Term term) {
        double DF = term.getDocumentFrequency();
        //todo what if getTermByTermString is null
        if(DF > 0)
            return 1.0 + (Math.log(docsCount/DF));
        return 1.0;
    }

//    public static void main(String[] args) {
//        VectorSpace vs = new VectorSpace();
//        Scanner scanner = new Scanner(System.in);
//        String input = scanner.nextLine();
//
//    }
}
