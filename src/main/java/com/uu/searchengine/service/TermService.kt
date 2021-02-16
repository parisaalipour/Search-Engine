package com.uu.searchengine.service

import com.uu.searchengine.entity.Term
import com.uu.searchengine.repository.TermRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.util.*

@Service
open class TermService {

    @Autowired
    private lateinit var repository: TermRepository


    fun saveTerm(term: Term): Term {
        return repository.save(term)
    }

    fun saveTerms(terms: MutableList<Term>): List<Term> {

        var mutableTerms = mutableListOf<Term>()
        for (term in terms) {
            mutableTerms.add(saveOrUpdateTerm(term))
        }

        return repository.saveAll(mutableTerms)
    }

//    @Cacheable(value = ["term"] , key = "#termString")
    fun getTermByTermString(termString: String): Term? {
        return repository.findById(termString).unwrap()
    }

    fun deleteTerm(id: String) {
        repository.deleteById(id)
    }

    fun updateTerms(term: Term): Term {
        val existingTerm: Term = getTermByTermString(term.termString) ?: return saveTerm(term)
        existingTerm.documentTerms = term.documentTerms
        return repository.save(existingTerm)
    }

    private fun saveOrUpdateTerm(term: Term): Term
    {
        var existingTerm = getTermByTermString(term.termString)
        existingTerm?.let {
            existingTerm.documentTerms.addAll(term.documentTerms)
            return existingTerm
        }
        return term
    }

    fun getTerms(pageIndex: Int, pageSize: Int): Page<Term>?{
        val pageRequest = PageRequest.of(pageIndex, pageSize)
        return repository.findAll(pageRequest)
    }

    fun getAllTerms() : List<Term>?{
        return repository.findAll()
    }

    fun getNumberOfTerms(): Long{
        return repository.count()
    }

    private fun <Term> Optional<Term>.unwrap(): Term? = orElse(null)

}
