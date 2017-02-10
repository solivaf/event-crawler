package br.com.nevents.crawler

import br.com.nevents.crawler.driver.CrawlerDriver

object CrawlerApplication extends App {
  val programArgs = args
  CrawlerDriver.run()
}
