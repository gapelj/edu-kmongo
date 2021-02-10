import com.mongodb.client.FindIterable
import kotlinx.serialization.Serializable
import org.litote.kmongo.*


fun main() {
    @Serializable
    data class Student(val name: String, val group: String)

    val students = database.getCollection<Student>().apply { drop() }

    println(" --- CREATE ---")
    students.insertOne(Student("Penny", "Girls"))
    students.insertOne("{'name': 'Amy', 'group': 'Girls' }")
    students.insertMany(
        arrayOf("Sheldon", "Leonard", "Howard", "Raj").map { Student(it, "Boys") }
    )

    println("\n --- READ --- \n")
    println(students.find().toList())
    println(students.findOne("{name: 'Penny'}"))
    val foundStudents: FindIterable<Student> = students.find(Student::group eq "Boys").sort("{'name': 1}")
    prettyPrintCursor(foundStudents)

    println("\n --- UPDATE --- \n")
    students.updateOne(Student::name eq "Amy", setValue(Student::name, "Amy Farrah Fowler"))
    prettyPrintCursor(students.find())
    students.updateMany("{'group' : 'Boys'}", "{'\$set' : {'group' : 'Man'}}")
    prettyPrintCursor(students.find())

    println("\n --- DELETE --- \n")
    students.deleteOne("{name: 'Penny'}")
    prettyPrintCursor(students.find())
    students.deleteMany(Student::group eq "Man")
    prettyPrintCursor(students.find())
    students.drop()
    prettyPrintCursor(students.find())
}

