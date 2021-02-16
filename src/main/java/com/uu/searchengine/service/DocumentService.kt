package com.uu.searchengine.service

import com.uu.searchengine.entity.Document
import com.uu.searchengine.repository.DocumentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service


@Service
class DocumentService {
    @Autowired
    private lateinit var repository: DocumentRepository

//    fun exists(url: String): Boolean = repository.exists(url)

//    fun getAllSavedUrls(): List<String> = repository.getAllSavedUrls()

    fun saveDocument(document: Document): Document {
        return repository.save(document)
    }

    fun saveDocuments(documents: List<Document>): List<Document> {
        return repository.saveAll(documents)
    }

    val allDocuments: List<Document>
        get() = repository.findAll()

    fun getDocumentById(id: Long): Document? {
        return repository.findById(id).orElse(null)
    }

//    @Cacheable(value = "documents" , key = "#url")
    fun getDocumentByUrl(url: String): Document? {
        return repository.findByUrl(url)
    }

//    @CacheEvict(value = ["documents" , "terms" , "documentTerms"])
    fun deleteDocumentByUrl(url: String) {
        repository.findByUrl(url)?.let {
            repository.deleteDocumentTermsByDocumentId(it)
            repository.deleteDocumentByUrl(it.url)
        }
    }

    fun updateWebPage(document: Document): Document {
        val existingDocument: Document = repository.findByUrl(document.url) ?: return repository.save(document)
        existingDocument.title = document.title
        existingDocument.documentTerms = document.documentTerms
        return repository.save(existingDocument)
    }

    fun saveOrUpdateDocument(document: Document): Document
    {
        var existingDocument = repository.findByUrl(document.url)
        existingDocument?.let {
            existingDocument.title = document.title
            existingDocument.documentTerms = document.documentTerms
            existingDocument.images.addAll(document.images)
            return saveDocument(existingDocument)
        }
        return saveDocument(document)
    }

    fun getNumberOfDocuments(): Long = repository.count()
}