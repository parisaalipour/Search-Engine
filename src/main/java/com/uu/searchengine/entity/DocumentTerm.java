package com.uu.searchengine.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "DocumentTerm")
@Table(name = "document_term")
@IdClass(CompositeKey.class)
public class DocumentTerm {

    int termFrequency;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    Term term;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    Document document;

    public DocumentTerm(int termFrequency, Term term, Document document) {
        this.termFrequency = termFrequency;
        this.term = term;
        this.document = document;
    }

    public DocumentTerm() {
    }

    public DocumentTerm(int termFrequency) {
        this.termFrequency = termFrequency;
    }

    public int getTermFrequency() {
        return termFrequency;
    }

    public void setTermFrequency(int termFrequency) {
        this.termFrequency = termFrequency;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

}