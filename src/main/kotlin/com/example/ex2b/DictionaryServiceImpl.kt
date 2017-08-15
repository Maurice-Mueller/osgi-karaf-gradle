package com.example.ex2b

import com.example.ex2.service.DictionaryService

class DictionaryServiceImpl : DictionaryService {
  override fun checkWord(word: String): Boolean {
    if(word.length > 5)
      return true
    return false
  }

}
