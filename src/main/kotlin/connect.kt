import com.mongodb.ExplainVerbosity
import com.mongodb.client.FindIterable
import org.json.JSONObject
import org.litote.kmongo.KMongo
import org.litote.kmongo.json

val client = KMongo
    .createClient("mongodb://root:vTnQMK3dxjFd@192.168.0.106:27017")
val database = client.getDatabase("test")

fun prettyPrintJson(json: String, printArray: Boolean = false) =
    println(
        JSONObject(json)
            .toString(4)
    )

fun prettyPrintCursor(cursor: FindIterable<*>) =
    prettyPrintJson("{ result: ${cursor.json} }")

fun prettyPrintExplain(cursor: FindIterable<*>) =
    prettyPrintJson(cursor.explain(ExplainVerbosity.EXECUTION_STATS).json)