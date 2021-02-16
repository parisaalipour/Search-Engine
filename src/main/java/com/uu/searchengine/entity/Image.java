package com.uu.searchengine.entity;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Image")
@Table(name = "image")
public class Image {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    long id;

    @Column(columnDefinition="VARCHAR(2083)")
    String src;

    @ManyToMany(fetch = FetchType.LAZY , mappedBy = "images", cascade = {CascadeType.ALL})
    Set<Document> documents = new HashSet<>();

    public Image() {
    }

    public Image(String src) {
        this.src = src;
    }

    public Image(long id, String src) {
        this.id = id;
        this.src = src;
    }

    public Image(String src , Set<Document> documents) {
        this.src = src;
        this.documents = documents;
    }


    public void addDocument(Document document)
    {
        documents.add(document);
    }

    public void removeDocument(Document document) {
        documents.remove(document);
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
