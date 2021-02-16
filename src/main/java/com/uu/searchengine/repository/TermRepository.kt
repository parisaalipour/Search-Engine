package com.uu.searchengine.repository

import com.uu.searchengine.entity.Document
import com.uu.searchengine.entity.Term
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
@Transactional
interface TermRepository : JpaRepository<Term, String>,PagingAndSortingRepository<Term, String>
{
    @Query("SELECT a FROM Term a WHERE a.termString = :termString")
    fun findByTermString(@Param("termString") termString: String): List<Term>
}