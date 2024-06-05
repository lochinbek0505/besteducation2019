package com.example.besteducation2019.utilits

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.besteducation2019.model.Author

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "users.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USERS = "users"

        private const val COLUMN_ID = "id"
        private const val COLUMN_PHONE = "phone"
        private const val COLUMN_FIRST_NAME = "first_name"
        private const val COLUMN_LAST_NAME = "last_name"
        private const val COLUMN_MIDDLE_NAME = "middle_name"
        private const val COLUMN_IMAGE = "image"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_FIRST_NAME + " TEXT,"
                + COLUMN_LAST_NAME + " TEXT,"
                + COLUMN_MIDDLE_NAME + " TEXT,"
                + COLUMN_IMAGE + " TEXT" + ")")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun addUser(user: Author): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, user.id)
            put(COLUMN_PHONE, user.phone)
            put(COLUMN_FIRST_NAME, user.firstName)
            put(COLUMN_LAST_NAME, user.lastName)
            put(COLUMN_MIDDLE_NAME, user.middleName)
            put(COLUMN_IMAGE, user.image)
        }
        val success = db.insert(TABLE_USERS, null, values)
        db.close()
        return success
    }

    fun updateUser(user: Author): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PHONE, user.phone)
            put(COLUMN_FIRST_NAME, user.firstName)
            put(COLUMN_LAST_NAME, user.lastName)
            put(COLUMN_MIDDLE_NAME, user.middleName)
            put(COLUMN_IMAGE, user.image)
        }
        val success = db.update(TABLE_USERS, values, "$COLUMN_ID = ?", arrayOf(user.id.toString()))
        db.close()
        return success
    }

    fun deleteUser(id: Int): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_USERS, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return success
    }

    fun readData(): List<Author> {
        val userList = mutableListOf<Author>()
        val selectQuery = "SELECT * FROM $TABLE_USERS"
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val user = Author(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                    firstName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)),
                    lastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)),
                    middleName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MIDDLE_NAME)),
                    image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))
                )
                userList.add(user)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return userList
    }
}
