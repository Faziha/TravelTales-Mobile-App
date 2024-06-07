package com.example.travel_tales_xml.offline

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.example.travel_tales_xml.models.Article

class MySqlHelper:SQLiteOpenHelper {
    var context: Context? =null;
    companion object{
        var TABLE:String="Article"
        var ID:String="id"
        var TITLE:String="title"
        var DESCRIPTION:String="description"
        var AUTHOR:String="author"
        var LIKESNO:String="likesNo"
        var IMAGEURL:String="imageUrl"
    }
    var CREATE_TABLE="CREATE TABLE "+ TABLE+" ( "+
            ID+" TEXT PRIMARY KEY , "+
            TITLE+" TEXT , "+
            DESCRIPTION+" TEXT , "+
            AUTHOR+" TEXT , "+
            LIKESNO+" INTEGER , "+
            IMAGEURL+" TEXT )";
    var DROP_TABLE="DROP TABLE IF EXISTS Article"

    constructor(c:Context) : super(c,"myexDB",null ,1) {
        context=c
    }
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_TABLE)
    }
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(DROP_TABLE)
        onCreate(p0)
    }
    fun insert(title: String, description: String, author: String, likesNo: Int, imageUrl: String, id: String  ): Long {
        var db=writableDatabase
        var cv=ContentValues()

        cv.put(TITLE,title)
        cv.put(DESCRIPTION,description)
        cv.put(AUTHOR,author)
        cv.put(LIKESNO,likesNo)
        cv.put(IMAGEURL,imageUrl)
        cv.put(ID,id)

        var id=db.insert(TABLE,null,cv)
        return id
    }
    @SuppressLint("Range")
    fun readArticles():ArrayList<Article>
    {
        var ls=ArrayList<Article>()
        var db=readableDatabase
        var c=db.query(TABLE,
            null,
            null,
            null,
            null,
            null,
            null)
        while(c.moveToNext()){
            var id=c.getString(c.getColumnIndex(ID))
            var title=c.getString(c.getColumnIndex(TITLE))
            var description=c.getString(c.getColumnIndex(DESCRIPTION))
            var author=c.getString(c.getColumnIndex(AUTHOR))
            var likesNo=c.getInt(c.getColumnIndex(LIKESNO))
            var imageUrl=c.getString(c.getColumnIndex(IMAGEURL))
            ls.add(Article(id,title,description,author,imageUrl,likesNo))

        }
        return ls

    }

    fun removeArticle(id:Int):Int{
        var db=writableDatabase
        var selectionArgs= arrayOf(id.toString())
        var id=db.delete(TABLE,ID+"=?",selectionArgs)
        return id
    }
}