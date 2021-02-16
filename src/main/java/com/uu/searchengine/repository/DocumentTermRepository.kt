package com.uu.searchengine.repository

import com.uu.searchengine.entity.CompositeKey
import com.uu.searchengine.entity.DocumentTerm
import com.uu.searchengine.entity.Term
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
@Transactional
interface DocumentTermRepository : JpaRepository<DocumentTerm, CompositeKey>
{
    @Query("SELECT a FROM DocumentTerm a WHERE a.term.termString = :termString AND a.document.url = :url")
    fun findByTermStringAndDocumentUrl(@Param("termString") termString: String ,@Param("url") url: String): DocumentTerm?
}