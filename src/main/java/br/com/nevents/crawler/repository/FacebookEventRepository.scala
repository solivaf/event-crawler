package br.com.nevents.crawler.repository

import br.com.nevents.crawler.service.MongoDSService
import com.mongodb.client.model.{Filters, UpdateOptions}
import org.mongodb.scala.Document
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.result.UpdateResult


object FacebookEventRepository {

  def upsert(document: BsonDocument, id: String): Unit = {
    MongoDSService.mongoCollection
      .replaceOne(Filters.eq("_id", document.get("_id")), document, new UpdateOptions().upsert(true))
      .subscribe(
        (result: UpdateResult) => println(s"OnNext: $result"),
        (error: Throwable) => println(s"onError: $error")
      )
  }
}
