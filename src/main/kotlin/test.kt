import org.litote.kmongo.*

data class Jedi(val name: String, val age: Int)

fun main() {
    val client = KMongo
        .createClient("mongodb://root:vTnQMK3dxjFd@192.168.0.105:27017")
    val database = client.getDatabase("test") //normal java driver usage
    val col= database.getCollection<Jedi>() //KMongo extension method

    col.insertOne(Jedi("Luke Skywalker", 19))
    col.insertOne("{name:'Yoda',age:896}")
    val yoda: Jedi? = col.findOne(Jedi::name eq "Yoda")
    println(yoda)
}

