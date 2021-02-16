package com.uu.searchengine.entity;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Bigram")
@Table(name = "bigrams")
public class Bigram {

    @Id
    @Column(nullable = false)
    String bigram;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "bigram_terms",
            joinColumns = @JoinColumn(name = "bigrams_bigram"),
            inverseJoinColumns = @JoinColumn(name = "term_termString"))
    Set<Term> terms = new HashSet<>();

    public Bigram(String bigram, Set<Term> terms) {
        this.bigram = bigram;
        this.terms = terms;
    }

    public Bigram(String bigram) {
        this.bigram = bigram;
    }

    public Bigram() {
    }

    public void addTerm(Term term){
        terms.add(term);
    }

    public void removeTerm(Term term){
        terms.remove(term);
    }

    public String getBigram() {
        return bigram;
    }

    public void setBigram(String bigram) {
        this.bigram = bigram;
    }

    public Set<Term> getTerms() {
        return terms;
    }

    public void setTerms(Set<Term> terms) {
        this.terms = terms;
    }
}
