package com.example.library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {


    public DbHelper(@Nullable Context context) {
        super(context, "library.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table books(" +
                "_id INTEGER primary key autoincrement," +
                "title TEXT not null," +
                "author TEXT not null," +
                "year TEXT not null," +
                "cover TEXT," +
                "genre TEXT not null," +
                "description TEXT not null)");

        /*db.execSQL("create table genres(" +
                "_id INTEGER primary key autoincrement," +
                "name TEXT not null," +
                "email TEXT not null unique," +
                "image TEXT)");*/
//add name/email
//        db.execSQL("insert into people(name,email) values (\"Tom\",\"tom@gmail.com\")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists books");
        onCreate(db);
    }

    public List<Book> getData() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from books", null);
        List<Book> books = new ArrayList<>();

        while (cursor.moveToNext()) {
            Book book = new Book(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6)
            );
            book.setId(cursor.getInt(0));
            books.add(book);
        }
        return books;
    }

    public Book getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from books where _id=?", new String[]{id + ""});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Book book = new Book(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );
                book.setId(cursor.getInt(0));
                return book;
            }
        }
        return null;
    }

    public Boolean insertData(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put("title", book.getTitle());
        content.put("author", book.getAuthor());
        content.put("year", book.getYear());
        content.put("cover", book.getCover());
        content.put("genre", book.getGenre());
        content.put("description", book.getDescription());

        long result = db.insert("books", null, content);
        return result == -1 ? false : true;
    }

    public Boolean updateData(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put("title", book.getTitle());
        content.put("author", book.getAuthor());
        content.put("year", book.getYear());
        content.put("cover", book.getCover());
        content.put("genre", book.getGenre());
        content.put("description", book.getDescription());

        long result = db.update("books", content,"_id=?", new String[]{book.getId()+""});
        return result == -1 ? false : true;
    }
    public Boolean deleteData(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete("books", "_id=?", new String[]{book.getId()+""});
        return result == -1 ? false : true;
    }
}