package com.uu.searchengine.repository

import com.uu.searchengine.entity.Bigram
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
@Transactional
interface BigramRepository : JpaRepository<Bigram, String> {
}