import kotlinx.serialization.StringFormat
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.setValue

fun uploadDB() {
    val students = listOf(
        Student("Иван", "Иванов","20з"),
        Student("Василий", "Петров","29с"),
        Student("Владимир", "Сабынин","20м"),
        Student("Петр", "Григорьев","21з")
    )
    mongoDb.student.insertMany(students)
    val tasks = listOf(
        Task("Сериализация", "ООП", typesOfTasks.LEC),
        Task("Проект", "ЭКОНОМ", typesOfTasks.KR),
        Task("Задача МС", "МАТЕМАТИКА", typesOfTasks.PRC),
        Task("Лабораторная работа 1", "СХЕМОТЕХНИКА", typesOfTasks.LAB)
    )
    mongoDb.task.insertMany(tasks)
    val marks = listOf(
        Marks("Иванов", "Проект", 5)
    )
    mongoDb.mark.insertMany(marks)
}

fun showTasks(){
    println(mongoDb.task.find().toList())
}

fun toChange(){
    println("Введите название задания для редактирования:")
    val toUpdate: String?
    val taskName: String = readLine().toString()
    println("Что хотите изменить: \n1 - Название\n2 - Предмет \n3 - Тип задания")
    when(readLine()?.toIntOrNull()){
        1 -> {
            println("\nВведите новое название:")
            toUpdate = readLine().toString()
            mongoDb.task.updateOne(
                Task::nameOfTask eq taskName, setValue(Task::nameOfTask,toUpdate)
            )
            println("\nНазвание изменено на: $toUpdate")
        }
        2 -> {
            println("\nВведите новое название предмета:")
            toUpdate = readLine().toString()
            mongoDb.task.updateOne(
                Task::nameOfTask eq taskName, setValue(Task::subjectOfTask, toUpdate)
            )
            println("\nНазвание предмета изменено на: $toUpdate")
        }
        3 -> {
            println("\nВыберите \n- Практика\n- Лабораторная\n- Курсовая \n- Лекция ")
            toUpdate = readLine().toString()
            if (toUpdate == "Практика") {
                mongoDb.task.updateOne(
                    Task::nameOfTask eq taskName, setValue(Task::taskType, typesOfTasks.PRC)
                )
                println("Тип задания изменен на: $toUpdate")
            }
            else if (toUpdate == "Лабораторная") {
                mongoDb.task.updateOne(
                    Task::nameOfTask eq taskName, setValue(Task::taskType, typesOfTasks.LAB)
                )
                println("Тип задания изменен на: $toUpdate")
            }
            else if (toUpdate == "Курсовая") {
                mongoDb.task.updateOne(
                    Task::nameOfTask eq taskName, setValue(Task::taskType, typesOfTasks.KR)
                )
                println("Тип задания изменен на: $toUpdate")
            }
            else if (toUpdate == "Лекция") {
                mongoDb.task.updateOne(
                    Task::nameOfTask eq taskName, setValue(Task::taskType, typesOfTasks.LEC)
                )
                println("Тип задания изменен на: $toUpdate")
            }
            else {
                println("Попробуйте снова.")
                toChange()
            }
        }
        else -> println("Попробуйте снова.")
    }
}

fun toDelete() {
    println("\nВведите название задания для удаления:")
    val toDelete: String = readLine().toString()
    mongoDb.task.deleteOne(Task::nameOfTask eq "$toDelete")
    println("Задание с названием: $toDelete , удалено")
}

fun toFind() {
    println("Выберите признак для поиска: \n1 - Название\n2 - Предмет\n3 - Тип работы")
    val toSearch: String
    when (readLine()?.toIntOrNull()) {
        1 -> {
            println("Введите название для поиска:")
            toSearch = readLine().toString()
            println(mongoDb.task.find(Task::nameOfTask eq toSearch).toList())
        }
        2 -> {
            println("Введите название предмета для поиска:")
            toSearch = readLine().toString()
            println(mongoDb.task.find(Task::subjectOfTask eq toSearch).toList())
        }
        3 -> {
            println("Введите тип работы для поиска:")
            when (readLine().toString()) {
                "Лабораторная" -> println(mongoDb.task.find(Task::taskType eq typesOfTasks.LAB).toList())
                "Практика" -> println(mongoDb.task.find(Task::taskType eq typesOfTasks.PRC).toList())
                "Курсовая" -> println(mongoDb.task.find(Task::taskType eq typesOfTasks.KR))
                "Лекция" -> println(mongoDb.task.find(Task::taskType eq typesOfTasks.LEC))
                else -> println("Введите корректное значение для поиска.")
            }
        }
        else -> println("Попробуйте снова. ")
    }
}

fun takeAStudents() {
    println("Работа со студентами\nВыберите:\n1 - Просмотр списка\n2 - Просмотр оценок\n3 - Выставление оценок студентам\n4 - Удаление оценок студентов")
    when(readLine()?.toIntOrNull()){
        1 -> {
            println(mongoDb.student.find().toList())
        }
        2 -> {
            println(mongoDb.mark.find().toList())
        }
        3 -> {
            println("Введите название задания:")
            val ntask: String = readLine().toString()
            println("Введите фамилию студента:")
            val sname: String = readLine().toString()
            println("Введите оценку")
            val mark: Int = readLine()!!.toInt()
            mongoDb.mark.insertOne(Marks(sname,ntask,mark))
        }
        4 -> {
            println("Введите фамилию студента:")
            val sname: String = readLine().toString()
            mongoDb.mark.deleteOne(Marks::surname eq sname)
            println("Оценка студента $sname удалена")
        }
        }
    }

fun addNewTask() {
    println("Введите предмет задания:")
    val subjname: String = readLine().toString()
    println("Введите тип задания: \n-Лабораторная\n-Практика\n-Курсовая\n-Лекция")
    val typetask: String = readLine().toString()
    var typet: typesOfTasks = typesOfTasks.LEC
    println("Введите название задания:")
    val nametask: String = readLine().toString()
    if (typetask == "Лабораторная") typet = typesOfTasks.LAB
    else if (typetask == "Практика") typet = typesOfTasks.PRC
    else if (typetask == "Курсовая") typet = typesOfTasks.KR
    else if (typetask == "Лекция") typet = typesOfTasks.LEC
    val toInsert = listOf(
        Task(
            nametask,
            subjname,
            typet
        )
    )
    mongoDb.task.insertMany(toInsert)
}