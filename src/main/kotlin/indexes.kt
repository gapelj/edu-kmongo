import com.mongodb.ExplainVerbosity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.bson.Document
import org.json.JSONObject
import org.litote.kmongo.*

fun main() {
    @Serializable
    data class Population(
        @SerialName("Country Code") val code: String,
        @SerialName("Country Name") val name: String,
        @SerialName("Value") val value: Float,
        @SerialName("Year") val year: Int
    )

    // get from https://datahub.io/core/population
    val populationJson = Population::class.java.getResource("population_json.json")
        .readText()
    val populationCol = Json.decodeFromString(
        ListSerializer(Population.serializer()),
        populationJson
    )
    println(populationCol.size)

    val client = KMongo
        .createClient("mongodb://root:vTnQMK3dxjFd@192.168.0.106:27017")
    val database = client.getDatabase("test")
    val population = database.getCollection<Population>().apply { drop() }

    population.insertMany(populationCol)

    println("___ Find year without index ___")
    prettyPrintJson(
        population
            .find(Population::year eq 2000)
            .explain(ExplainVerbosity.EXECUTION_STATS)
            .json
    )

    val yearIndex = population.createIndex(Document.parse("{'Year' : 1}"))

    println("___ Find year with index ___")
    prettyPrintJson(
        population
            .find(Population::year eq 2000)
            .explain(ExplainVerbosity.EXECUTION_STATS)
            .json
    )

    prettyPrintJson(
        population
            .find(and(Population::code eq "RUS", Population::year gt 2000))
            .json,
    true
    )
    println("___ Find code and range years with year index ___")
    prettyPrintJson(
        population
            .find(and(Population::code eq "RUS", Population::year gt 2000))
            .explain(ExplainVerbosity.EXECUTION_STATS)
            .json
    )

    population.dropIndex(yearIndex)
    println("___ Find code and range years without index ___")
    prettyPrintJson(
        population
            .find(and(Population::code eq "RUS", Population::year gt 2000))
            .explain(ExplainVerbosity.EXECUTION_STATS)
            .json
    )

    val codeYearIndex = population.createIndex(Document.parse("{'Country Code' : 1, 'Year' : 1}"))
    println("___ Find code and range years with code/year index ___")
    prettyPrintJson(
        population
            .find(and(Population::code eq "RUS", Population::year gt 2000))
            .explain(ExplainVerbosity.EXECUTION_STATS)
            .json
    )
}

fun prettyPrintJson(json: String, printArray: Boolean = false) =
    println(
        JSONObject(
            if (printArray)
                "{ a: $json }"
            else
                json
        ).toString(4)
    )
