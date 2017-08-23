package nl.ronalddehaan

import akka.actor.ActorSystem
import akka.kafka.ProducerMessage
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.Source
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.ByteArraySerializer
import org.apache.kafka.common.serialization.StringSerializer
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import scala.concurrent.Future
import akka.Done
import scala.util.{Failure, Success}

trait ProducerExample {
  val system = ActorSystem("example")

  // #producer
  // #settings
  val producerSettings = ProducerSettings(system, new ByteArraySerializer, new StringSerializer)
    .withBootstrapServers("localhost:9092")
  // #settings
  val kafkaProducer = producerSettings.createKafkaProducer()
  // #producer

  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer.create(system)

  def terminateWhenDone(result: Future[Done]): Unit = {
    result.onComplete {
      case Failure(e) =>
        system.log.error(e, e.getMessage)
        system.terminate()
      case Success(_) => system.terminate()
    }
  }
}

object PlainSinkExample extends ProducerExample {
  def main(args: Array[String]): Unit = {
    // #plainSink
    val done = Source(1 to 100)
      .map(_.toString)
      .map { elem =>
        new ProducerRecord[Array[Byte], String]("test-topic", elem)
      }
      .runWith(Producer.plainSink(producerSettings))
    // #plainSink

    terminateWhenDone(done)
  }
}


//object TestProducer extends App{
//
//
//  val  props = new Properties()
//  props.put("bootstrap.servers", "localhost:9092")
//
//  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
//  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
//
//  val producer = new KafkaProducer[String, String](props)
//
//  val topic="test-topic"
//
//  for(i<- 1 to 50){
//    val record = new ProducerRecord(topic, "key", s"hello $i")
//    producer.send(record)
//  }
//
//  val record = new ProducerRecord(topic, "key", "the end "+new java.util.Date)
//  producer.send(record)
//
//  producer.close()
//
//}
