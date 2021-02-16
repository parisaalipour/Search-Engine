package com.uu.searchengine.entity;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.*;

@Entity(name = "Document")
@Table(name = "document")
public class Document {


    public Document(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public Document()
    {

    }

    public Document(long id, String url, String title, Collection<DocumentTerm> documentTerms, Set<Image> images) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.documentTerms = documentTerms;
        this.images = images;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    long id;


    @Column(columnDefinition="VARCHAR(2083)")
    String url;


    @Column(nullable = true)
    String title;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "document", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @Column
    Collection<DocumentTerm> documentTerms = new LinkedHashSet<DocumentTerm>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "document_image",
            joinColumns = @JoinColumn(name = "document_url"),
            inverseJoinColumns = @JoinColumn(name = "image_src"))
    Set<Image> images = new HashSet<>();

    public void addImage(Image image)
    {
        images.add(image);
        image.addDocument(this);
    }

    public void removeImage(Image image)
    {
        images.remove(image);
        image.removeDocument(this);
    }

    public void addDocumentTerm(DocumentTerm documentTerm)
    {
        documentTerms.add(documentTerm);
        documentTerm.setDocument(this);
    }

    public void addAllDocumentTerm(List<DocumentTerm> documentTerms)
    {
        documentTerms.addAll(documentTerms);
        for (DocumentTerm documentTerm : documentTerms) {
            documentTerm.setDocument(this);
        }
    }


    public void removeDocumentTerm(DocumentTerm documentTerm)
    {
        documentTerms.remove(documentTerm);
        documentTerm.setDocument(null);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Collection<DocumentTerm> getDocumentTerms() {
        return documentTerms;
    }

    public void setDocumentTerms(Collection<DocumentTerm> documentTerms) {
        this.documentTerms = documentTerms;
    }

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}