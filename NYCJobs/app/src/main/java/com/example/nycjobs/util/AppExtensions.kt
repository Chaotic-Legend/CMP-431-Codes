package com.example.nycjobs.util

/**
 * Extension function to get the tag for logging.
 */
val Any.TAG: String
    get() {
        return if(!javaClass.isAnonymousClass) {
            val name = javaClass.simpleName
            // First 23 Characters.
            if (name.length <= 23) name else name.substring(0, 23)
        } else {
            val name = javaClass.name
            // Last 23 Characters.
            if (name.length <= 23) name else name.substring(name.length - 23, name.length)
        }
    }

/**
 * Extension function to capitalize words.
 */
fun String.capitalizeWords(delimiter: String = " ") =
    split(delimiter).joinToString(delimiter) { word ->
        val lowercaseWord = word.lowercase()
        lowercaseWord.replaceFirstChar (Char::titlecaseChar)
    }