package com.example.ex2

import com.example.ex2.service.DictionaryService
import org.osgi.framework.BundleActivator
import org.osgi.framework.BundleContext
import java.util.Hashtable


class Activator: BundleActivator {
  override fun start(context: BundleContext) {
     val props = Hashtable<String, String>()
     props.put("Language", "English")
     context.registerService(DictionaryService::class.java.name, DictionaryServiceImpl(), props)
  }

  override fun stop(context: BundleContext) {
  }

}
