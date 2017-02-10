package br.com.nevents.crawler.model

import java.util.Date

case class FacebookEventWrapper(data: Array[FacebookEvent])
case class FacebookEvent(description: String, name: String, endTime: Date, place: Place,
                         startTime: Date, id: String, category: String)
case class Place(name: String, location: Location, id: String)
case class Location(city: String, country: String, latitude: Double, longitude: Double, state: String)