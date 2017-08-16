package com.example.ex9b.service

import org.osgi.service.component.annotations.Activate


interface IReadWordsAndCheckSpelling {
  @Activate
  fun readWordsFromUser()
}
