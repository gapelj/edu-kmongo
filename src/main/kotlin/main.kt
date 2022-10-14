import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
class Mongo {
    val client = KMongo
        .createClient("mongodb://localhost:27017")
    val mdb = client.getDatabase("MongoC")
    val student = mdb.getCollection<Student>()
    val task = mdb.getCollection<Task>()
    val mark = mdb.getCollection<Marks>()
}
val mongoDb = Mongo()

fun main() {
    mongoDb.student.drop()
    mongoDb.task.drop()
    mongoDb.mark.drop()
    uploadDB()
    var cicle = true
    while (cicle) {
        println("Введите: \n1 - Просмотр заданий")
        println("2 - Редактирование заданий\n3 - Добавление задания\n4 - Удаление задания\n5 - Поиск\n6 - Работа со студентами\n0 - Завершить")
        when (readLine()?.toIntOrNull()) {
            1 -> showTasks()
            2 -> toChange()
            3 -> addNewTask()
            4 -> toDelete()
            5 -> toFind()
            6 -> takeAStudents()
            0 -> cicle = false
            else -> println ("Попробуйте снова")
        }
    }
    println("Работа завершена")
}