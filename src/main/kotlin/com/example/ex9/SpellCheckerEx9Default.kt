package com.example.ex9

import com.example.ex2.service.DictionaryService
import com.example.ex9.service.SpellCheckerEx9
import org.osgi.framework.BundleContext
import org.osgi.service.component.ComponentContext
import org.osgi.service.component.annotations.*


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


  @Activate
  fun activate(cc: ComponentContext,
               bc: BundleContext,
               config: Map<String,Object>) {
    println("SpellCheckerEx9Default: activated")
  }

  @Deactivate
  fun deactivate(cc: ComponentContext,
               bc: BundleContext,
               config: Map<String,Object>) {
    println("SpellCheckerEx9Default: deactivated")
  }

  override fun check(word: String) {
    if (word == null || word.length === 0) {
      println("SpellCheckerEx9Default: word empty or null")
      return
    }
    println("SpellCheckerEx9Default: check called")
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
