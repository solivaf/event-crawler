package br.com.nevents.crawler.util

trait SysProperties {

  def getProperty(key: String, default: String = ""): String = {
    sys.props(key)
  }
}