package com.uu.searchengine.entity;

import com.uu.searchengine.utils.Normalizer;

import java.io.Serializable;
import java.util.Objects;

public class CompositeKey implements Serializable {
    private String term;
    private long document;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeKey that = (CompositeKey) o;
        return Objects.equals(term, that.term) && Objects.equals(document, that.document);
    }

    @Override
    public int hashCode() {
        return Objects.hash(term, document);
    }

    public CompositeKey(String term, long document) {
        this.term = term;
        this.document = document;
    }

    public CompositeKey() {
    }
}