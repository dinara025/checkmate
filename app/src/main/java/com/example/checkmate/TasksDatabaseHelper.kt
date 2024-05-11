import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.checkmate.Task

class TasksDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "checkmate.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "alltasks"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_DEADLINE ="deadline"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT, $COLUMN_DEADLINE TEXT)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertTask(task: Task) {
        writableDatabase.use { db ->
            val values = ContentValues().apply {
                put(COLUMN_TITLE, task.title)
                put(COLUMN_CONTENT, task.content)
                put(COLUMN_DEADLINE, task.deadline)
            }
            db.insert(TABLE_NAME, null, values)
        }
    }

    fun getAllTasks(): List<Task> {
        val tasksList = mutableListOf<Task>()
        readableDatabase.use { db ->
            val query = "SELECT * FROM $TABLE_NAME"
            val cursor = db.rawQuery(query, null)
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
                val deadline = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEADLINE))
                val task = Task(id, title, content,deadline)
                tasksList.add(task)
            }
            cursor.close()
        }
        return tasksList
    }

    fun updateTask(task: Task) {
        writableDatabase.use { db ->
            val values = ContentValues().apply {
                put(COLUMN_TITLE, task.title)
                put(COLUMN_CONTENT, task.content)
                put(COLUMN_DEADLINE, task.deadline)
            }
            val whereClause = "$COLUMN_ID = ?"
            val whereArgs = arrayOf(task.id.toString())
            db.update(TABLE_NAME, values, whereClause, whereArgs)
        }
    }

    fun getTaskByID(taskId: Int): Task? {
        readableDatabase.use { db ->
            val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ?"
            val cursor = db.rawQuery(query, arrayOf(taskId.toString()))
            cursor.use {
                if (it.moveToFirst()) {
                    val id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID))
                    val title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE))
                    val content = it.getString(it.getColumnIndexOrThrow(COLUMN_CONTENT))
                    val deadline = it.getString(it.getColumnIndexOrThrow(COLUMN_DEADLINE))
                    return Task(id, title, content,deadline)
                }
            }
        }
        return null
    }

    fun deleteTask(taskId: Int) {
        writableDatabase.use { db ->
            val whereClause = "$COLUMN_ID = ?"
            val whereArgs = arrayOf(taskId.toString())
            db.delete(TABLE_NAME, whereClause, whereArgs)
        }
    }
}
