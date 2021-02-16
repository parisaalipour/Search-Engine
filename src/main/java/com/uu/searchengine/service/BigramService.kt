package com.uu.searchengine.service

import com.uu.searchengine.entity.Bigram
import com.uu.searchengine.entity.Document
import com.uu.searchengine.entity.Term
import com.uu.searchengine.repository.BigramRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.SQLIntegrityConstraintViolationException

@Service
class BigramService {
    @Autowired
    private lateinit var repository: BigramRepository

    fun saveBigram(bigram: Bigram): Bigram {
        return repository.save(bigram);
    }

    fun saveBigrams(bigrams: HashSet<Bigram>) {

        var mutableBigrams = HashSet<Bigram>()
        for (bigram in bigrams) {
            mutableBigrams.add(saveOrUpdateBigram(bigram))
        }

        try {
            repository.saveAll(mutableBigrams)
        }catch (e: SQLIntegrityConstraintViolationException){
            e.printStackTrace()
        }
    }

    private fun saveOrUpdateBigram(bigram: Bigram): Bigram
    {
        var existingBigram = getBigramById(bigram.bigram)
            existingBigram?.let {
                existingBigram.terms.addAll(bigram.terms)
                return existingBigram
            }
        return bigram
    }

    val allBigrams: List<Bigram>
        get() = repository.findAll()

    fun getBigramById(bigram: String): Bigram? {
        return repository.findById(bigram).orElse(null)
    }

    fun deleteBigram(bigram: String) {
        repository.deleteById(bigram)
    }

}