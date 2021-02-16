package com.uu.searchengine.handlers

import com.github.penemue.keap.PriorityQueue
import com.uu.searchengine.BeanUtil
import com.uu.searchengine.entity.Bigram
import com.uu.searchengine.entity.Document
import com.uu.searchengine.entity.DocumentTerm
import com.uu.searchengine.entity.Term
import com.uu.searchengine.service.BigramService
import com.uu.searchengine.service.DocumentService
import com.uu.searchengine.service.DocumentTermService
import com.uu.searchengine.service.TermService
import com.uu.searchengine.utils.RemovingStopWords
import com.uu.searchengine.utils.Tokenizer
import com.uu.searchengine.utils.VectorSpace
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component

@Component
object Provider {

    private val termService: TermService = BeanUtil.getBean(TermService::class.java)
    private val documentService: DocumentService = BeanUtil.getBean(DocumentService::class.java)
    private val documentTermService: DocumentTermService = BeanUtil.getBean(DocumentTermService::class.java)
    private val bigramService: BigramService = BeanUtil.getBean(BigramService::class.java)

    private fun getPhraseDocuments(terms: Set<String>): Set<Document>
    {
//        val terms = Tokenizer.normalizeThenTokenizeToSet(phrase)
        var termsList : List<String> = RemovingStopWords.removeStopWords(terms)

        var resultSet = mutableSetOf<Document>()
        var intersect = mutableSetOf<Document>()
        var counter = 0
        termsList.forEach { termString ->
            termService.getTermByTermString(termString)?.let {
                if (counter == 0){
                    println(it.documentTerms)
                    resultSet.addAll(it.documentTerms.map { it.document })
                    counter++
                }
                else{
                        println(it.documentTerms)
                        intersect.addAll(resultSet)
                        intersect.retainAll(it.documentTerms.map { it.document })
                        if ( intersect.size < 20 )
                            resultSet.addAll(it.documentTerms.map { it.document })
                        else
                            resultSet.addAll(intersect)
//                    resultSet.retainAll(it.documentTerms.map { it.document })
                        counter++
                }
            }

        }


        return resultSet
    }

    fun getDocumentByUrl(url: String): Document?{
        return documentService.getDocumentByUrl(url)
    }

    fun getTermDocumentTerm(termString: String)
    {
        termService.getTermByTermString(termString)?.let {
            println(it.documentTerms)
        }
    }

    fun search(phrase: String): List<Document>
    {

        val heap = PriorityQueue<ScoredDocumentWrapper>()

        val terms = Tokenizer.normalizeThenTokenizeToSet(phrase)
        val initialDocsResult = getPhraseDocuments(terms)

        println("found ${initialDocsResult.size} initial docs")

        val vs = VectorSpace()

        var startTime = System.currentTimeMillis()
        initialDocsResult.forEach {
            heap.add(ScoredDocumentWrapper(it,vs.cosineScore(terms.toList(),it)))
        }

        println("cosine score time: "+(System.currentTimeMillis() - startTime))
        println("finished building heap")

        val result = mutableListOf<Document>()

        for (i in 0 until 50)
            heap.poll()?.let {
                result.add(it.document)
                println(it.document.url + ": " + it.cosineScore)
            }

        return result
//        return initialDocsResult.toList()
    }

    private fun getScoredDocumentWrapper(document: Document , queryTerms: List<String>)
    : ScoredDocumentWrapper {
        return ScoredDocumentWrapper(document,0.0)
    }

    data class ScoredDocumentWrapper(
        var document: Document ,
        var cosineScore: Double = 0.0
    ): Comparable<ScoredDocumentWrapper> {
        override fun compareTo(other: ScoredDocumentWrapper): Int
        {
            return if (this.cosineScore < other.cosineScore) 1
            else -1
        }
    }

    fun getTermByTermString(termString: String): Term? = termService.getTermByTermString(termString)

    fun getBigramById(bigram: String): Bigram?{
        return bigramService.getBigramById(bigram)
    }

    fun getAllBigrams(): List<Bigram>?{
        return bigramService.allBigrams
    }

    fun getTermsForBigram(bigramString: String): Set<Term>? {
        return bigramService.getBigramById(bigramString)?.terms
    }

//    fun addTermsForBigram(bigramString: String, termsList: Set<Term>){
//        val bigram = bigramService.getBigramById(bigramString) ?: Bigram(bigramString)
//        for (term in termsList){
//            bigram.addTerm(term)
//        }
//        bigramService.saveOrUpdateBigram(bigram)
//    }

    fun saveBigram(bigramString: String){
        val bigram = bigramService.getBigramById(bigramString) ?: Bigram(bigramString)
        bigramService.saveBigram(bigram)
    }
    fun saveBigrams(bigrams: HashSet<Bigram>)
    {
        bigramService.saveBigrams(bigrams)
    }

//    fun findByTermStringAndDocumentUrl(termString: String , url: String): DocumentTerm?
//    {
//        return documentTermService.findByTermStringAndDocumentUrl(termString,url)
//    }

    fun getAllTerms(): List<Term>?{
        return termService.getAllTerms()
    }

    fun getTerms(pageIndex: Int, pageSize: Int): Page<Term>? {
        return termService.getTerms(pageIndex, pageSize);
    }

    fun getNumberOfTerms(): Long{
        return termService.getNumberOfTerms()
    }

    fun getNumberOfDocuments(): Long{
        return documentService.getNumberOfDocuments()
    }

    fun findByTermStringAndDocumentUrl(termString: String , id: Long): DocumentTerm?
    {
        return documentTermService.findByTermStringAndDocumentId(termString,id)
    }
}