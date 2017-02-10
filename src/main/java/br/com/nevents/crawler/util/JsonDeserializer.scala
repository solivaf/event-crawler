package br.com.nevents.crawler.util

import br.com.nevents.crawler.model.FacebookEventWrapper
import com.google.gson.{FieldNamingPolicy, GsonBuilder}
import org.bson.BsonDocument

object JsonDeserializer {
  val gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

  def deserializer(json: String): FacebookEventWrapper = {
    BsonDocument.parse(json)
    gson.fromJson(json, classOf[FacebookEventWrapper])
  }
}
