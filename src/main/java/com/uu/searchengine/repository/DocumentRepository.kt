package com.uu.searchengine.repository

import com.uu.searchengine.dto.WebPageDto
import com.uu.searchengine.entity.Document
import org.springframework.cache.annotation.CacheEvict
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import javax.transaction.Transactional
import org.springframework.data.redis.core.RedisTemplate

import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisHash


@Repository
@Transactional
interface DocumentRepository : JpaRepository<Document, Long>
{
//    @Query("select url from document")
//    fun getAllSavedUrls(): List<String>
//
//    var hashOperations: HashOperations<*, *, *>?
//
//    var redisTemplate: RedisTemplate<String, List<Document>>?
//
//    fun RedisUserRepository(redisTemplate: RedisTemplate<String, List<Document>>?) {
//        this.redisTemplate = redisTemplate
//        hashOperations = this.redisTemplate!!.opsForHash()
//    }

    @Query("SELECT a FROM Document a WHERE a.url = :url")
    fun findByUrl(@Param("url") url: String): Document?


    @Transactional
    @Modifying
    @Query("DELETE FROM DocumentTerm WHERE document = :document")
    fun deleteDocumentTermsByDocumentId(@Param("document") document: Document)

//    @CacheEvict(value = ["documents" , "terms" , "documentTerms"])
    @Transactional
    @Modifying
    @Query("DELETE FROM Document a WHERE a.url = :url")
    fun deleteDocumentByUrl(@Param("url") url: String)

}