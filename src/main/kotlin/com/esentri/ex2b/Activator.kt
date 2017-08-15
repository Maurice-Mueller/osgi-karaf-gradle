package com.esentri.ex2b

import com.esentri.ex2.service.DictionaryService
import org.osgi.framework.BundleActivator
import org.osgi.framework.BundleContext
import java.util.*


class Activator: BundleActivator {
  override fun start(context: BundleContext) {
      val props = Hashtable<String, String>()
      props.put("Language", "German")
      context.registerService(DictionaryService::class.java.name, DictionaryServiceImpl(), props)
  }

  override fun stop(context: BundleContext) {
  }

}
