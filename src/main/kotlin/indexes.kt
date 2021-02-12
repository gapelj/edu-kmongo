import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.bson.Document
import org.litote.kmongo.*

fun main() {
    @Serializable
    data class Population(
        @SerialName("Country Code") val code: String,
        @SerialName("Country Name") val name: String,
        @SerialName("Value") val value: Float,
        @SerialName("Year") val year: Int
    )

    // got from https://datahub.io/core/population
    val populationJson = Population::class.java.getResource("population_json.json")
        .readText()
    val populationCol = Json.decodeFromString(
        ListSerializer(Population.serializer()),
        populationJson
    )
    println(populationCol.size)

    val population = database.getCollection<Population>().apply { drop() }

    population.insertMany(populationCol)

    println("\n --- Find year without index --- \n")
    prettyPrintExplain(population.find(Population::year eq 2000))

    val yearIndex = population.createIndex(Document.parse("{'Year' : 1}"))

    println("\n --- Find year with index --- \n")
    prettyPrintExplain(population.find(Population::year eq 2000))

    val bsonRequest = and(Population::code eq "RUS", Population::year gt 2000)
    prettyPrintCursor(population.find(bsonRequest))

    println("\n --- Find code and range years with year index ---\n")
    prettyPrintExplain(population.find(bsonRequest))

    population.dropIndex(yearIndex)
    println("\n --- Find code and range years without index --- \n")
    prettyPrintExplain(population.find(bsonRequest))

    val codeYearIndex = population.createIndex(Document.parse("{'Country Code' : 1, 'Year' : 1}"))
    println("\n --- Find code and range years with code/year index --- \n")
    prettyPrintExplain(population.find(bsonRequest))
}


