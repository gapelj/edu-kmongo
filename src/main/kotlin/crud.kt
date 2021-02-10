import com.mongodb.client.FindIterable
import kotlinx.serialization.Serializable
import org.litote.kmongo.*


fun main() {
    @Serializable
    data class Student(val name: String, val group: String)

    val client = KMongo
        .createClient("mongodb://root:vTnQMK3dxjFd@192.168.0.100:27017")
    val database = client.getDatabase("test") //normal java driver usage
    val students = database.getCollection<Student>() //KMongo extension method

    println(" --- CREATE ---")
    students.insertOne(Student("Penny", "Girls"))
    students.insertOne("{'name': 'Amy', 'group': 'Girls' }")
    students.insertMany(
        arrayOf("Sheldon", "Leonard").map { Student(it, "Boys") }
    )

    println(" --- READ ---")
    println(students.find().toList())
    println(students.findOne("{name: 'Penny'}"))
    val foundStudents: FindIterable<Student> = students.find(Student::group eq "Boys")
    println(foundStudents.toList())

    println(" --- UPDATE ---")
    students.updateOne(Student::name eq "Amy", setValue(Student::name, "Amy Farrah Fowler"))
    println(students.find().toList())
    students.updateMany("{'group' : 'Boys'}", "{'\$set' : {'group' : 'Man'}}")
    println(students.find().toList())

    println(" --- DELETE ---")
    students.deleteOne("{name: 'Penny'}")
    println(students.find().toList())
    students.deleteMany(Student::group eq "Man")
    println(students.find().toList())
    students.drop()
    println(students.find().toList())
}

