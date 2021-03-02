import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.serialization.Serializer

class MyDataSerializer: Serializer<MyData> {
    override fun close() {
    }

    override fun configure(p0: MutableMap<String, *>?, p1: Boolean) {
    }

    override fun serialize(p0: String?, p1: MyData?): ByteArray {
        return ObjectMapper().writeValueAsString(p1).toByteArray()
    }
}
