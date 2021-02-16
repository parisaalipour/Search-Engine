package com.uu.searchengine.repository

import com.uu.searchengine.entity.Bigram
import com.uu.searchengine.entity.Document
import com.uu.searchengine.entity.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
@Transactional
interface ImageRepository : JpaRepository<Image, Long> {
    @Query("SELECT a FROM Image a WHERE a.src = :src")
    fun findBySrc(@Param("src") src: String): List<Image>
}