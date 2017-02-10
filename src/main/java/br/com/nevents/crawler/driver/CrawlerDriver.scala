package br.com.nevents.crawler.driver

import br.com.nevents.crawler.util.SysProperties
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Seconds, StreamingContext}
import br.com.nevents.crawler.service.FacebookEventService._

object CrawlerDriver extends SysProperties {
  val appName = getProperty("app.name")
  val appMaster = getProperty("app.master")
  val batchDuration = getProperty("app.batch.duration")
  val kafkaServers = getProperty("kafka.servers")
  val kafkaTopic = getProperty("kafka.topic")
  val groupId = getProperty("group.id")
  val enableAutoCommit = getProperty("enable.auto.commit", "true")

  def run(): Unit = {
    val sparkConf = new SparkConf().setAppName(appName).setMaster(appMaster)
    val streamingContext = new StreamingContext(sparkConf, Seconds(batchDuration.toInt))

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> getProperty("kafka.servers"),
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> groupId,
      "enable.auto.commit" -> enableAutoCommit.toBoolean
    )

    val stream = KafkaUtils.createDirectStream[String, String](
      streamingContext,
      PreferConsistent,
      Subscribe[String, String](Array(kafkaTopic), kafkaParams)
    )

    stream.foreachRDD(rdd => rdd.foreachPartition(partition => partition.foreach(record => getEventsFromFacebook(record.value()))))

    streamingContext.start()
    streamingContext.awaitTermination()
  }
}