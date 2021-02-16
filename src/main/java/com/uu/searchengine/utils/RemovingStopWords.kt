package com.uu.searchengine.utils

object RemovingStopWords {

    fun removeStopWords(terms: Set<String>) : List<String> {
        var stopWords = listOf("و","با", "در", "ها", "های", "ای", "را", "از")

        return (terms.toSet()).minus(stopWords.toSet()).toList()
    }
}