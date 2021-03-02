import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.serialization.Deserializer
import java.lang.Exception

class MyDataDeserializer: Deserializer<MyData> {
    override fun close() {
    }

    override fun configure(p0: MutableMap<String, *>?, p1: Boolean) {
    }

    override fun deserialize(p0: String?, p1: ByteArray?): MyData {
        var retVal: MyData? = null
        try {
            retVal = ObjectMapper().readValue(p1, MyData::class.java)
        }
        catch (e: Exception) {
            println(e.message)
        }
        return retVal!!
    }
}
