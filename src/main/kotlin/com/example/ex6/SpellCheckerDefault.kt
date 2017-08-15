package com.example.ex6

import com.example.ex2.service.DictionaryService
import com.example.ex6.service.SpellChecker


class SpellCheckerDefault(val dictionaries: MutableList<Pair<DictionaryService, String>>): SpellChecker{

  override fun check(word: String) {
    if (word == null || word.length === 0) {
      return
    }

    synchronized(dictionaries) {
      dictionaries.forEach { (dictionary, name) ->
        if(dictionary.checkWord(word)) {
          println("${word} is valid for ${name}")
          return
        }
        println("${word} is invalid for ${name}")
      }
    }
  }
}
