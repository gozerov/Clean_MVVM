package foundation.model.sqlite

object AppSQLiteContract {

    object StudentsTable {
        const val TABLE_NAME = "students"
        const val COLUMN_ID = "id"
        const val COLUMN_FULLNAME = "fullname"
        const val COLUMN_SCHOOL = "school"
        const val COLUMN_SCHOOL_GRADE = "school_grade"
        const val COLUMN_IS_RENAMED = "is_renamed"
        const val COLUMN_IS_CURRENT = "is_current"
    }

}