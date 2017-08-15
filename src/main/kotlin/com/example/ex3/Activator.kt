package com.example.ex3

import com.example.ex2.service.DictionaryService
import org.osgi.framework.BundleActivator
import org.osgi.framework.BundleContext
import org.osgi.framework.ServiceReference
import java.io.IOException
import java.io.InputStreamReader
import java.io.BufferedReader


class Activator : BundleActivator {

  override fun start(context: BundleContext) {
    // Query for all service references matching any language.
    val refs = context.getServiceReferences(
      DictionaryService::class.java.name, "(Language=*)")

    if (refs == null) {
      println("No dictionary service found. Exiting...")
      return
    }

    val dictionary = context.getService<Any>(refs.get(0) as ServiceReference<Any>?) as DictionaryService
    try {
      println("Enter a blank line to exit.")
      val input = BufferedReader(InputStreamReader(System.`in`))
      var word = ""

      // Loop endlessly.
      while (true) {
        // Ask the user to enter a word.
        print("Enter word: ")
        word = input.readLine()

        // If the user entered a blank line, then
        // exit the loop.
        if (word.length == 0) {
          break
        }

        // First, get a dictionary service and then check
        // if the word is correct.
        if (dictionary.checkWord(word)) {
          println("Correct.")
        } else {
          println("Incorrect.")
        }

        // Unget the dictionary service.
        context.ungetService(refs[0])
      }
    } catch (ex: IOException) {
    }

  }

  override fun stop(context: BundleContext) {
  }

}
