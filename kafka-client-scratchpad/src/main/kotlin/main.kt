
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import java.util.Properties
import org.apache.kafka.clients.consumer.KafkaConsumer

const val TOPIC = "test"
const val CONSUMER_GROUP = "${TOPIC}_group"

const val BOOTSTRAP_SERVER = "localhost:9092"
const val SERIALIZATION_LIB = "org.apache.kafka.common.serialization"

val producerProperties = run {
    val properties = Properties()
    properties["bootstrap.servers"] = BOOTSTRAP_SERVER
    properties["key.serializer"] = "$SERIALIZATION_LIB.StringSerializer"
    properties["value.serializer"] = "MyDataSerializer"
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
    properties["key.deserializer"] = "$SERIALIZATION_LIB.StringDeserializer"
    properties["value.deserializer"] = "MyDataDeserializer"
    properties["group.id"] = CONSUMER_GROUP
    properties["enable.auto.commit"] = "true"
    properties["auto.commit.interval.ms"] = "1000"
    properties["session.timeout.ms"] = "30000"
    properties["auto.offset.reset"] = "earliest"
    properties
}

fun main(args: Array<String>) {

    val producer: Producer<String, MyData> = KafkaProducer(producerProperties)

    for (i in 0..99) {
        val producerRecord = ProducerRecord(TOPIC, Integer.toString(i), MyData(i))
        println("Created record for producer - $producerRecord")
        producer.send(producerRecord)
    }
    producer.close()

    val consumer = KafkaConsumer<String, MyData>(consumerProperties)

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