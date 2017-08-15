package com.example.ex6

import com.example.ex2.service.DictionaryService
import org.osgi.framework.BundleActivator
import org.osgi.framework.BundleContext
import org.osgi.framework.ServiceListener
import org.osgi.framework.ServiceReference
import java.io.InputStreamReader
import java.io.BufferedReader
import org.osgi.framework.ServiceEvent


class Activator : BundleActivator, ServiceListener {

  private var context: BundleContext? = null
  private var serviceReference: ServiceReference<*>? = null
  private var dictionaryService: DictionaryService? = null

  override fun start(context: BundleContext) {
    this.context = context
    synchronized(this) {
      // Listen for events pertaining to dictionary services.
      this.context!!.addServiceListener(this,
        "(&(objectClass=" + DictionaryService::class.java.name + ")" +
          "(Language=*))")

      val refs = this.context!!.getServiceReferences(
        DictionaryService::class.java.name, "(Language=*)")

      if (refs != null) {
        serviceReference = refs[0]
        dictionaryService = this.context!!.getService<Any>(serviceReference as ServiceReference<Any>?) as DictionaryService
      }
    }

    checkWordsFromUser()
  }

  private fun checkWordsFromUser() {
    println("Enter a blank line to exit.")
    var word = ""
    val console = BufferedReader(InputStreamReader(System.`in`))

    while (true) {
      print("Enter word: ")
      word = console.readLine()

      if (word.length == 0) {
        break
      } else if (dictionaryService == null) {
        println("No dictionary available.")
      } else if (dictionaryService!!.checkWord(word)) {
        println("Correct.")
      } else {
        println("Incorrect.")
      }
    }
  }

  override fun stop(context: BundleContext) {
    // NOTE: The service is automatically released.
  }

  @Synchronized override fun serviceChanged(event: ServiceEvent) {

    if (event.type == ServiceEvent.REGISTERED) {
      handleRegisteringEvent(event)
      return
    }
    if (event.type == ServiceEvent.UNREGISTERING) {
      handleUnregisteringEvent(event)
    }
  }

  private fun handleUnregisteringEvent(event: ServiceEvent) {
    println("service unregistered: ${event.serviceReference!!.getProperty("Language")}")
    if (event.serviceReference != serviceReference)
      return
    // Unget service object and null references.
    context!!.ungetService(serviceReference)
    serviceReference = null
    dictionaryService = null

    // Query to see if we can get another service.
    var refs: Array<ServiceReference<*>>? = null
    refs = context!!.getServiceReferences(
    DictionaryService::class.java.name, "(Language=*)")

    if (refs != null) {
      // Get a reference to the first service object.
      serviceReference = refs[0]
      dictionaryService = context!!.getService<Any>(serviceReference as ServiceReference<Any>?) as DictionaryService
    }
  }

  private fun handleRegisteringEvent(event: ServiceEvent) {
    println("new service registered: ${event.serviceReference!!.getProperty("Language")}")
    if (serviceReference != null)
      return
    // Get a reference to the service object.
    serviceReference = event.serviceReference
    dictionaryService = context!!.getService<Any>(serviceReference as ServiceReference<Any>?) as DictionaryService
    println("new service set: ${serviceReference!!.getProperty("Language")}")
  }

}
