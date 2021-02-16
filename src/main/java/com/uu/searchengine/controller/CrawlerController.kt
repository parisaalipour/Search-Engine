package com.uu.searchengine.controller

import com.uu.searchengine.Spider
import com.uu.searchengine.dto.ImageDto
import com.uu.searchengine.dto.WebPageDto
import com.uu.searchengine.handlers.Handler
import com.uu.searchengine.spellcorrection.KgramIndexing
import org.springframework.web.bind.annotation.*


@RestController
class CrawlerController {

//    @Autowired
//    private lateinit var webPageService: TermService

    @GetMapping("addtest")
    fun addTest() {

        val imageDtos  = mutableListOf<ImageDto>();
        imageDtos.add(ImageDto("a.png" , ""));
        imageDtos.add(ImageDto("b.png" , ""));

        val webPageDto = WebPageDto("google.com", "google", "jennifer steve friend", imageDtos)
        Handler.addWebPageDto(webPageDto)
    }

    @GetMapping("puttest")
    fun putTest() {

        val imageDtos  = mutableListOf<ImageDto>();
        imageDtos.add(ImageDto("a.png" , ""));
        imageDtos.add(ImageDto("b.png" , ""));

        val webPageDto = WebPageDto("google.com", "google", "jennifer steve", imageDtos)
        Handler.updateWebPageDto(webPageDto)

    }

    @GetMapping("deletetest")
    fun deleteTest() {
        Handler.deleteWebPageDto("google.com")
    }

    @GetMapping("kgramprocess")
    fun kgramProcess() {
        val kg = KgramIndexing()
        kg.initialize()
    }

    @GetMapping("crawl/{start}")
    fun crawl(@PathVariable("start") start: Boolean) {
        if (start)
            Spider.search("https://www.hamshahrionline.ir")
//            Spider.search("http://urmia.ac.ir")

        else Spider.stopSearch()
    }

}