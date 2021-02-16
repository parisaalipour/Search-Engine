package com.uu.searchengine.handlers

import com.uu.searchengine.BeanUtil
import com.uu.searchengine.dto.WebPageDto
import com.uu.searchengine.entity.*
import com.uu.searchengine.service.*
import com.uu.searchengine.utils.Normalizer
import com.uu.searchengine.utils.Tokenizer
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Component

@Component
object Handler {

    private val termService: TermService = BeanUtil.getBean(TermService::class.java)
    private val imageService: ImageService = BeanUtil.getBean(ImageService::class.java)
    private val documentService: DocumentService = BeanUtil.getBean(DocumentService::class.java)
    private val documentTermService: DocumentTermService = BeanUtil.getBean(DocumentTermService::class.java)

    fun addWebPageDto(webPageDto: WebPageDto) {

//        if (documentService.exists(webPageDto.url))
//            return

        val termStrings = Tokenizer.normalizeThenTokenizeToList(webPageDto.body)
        val termStringsSet = termStrings.toSet()

        val document = documentService.getDocumentByUrl(webPageDto.url) ?: Document(webPageDto.url, webPageDto.title)

        documentService.saveOrUpdateDocument(document)

        var i = 1

        val docTerms = arrayListOf<DocumentTerm>()
        val terms = arrayListOf<Term>()

        val deletedDocumentTerms = arrayListOf<DocumentTerm>()

//        println("term count ${termStringsSet.size}")

        for (termString in termStringsSet) {

//            println("$i of ${termStringsSet.size}")

            if (termString.contains(" "))
                println(termString + " kkkkkkkkkkkkkkkkkkkkkkkkk")

            i++

            val term = Term(termString)
//
//            term.addDocumentTerm(documentTerm)
//
//            document.addDocumentTerm(documentTerm)

            val documentTerm = DocumentTerm(termStrings.count { it == termString })

            documentTerm.document = document
            documentTerm.term = term

//            term.addDocumentTerm(documentTerm)
//            document.addDocumentTerm(documentTerm)

            docTerms.add(documentTerm)
            terms.add(term)

        }

        termService.saveTerms(terms)
        documentTermService.saveAll(docTerms)


//        println("count of images ${webPageDto.images.size}")
//        webPageDto.images.forEach {
//            if (imageService.findBySrc(it.src) == null)
//                document.addImage(Image(it.src))
//        }

        documentService.saveOrUpdateDocument(document)

    }

    fun updateWebPageDto(webPageDto: WebPageDto) {

//        if (documentService.exists(webPageDto.url))
//            return

        val termStrings = Tokenizer.normalizeThenTokenizeToList(webPageDto.body)
        val termStringsSet = termStrings.toSet()

        documentService.deleteDocumentByUrl(webPageDto.url)

        val document = documentService.getDocumentByUrl(webPageDto.url) ?: Document(webPageDto.url, webPageDto.title)

        documentService.saveOrUpdateDocument(document)

        var i = 1

        val docTerms = arrayListOf<DocumentTerm>()
        val terms = arrayListOf<Term>()

        val deletedDocumentTerms = arrayListOf<DocumentTerm>()

//        println("term count ${termStringsSet.size}")

        for (termString in termStringsSet) {

//            println("$i of ${termStringsSet.size}")

            if (termString.contains(" "))
                println(termString + " kkkkkkkkkkkkkkkkkkkkkkkkk")

            i++

            val term = Term(termString)
//
//            term.addDocumentTerm(documentTerm)
//
//            document.addDocumentTerm(documentTerm)

            val documentTerm = DocumentTerm(termStrings.count { it == termString })

            documentTerm.document = document
            documentTerm.term = term

//            term.addDocumentTerm(documentTerm)
//            document.addDocumentTerm(documentTerm)

            docTerms.add(documentTerm)
            terms.add(term)

        }

        termService.saveTerms(terms)
        documentTermService.saveAll(docTerms)


        println("count of images ${webPageDto.images.size}")
        webPageDto.images.forEach {
            if (imageService.findBySrc(it.src) == null)
                document.addImage(Image(it.src))
        }

        documentService.saveOrUpdateDocument(document)

    }

    fun deleteWebPageDto(url: String) {
        documentService.deleteDocumentByUrl(url)
    }

}