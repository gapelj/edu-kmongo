import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.litote.kmongo.*
import org.litote.kmongo.id.serialization.IdKotlinXSerializationModule
import java.util.*

fun main() {
    val json = Json { serializersModule = IdKotlinXSerializationModule }

    @Serializable
    data class Student(
        val name: String,
        val group: String,
        @Contextual val id: Id<Student> = newId()
    )

    @Serializable
    data class Grade(
        val studentId: @Contextual Id<Student>,
        val studentName: String,
        val value: Int? = null,
        @Serializable(with = DateAsLongSerializer::class)
        val date: Date? = null,
    )

    @Serializable
    data class Course(
        val name: String,
        val grades: List<Grade> = emptyList()
    )

    val mStudents = database.getCollection<Student>().apply { drop() }
    val mCourses = database.getCollection<Course>().apply { drop() }

    // direction create session

    val students = listOf("Penny", "Amy").map { Student(it, "Girls") } +
            listOf("Sheldon", "Leonard", "Howard", "Raj").map { Student(it, "Boys") }
    mStudents.insertMany(students)
    val courses = listOf("Math", "Phys", "History").map {
        Course(it, students.map { Grade(it.id, it.name) })
    }
    mCourses.insertMany(courses)
    prettyPrintCursor(mStudents.find())
    prettyPrintCursor(mCourses.find())

    // tutor set grade
    val math = mCourses.findOne { Course::name eq "Math" } !!

    fun setGrade(course: Course, studentName: String, value: Int) {
        val oldGradeWithIndex = course.grades.withIndex().find { it.value.studentName == studentName }
        val newGrade = oldGradeWithIndex?.value?.copy(value = value, date = Date(System.currentTimeMillis()))
        val newGradeJson = json.encodeToString(newGrade)
        val updateExpr = "{ '\$set' : { 'grades.${oldGradeWithIndex?.index}' : $newGradeJson } }"
        mCourses.updateOne(Course::name eq course.name, updateExpr)
    }

    setGrade(math, "Penny", 5)
    setGrade(math, "Sheldon", 6)

    prettyPrintCursor(mCourses.find(Course::name eq "Math" ))

}

