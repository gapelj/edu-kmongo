import kotlinx.serialization.Serializable

@Serializable
data class Student (
    var name: String,
    var surname: String,
    val group: String
)

enum class typesOfTasks {
    PRC, LAB, KR, LEC
}

@Serializable
data class Task(
    var nameOfTask: String,
    var subjectOfTask: String,
    var taskType: typesOfTasks
)

@Serializable
data class Marks(
    var surname: String,
    var nameOfTask: String,
    var mark: Int
)