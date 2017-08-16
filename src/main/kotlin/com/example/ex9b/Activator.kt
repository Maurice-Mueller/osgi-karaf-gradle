package com.example.ex9b

import com.example.ex9.service.SpellCheckerEx9
import org.osgi.framework.BundleActivator
import org.osgi.framework.BundleContext
import java.io.InputStreamReader
import java.io.BufferedReader
import org.osgi.util.tracker.ServiceTracker


class Activator : BundleActivator {

  private var context: BundleContext? = null
  private var serviceTracker: ServiceTracker<*, SpellCheckerEx9>? = null

  override fun start(context: BundleContext) {
    this.context = context

    // Create a service tracker to monitor dictionary services.
    serviceTracker = ServiceTracker<Any, SpellCheckerEx9>(
      this.context!!,
      this.context!!.createFilter("(objectClass=" + SpellCheckerEx9::class.java.name + ")"), null)
    serviceTracker!!.open()

    readWordsFromUser()
  }

  private fun readWordsFromUser() {
    println("Enter a blank line to exit.")
    var word = ""
    val console = BufferedReader(InputStreamReader(System.`in`))

    while (true) {
      print("Enter word: ")
      word = console.readLine()

      // Get the selected dictionary service, if available.
      val spellChecker = serviceTracker!!.service

      if (word.length == 0)
        break
      if(spellChecker == null) {
        println("no spellchecker available")
        break
      }
      spellChecker.check(word)
    }
  }

  override fun stop(context: BundleContext) {}

}
