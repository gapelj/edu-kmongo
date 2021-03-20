import org.litote.kmongo.eq
import org.litote.kmongo.regex
import kotlin.reflect.KProperty1

fun main() {
    println("\n --- Create filled study sheet --- \n")
    fillStudentAndCourseWithGrades()
    prettyPrintCursor(mCourses.find())

    val regexpr = Regex("y")
    prettyPrintCursor(mCourses.find(Course::name regex regexpr))
}