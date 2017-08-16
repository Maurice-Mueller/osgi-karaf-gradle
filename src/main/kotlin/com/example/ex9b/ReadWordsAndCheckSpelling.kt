package com.example.ex9b

import com.example.ex9.service.SpellCheckerEx9
import com.example.ex9b.service.IReadWordsAndCheckSpelling
import org.osgi.framework.BundleActivator
import org.osgi.framework.BundleContext
import org.osgi.service.component.annotations.Activate
import org.osgi.service.component.annotations.Component
import org.osgi.service.component.annotations.Reference
import org.osgi.service.component.annotations.ReferencePolicy
import java.io.InputStreamReader
import java.io.BufferedReader
import org.osgi.util.tracker.ServiceTracker


@Component(immediate = true)
class ReadWordsAndCheckSpelling : IReadWordsAndCheckSpelling {

  @Reference(policy = ReferencePolicy.DYNAMIC)
  private var spellChecker: SpellCheckerEx9? = null


  @Activate
  override fun readWordsFromUser() {
    println("Enter a blank line to exit.")
    var word = ""
    val console = BufferedReader(InputStreamReader(System.`in`))

    while (true) {
      print("Enter word: ")
      word = console.readLine()

      // Get the selected dictionary service, if available.
      //val spellChecker = serviceTracker!!.service

      if (word.length == 0)
        break
      if(spellChecker == null) {
        println("no spellchecker available")
        break
      }
      spellChecker!!.check(word)
    }
  }

}
