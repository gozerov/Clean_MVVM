package foundation.utils

import com.example.clean_mvvm.data.cache.entity.DBStudent
import com.example.clean_mvvm.domain.entity.student.Student

fun DBStudent.toStudent(): Student {
    return Student(
        id = id,
        fullName = fullName,
        school = school,
        schoolClass = schoolClass,
        isRenamed = isRenamed
    )
}

fun Student.toDBStudent(): DBStudent {
    return DBStudent(
        id = id,
        fullName = fullName,
        school = school,
        schoolClass = schoolClass,
        isRenamed = isRenamed
    )
}

fun List<DBStudent>.toStudentList(): List<Student> = this.map { it.toStudent() }

fun List<Student>.toDBStudentList(): List<DBStudent> = this.map { it.toDBStudent() }