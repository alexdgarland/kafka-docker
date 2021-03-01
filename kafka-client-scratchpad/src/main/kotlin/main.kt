
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import java.util.Properties
import org.apache.kafka.clients.consumer.KafkaConsumer

const val TOPIC = "test"
const val BOOTSTRAP_SERVER = "localhost:9092"
const val SERIALIZATION_LIB = "org.apache.kafka.common.serialization"
const val STRING_SERIALIZER = "$SERIALIZATION_LIB.StringSerializer"
const val STRING_DESERIALIZER = "$SERIALIZATION_LIB.StringDeserializer"

val producerProperties = run {
    val properties = Properties()
    properties["bootstrap.servers"] = BOOTSTRAP_SERVER
    properties["key.serializer"] = STRING_SERIALIZER
    properties["value.serializer"] = STRING_SERIALIZER
    properties["acks"] = "all"
    properties["retries"] = 0
    properties["batch.size"] = 16384
    properties["linger.ms"] = 1
    properties["buffer.memory"] = 33554432
    properties["request.timeout.ms"] = 2000
    properties
}

val consumerProperties = run {
    val properties = Properties()
    properties["bootstrap.servers"] = BOOTSTRAP_SERVER
    properties["key.deserializer"] = STRING_DESERIALIZER
    properties["value.deserializer"] = STRING_DESERIALIZER
    properties["group.id"] = "testgroup"
    properties["enable.auto.commit"] = "true"
    properties["auto.commit.interval.ms"] = "1000"
    properties["session.timeout.ms"] = "30000"
    properties
}

fun main(args: Array<String>) {

    val producer: Producer<String, String> = KafkaProducer(producerProperties)

    for (i in 0..99) {
        val producerRecord = ProducerRecord(TOPIC, Integer.toString(i), Integer.toString(i))
        println("Created record for producer - $producerRecord")
        producer.send(producerRecord)
    }
    producer.close()

    val consumer = KafkaConsumer<String, String>(consumerProperties)

    println("Subscribing to topic $TOPIC")
    consumer.subscribe(listOf(TOPIC))

    while (true) {
        val records = consumer.poll(1000)
        println("Got ${records.count()} records")
        for (record in records) {
            println("offset = ${record.offset()}, key = ${record.key()}, value = ${record.value()}")
        }
    }

}