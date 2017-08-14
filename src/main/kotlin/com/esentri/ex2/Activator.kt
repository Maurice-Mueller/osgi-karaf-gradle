package com.esentri.ex2

import org.osgi.framework.BundleActivator
import org.osgi.framework.BundleContext


class Activator: BundleActivator {
  override fun start(context: BundleContext) {
//    try{
//      val props = Hashtable<String, String>()
//      props.put("Language", "English")
//      context.registerService(DictionaryService::class.simpleName, DictionaryServiceImpl(), props)
//    } catch(e: Exception) {
//      println(e.stackTrace)
//    }
  }

  override fun stop(context: BundleContext) {
  }

}
