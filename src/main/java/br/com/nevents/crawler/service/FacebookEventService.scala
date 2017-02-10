package br.com.nevents.crawler.service

import java.util.Date

import akka.actor.ActorSystem
import br.com.nevents.crawler.model.{FacebookEvent, FacebookEventWrapper, Location, Place}
import br.com.nevents.crawler.repository.FacebookEventRepository
import br.com.nevents.crawler.util.{JsonDeserializer, SysProperties}
import org.mongodb.scala.bson.{BsonBoolean, BsonDateTime, BsonDocument, BsonDouble, BsonString}
import spray.client.pipelining.{Get, sendReceive}
import spray.http.{HttpCharsets, Uri}
import spray.http.Uri.Path
import FacebookEventRepository.upsert

object FacebookEventService extends SysProperties {
  val graphAPIUri = getProperty("graph.facebook.uri")
  val accessToken = getProperty("graph.facebook.access.token")
  val limit = getProperty("limit", "50")
  val eventFields = "category,description,name,place,start_time,end_time"

  def saveOrReplace(event: FacebookEvent): Unit = {
    upsert(buildFacebookEventMutableDocument(event), event.id)
  }

  private def buildFacebookEventMutableDocument(event: FacebookEvent): BsonDocument = {
    val document = BsonDocument()
    var now: Date = new Date()

    if (event.category.!=(null)) document.put("category", BsonString(event.category))
    if (event.startTime.!=(null)) now = event.startTime
    if (event.endTime.!=(null)) document.put("end_time", BsonDateTime(event.endTime))


    if (event.id.!=(null) && event.description.!=(null) && event.name.!=(null)) {
      document.put("_id", BsonString(event.id))
      document.put("description", BsonString(event.description))
      document.put("name", BsonString(event.name))
      document.put("start_time", BsonDateTime(now))
      document.put("is_approved", BsonBoolean(false))
      document.put("updatedAt", BsonDateTime(now))

      if (event.place.!=(null)) document.put("place", placeDocument(event.place))
    }
    document
  }

  private def placeDocument(place: Place): BsonDocument = {
    var document = BsonDocument("name" -> place.name)
    if (place.location.!=(null))
      document.put("location", locationDocument(place.location))
    document
  }

  private def locationDocument(location: Location): BsonDocument = {
    val document: BsonDocument = BsonDocument()
    if (location.city.!=(null)) document.put("city", BsonString(location.city))
    if (location.latitude.!=(0.0)) document.put("latitude", BsonDouble(location.latitude))
    if (location.latitude.!=(0.0)) document.put("longitude", BsonDouble(location.longitude))
    document
  }

  def getEventsFromFacebook(userId: String) = {
    implicit val actorSystem = ActorSystem()
    import actorSystem.dispatcher

    try {
      val pipeline = sendReceive
      pipeline(
        Get(
          Uri(graphAPIUri)
            .withPath(Path./(userId + "/events"))
            .withQuery("access_token" -> accessToken, "limit" -> limit, "fields" -> eventFields)
        )
      ).map({
        response =>
          if (response.status.isFailure)
            sys.error(s"Received unexpected status ${response.status} : ${response.entity.asString(HttpCharsets.`UTF-8`)}")
          else {
            val wrapper: FacebookEventWrapper = JsonDeserializer.deserializer(response.entity.asString)
            wrapper.data.foreach(event => saveOrReplace(event))
          }
      })
    } catch {
      case e: java.net.ConnectException => ""
      case t: Throwable => ""
    }
  }
}
