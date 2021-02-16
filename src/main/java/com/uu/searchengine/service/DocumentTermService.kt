package com.uu.searchengine.service

import com.uu.searchengine.BeanUtil
import com.uu.searchengine.entity.CompositeKey
import com.uu.searchengine.entity.DocumentTerm
import com.uu.searchengine.repository.DocumentTermRepository
import com.uu.searchengine.repository.TermRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.util.*

@Service
open class DocumentTermService {

    private var repository: DocumentTermRepository = BeanUtil.getBean(DocumentTermRepository::class.java)

    fun saveDocumentTerm(documentTerm: DocumentTerm): DocumentTerm {
        return repository.save(documentTerm)
    }

    fun deleteAll(documentTerms: DocumentTerm)
    {
        repository.delete(documentTerms)
    }

    fun saveAll(documentTerms: List<DocumentTerm>): List<DocumentTerm>
    {
        return repository.saveAll(documentTerms)
    }

//    @Cacheable(value = ["documentTerms"] , key = "#termString + #url")
//    open fun findByTermStringAndDocumentUrl(termString: String, url: String): DocumentTerm?
//    {
//        return repository.findByTermStringAndDocumentUrl(termString,url)
//    }
fun findByTermStringAndDocumentId(termString: String, id: Long): DocumentTerm?
{
    return repository.findById(CompositeKey(termString,id)).unwrap()
}

    private fun <DocumentTerm> Optional<DocumentTerm>.unwrap(): DocumentTerm? = orElse(null)

}