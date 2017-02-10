package br.com.nevents.crawler.service

import br.com.nevents.crawler.util.SysProperties
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase}

object MongoDSService extends SysProperties {
  private val connectionUrl = getProperty("mongo.connection.url", "mongodb://localhost:27017")
  private val databaseName = getProperty("mongo.database.name", "test")
  private val collection = getProperty("mongo.collection.name", "fb_events")

  private val mongoClient: MongoClient = MongoClient(connectionUrl)
  private val mongoDatabase: MongoDatabase = mongoClient.getDatabase(databaseName)
  val mongoCollection: MongoCollection[Document] = mongoDatabase.getCollection(collection)

}
