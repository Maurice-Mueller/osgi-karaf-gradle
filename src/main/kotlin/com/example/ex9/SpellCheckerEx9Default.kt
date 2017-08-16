package com.example.ex9

import com.example.ex2.service.DictionaryService
import com.example.ex9.service.SpellCheckerEx9
import org.osgi.framework.BundleContext
import org.osgi.service.component.ComponentContext
import org.osgi.service.component.annotations.*


@Component(immediate = true)
class SpellCheckerEx9Default : SpellCheckerEx9 {

  /**
   * List of service objects.

   * This field is managed by the Service Component Runtime and updated
   * with the current set of available dictionary services.
   * At least one dictionary service is required.
   */
  @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.AT_LEAST_ONE)
  private val dictionaries: MutableList<DictionaryService>? = null


  @Activate
  fun activate() {
    println("SpellCheckerEx9Default: activated")
    if(dictionaries != null)
      println("SpellCheckerEx9Default: number of dictionary services: " + dictionaries.size)
    else
      println("SpellCheckerEx9Default: number of dictionary services: null")
  }

  @Deactivate
  fun deactivate() {
    println("SpellCheckerEx9Default: deactivated")
  }

  override fun check(word: String) {
    if (word.isEmpty()) {
      println("SpellCheckerEx9Default: word empty")
      return
    }
    println("SpellCheckerEx9Default: check called")
    if(dictionaries == null)
      return
    synchronized(dictionaries) {
      dictionaries.forEachIndexed { index, dictionary ->
        if (dictionary.checkWord(word)) {
          println("${word} is valid for ${index}")
        } else {
          println("${word} is invalid for ${index}")
        }
      }
    }
  }
}
