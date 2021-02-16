package com.uu.searchengine.utils

object Tokenizer {

    fun normalizeThenTokenizeToSet(text: String): Set<String> {
        return Normalizer.normalize(text).toSet()
    }

    fun normalizeThenTokenizeToList(text: String): List<String> {
        return Normalizer.normalize(text)
    }

}