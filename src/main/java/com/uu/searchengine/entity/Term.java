package com.uu.searchengine.entity;

import javax.persistence.*;
import java.util.*;

@Entity(name = "Term")
@Table(name = "term")
public class Term {


    public Term() {
    }

    public Term(String termString) {
        this.termString = termString;
    }

    public Term(String termString, List<DocumentTerm> documentTerms) {
        this.termString = termString;
        this.documentTerms = documentTerms;
    }

    public Term(String termString, List<DocumentTerm> documentTerms, Set<Bigram> bigrams) {
        this.termString = termString;
        this.documentTerms = documentTerms;
        this.bigrams = bigrams;
    }
    //    @Id
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
//    long id;

    @Id
    @Column
    String termString;

    @OneToMany(fetch = FetchType.LAZY , mappedBy = "term", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @Column
    List<DocumentTerm> documentTerms = new ArrayList();


    public Set<Bigram> getBigrams() {
        return bigrams;
    }

    public void setBigrams(Set<Bigram> bigrams) {
        this.bigrams = bigrams;
    }

    @ManyToMany(fetch = FetchType.LAZY , mappedBy = "terms", cascade = {CascadeType.ALL})
    Set<Bigram> bigrams = new HashSet<>();


    public void addDocumentTerm(DocumentTerm documentTerm)
    {
        documentTerms.add(documentTerm);
        documentTerm.setTerm(this);
    }

    public void removeDocumentTerm(DocumentTerm documentTerm)
    {
        documentTerms.remove(documentTerm);
        documentTerm.setDocument(null);
    }

    public int getDocumentFrequency() {
        return documentTerms.size();
    }

//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }

    public String getTermString() {
        return termString;
    }

    public void setTermString(String termString) {
        this.termString = termString;
    }

    public List<DocumentTerm> getDocumentTerms() {
        return documentTerms;
    }

    public void setDocumentTerms(List<DocumentTerm> documentTerms) {
        this.documentTerms = documentTerms;
    }


    @Override
    public String toString() {
        return "Term{" +
                ", termString='" + termString + '\'' +
                ", documentTerms=" + documentTerms.size() +
                '}';
    }

}