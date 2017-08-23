package nl.ronalddehaan

import java.util.concurrent.TimeUnit

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import com.typesafe.config.ConfigFactory
import kafka.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration
import scala.util.control.NonFatal


object SimpleConsumer extends App {
  val brokers = "localhost:9092"
  val groupId = "console-consumer-11478"
  val topic = "my-topic"


  val config = ConfigFactory.load()
  val host = config.getString("http.host") // Gets the host and a port from the configuration
  val port = config.getInt("http.port")

  implicit val system = ActorSystem("example")
  implicit val ec = system.dispatcher  //bindAndHandle requires an implicit ExecutionContext
  implicit val materializer = ActorMaterializer()

  val consumerSettings = ConsumerSettings(system, new ByteArrayDeserializer, new StringDeserializer)
    .withBootstrapServers(brokers)
    .withGroupId(groupId)
    .withProperty(ConsumerConfig.AutoOffsetReset, "earliest")
    .withPollInterval(FiniteDuration(3, TimeUnit.SECONDS))

  def printMe(msg: String): Future[Done] = {
    println(msg)
    Future.successful(Done)
  }

  val done =
    Consumer.committableSource(consumerSettings, Subscriptions.topics(topic))
      .mapAsync(4) { msg =>
        printMe(msg.record.value)
          .map(_ => {
            msg
          })
      }
//      .mapAsync(1) { msg =>
//        msg.committableOffset.commitScaladsl()
//      }
      .runWith(Sink.ignore)

  done.onFailure {
    case NonFatal(e) => println(e)
  }
}
