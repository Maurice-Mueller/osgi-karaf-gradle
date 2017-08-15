package com.example.ex6

import com.example.ex2.service.DictionaryService
import com.example.ex6.service.SpellChecker
import org.osgi.framework.*


typealias ServiceRef = ServiceReference<DictionaryService>

class Activator : BundleActivator, ServiceListener {

  private var context: BundleContext? = null
  private var serviceReferences: MutableList<ServiceRef> = ArrayList()
  private var serviceRegistration: ServiceRegistration<SpellChecker>? = null
  private var dictionaries = ArrayList<Pair<DictionaryService, String>>()

  override fun start(context: BundleContext) {
    this.context = context

    synchronized(this) {
      // Listen for events pertaining to dictionary services.
      this.context!!.addServiceListener(this,
        "(&(objectClass=" + DictionaryService::class.java.name + ")" +
          "(Language=*))")

      serviceReferences = this.context!!.getServiceReferences(
        DictionaryService::class.java.name, "(Language=*)").toMutableList() as MutableList<ServiceRef>

      serviceReferences!!.forEach {
        dictionaries.add(Pair(this.context!!.getService<Any>(it as ServiceReference<Any>) as DictionaryService, it.getProperty("Language") as String))
        println("service added: ${it.getProperty("Language")}")
      }

      serviceRegistration = this.context!!.registerService(SpellChecker::class.java.name, SpellCheckerDefault(dictionaries), null) as ServiceRegistration<SpellChecker>
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
    val service = this.context!!.getService(event.serviceReference)
    service ?: return

    val oldDictionaryPair = generatePair(event.serviceReference)
    dictionaries.remove(oldDictionaryPair)
    println("service removed: ${event.serviceReference!!.getProperty("Language")}")
  }

  private fun handleRegisteringEvent(event: ServiceEvent) {
    println("service registered: ${event.serviceReference!!.getProperty("Language")}")
    val service = this.context!!.getService(event.serviceReference)
    service ?: return

    val newDictionaryPair = generatePair(event.serviceReference)

    if(dictionaries.contains(newDictionaryPair))
      return

    dictionaries.add(newDictionaryPair)
    println("service added: ${event.serviceReference!!.getProperty("Language")}")
  }

  private fun generatePair(serviceReference: ServiceReference<*>): Pair<DictionaryService, String> =
    Pair(this.context!!.getService<Any>(serviceReference as ServiceReference<Any>?) as DictionaryService,
      serviceReference.getProperty("Language") as String)
}
