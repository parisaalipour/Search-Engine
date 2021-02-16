package com.uu.searchengine.utils

object Normalizer {

    /* todo: equivalence class normalizing */

    @JvmStatic
    fun normalize(input: String): MutableList<String> {
        val charList = input.toMutableList()
        var builder = StringBuilder()
        var punc: Boolean? = false
        var wordList: MutableList<String> = mutableListOf<String>()
        var it = 0

        var startTime = System.currentTimeMillis()
        while (it < input.length){

            if (input[it] == 'ء' || input[it] == 'ـ' || input[it] == 'َ' || input[it] == 'ِ' || input[it] == 'ّ' || input[it] == 'َ' || input[it] == 'ً'
                || input[it] == 'ٌ' || input[it] == 'ٌ' || input[it] == 'ٍ' || input[it] == 'ْ' || input[it] == 'ْ' || input[it] == 'ٌ' || input[it] == 'ٍ'
                || input[it] == 'ً' || input[it] == 'ُ' || input[it] == 'ِ' || input[it] == 'َ' || input[it] == 'ّ' || input[it] == 'ّ'
                || input[it] == 'ٰ' || input[it] == 'ٔ' || input[it] == 'َ' || input[it] == 'ُ' || input[it] == 'ّ') {
                it++ }

            else if (input[it] in '\u0620'..'\u065F' || input[it] in '\u0670'..'\u06EF' || input[it] in 'a'..'z' || input[it] in 'A'..'Z') {
                when (input[it]) {

                    /* character level normalizing */

                    // arabic letters
                    'ي' -> builder.append('ی')
                    'ئ' -> builder.append('ی')

                    'ك' -> builder.append('ک')

                    'ؤ' -> builder.append('و')
                    'ۇ' -> builder.append('و')

                    'ة' -> builder.append('ه')
                    'ۀ' -> builder.append('ه')

                    'أ' -> builder.append('ا')
                    'إ' -> builder.append('ا')
                    'ٱ' -> builder.append('ا')
                    else -> builder.append(input[it])
                }
                it++
            }
            else{
                if(!builder.isEmpty())
                    wordList.add(builder.toString().toLowerCase())
                builder = StringBuilder()
                it++}
        }

//        println("String : $builder")

        if(!builder.isEmpty())
            wordList.add(builder.toString().toLowerCase())

        println("normalizing time: " + (System.currentTimeMillis() - startTime))
//        println("String : $wordList")
        return wordList
    }
}