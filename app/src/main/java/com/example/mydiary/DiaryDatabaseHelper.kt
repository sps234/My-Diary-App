package com.example.mydiary

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DiaryDatabaseHelper (context: Context):SQLiteOpenHelper( context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object{
        private const val DATABASE_NAME = "diaryapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "Diaries"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertDiary(diary:Diary){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, diary.title)
            put(COLUMN_CONTENT, diary.content)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllDiary():List<Diary>{
        val diaryList = mutableListOf<Diary>()
        val db = readableDatabase
        val query = "SELECT* FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))

            val diary = Diary(id,title,content)
            diaryList.add(diary)
        }
        cursor.close()
        db.close()
        return diaryList
    }

    fun updateDiary(diary:Diary){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, diary.title)
            put(COLUMN_CONTENT, diary.content)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(diary.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun getDiaryById(diaryId: Int):Diary{
        val db = readableDatabase
        val query = "SELECT* FROM $TABLE_NAME WHERE $COLUMN_ID = $diaryId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))

        cursor.close()
        db.close()
        return Diary(id, title, content)
    }

    fun deleteDiary(diaryId: Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(diaryId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }
}