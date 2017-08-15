package com.example.edv.server.core

import org.osgi.framework.BundleActivator
import org.osgi.framework.ServiceEvent
import org.osgi.framework.BundleContext
import org.osgi.framework.ServiceListener


class Activator: BundleActivator, ServiceListener {
  /**
   * Implements BundleActivator.start(). Prints
   * a message and adds itself to the bundle context as a service
   * listener.
   * @param context the framework context for the bundle.
   */
  override fun start(context: BundleContext) {
    println("Starting to listen for service events.")
    context.addServiceListener(this)
  }

  /**
   * Implements BundleActivator.stop(). Prints
   * a message and removes itself from the bundle context as a
   * service listener.
   * @param context the framework context for the bundle.
   */
  override fun stop(context: BundleContext) {
    context.removeServiceListener(this)
    println("Stopped listening for service events.")

    // Note: It is not required that we remove the listener here,
    // since the framework will do it automatically anyway.
  }

  /**
   * Implements ServiceListener.serviceChanged().
   * Prints the details of any service event from the framework.
   * @param event the fired service event.
   */
  override fun serviceChanged(event: ServiceEvent) {
    val objectClass = event.serviceReference.getProperty("objectClass") as Array<String>

    if (event.type == ServiceEvent.REGISTERED) {
      println(
        "Ex1: Service of type " + objectClass[0] + " registered.")
    } else if (event.type == ServiceEvent.UNREGISTERING) {
      println(
        "Ex1: Service of type " + objectClass[0] + " unregistered.")
    } else if (event.type == ServiceEvent.MODIFIED) {
      println(
        "Ex1: Service of type " + objectClass[0] + " modified.")
    }
  }
}
