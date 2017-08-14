package com.esentri.ex2b

import com.esentri.ex2b.service.DictionaryService

class DictionaryServiceImpl : DictionaryService {
  override fun checkWord(word: String): Boolean {
    if(word.length > 5)
      return true
    return false
  }

}
