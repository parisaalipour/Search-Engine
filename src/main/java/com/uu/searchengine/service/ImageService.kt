package com.uu.searchengine.service

import com.uu.searchengine.entity.Image
import com.uu.searchengine.repository.DocumentTermRepository
import com.uu.searchengine.repository.ImageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ImageService {

    @Autowired
    private lateinit var repository: ImageRepository

    fun findBySrc(src: String): Image? {
        return repository.findBySrc(src).firstOrNull()
    }

}