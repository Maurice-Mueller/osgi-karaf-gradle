package com.esentri.ex3

import com.esentri.ex3.service.DictionaryService

class DictionaryServiceImpl : DictionaryService {
  override fun checkWord(word: String): Boolean {
    if(word.length > 5)
      return true
    return false
  }

}
