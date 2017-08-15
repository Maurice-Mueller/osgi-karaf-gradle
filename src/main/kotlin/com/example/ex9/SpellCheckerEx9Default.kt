package com.example.ex9

import com.example.ex2.service.DictionaryService
import com.example.ex9.service.SpellCheckerEx9
import org.osgi.service.component.annotations.Component
import org.osgi.service.component.annotations.Reference
import org.osgi.service.component.annotations.ReferenceCardinality
import org.osgi.service.component.annotations.ReferencePolicy


@Component
class SpellCheckerEx9Default(): SpellCheckerEx9 {

  /**
   * List of service objects.

   * This field is managed by the Service Component Runtime and updated
   * with the current set of available dictionary services.
   * At least one dictionary service is required.
   */
  @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.AT_LEAST_ONE)
  private val dictionaries: MutableList<DictionaryService>? = null

  override fun check(word: String) {
    if (word == null || word.length === 0) {
      return
    }

    synchronized(dictionaries!!) {
      dictionaries.forEachIndexed { index, dictionary ->
        if(dictionary.checkWord(word)) {
          println("${word} is valid for ${index}")
        } else {
          println("${word} is invalid for ${index}")
        }
      }
    }
  }
}
