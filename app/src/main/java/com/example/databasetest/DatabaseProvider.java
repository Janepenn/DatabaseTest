package com.example.databasetest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2016/9/8.
 */
public class DatabaseProvider extends ContentProvider {
    public static final int Book_DIR = 0;
    public static final int BOOK_ITEM = 1;
    public static final int CATEGORY_DIR = 2;
    public static final int CATEGORY_ITEM = 3;
    public static final String AUTHORITY = "com.example.databasetest.provider";
    public static UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "book", Book_DIR);
        uriMatcher.addURI(AUTHORITY, "book/#", BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY, "category", CATEGORY_DIR);
        uriMatcher.addURI(AUTHORITY, "category/#", CATEGORY_ITEM);
    }

    private MyDatabaseHelper dbhelper;

    @Override
    public boolean onCreate() {
        dbhelper = new MyDatabaseHelper(getContext(), "BookStore.db", null, 2);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case Book_DIR:
                cursor = db.query("book", strings, s, strings1, null, null, s1);
                break;
            case BOOK_ITEM:
                String bookid = uri.getPathSegments().get(1);
                cursor = db.query("book", strings, "id=?", new String[]{bookid}, null, null, s1);
                break;
            case CATEGORY_DIR:
                cursor = db.query("category", strings, s, strings1, null, null, s1);
                break;
            case CATEGORY_ITEM:
                String categoryid = uri.getPathSegments().get(1);
                cursor = db.query("category", strings, "id=?", new String[]{categoryid}, null, null, s1);
                break;
            default:
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case Book_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.databasetest.provider.book";
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.databasetest.provider.book";
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.databasetest.provider.category";
            case CATEGORY_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.databasetest.provider.category";
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Uri urireturn = null;
        switch (uriMatcher.match(uri)) {
            case Book_DIR:
            case BOOK_ITEM:
                long newbookid = db.insert("book", null, contentValues);
                urireturn = Uri.parse("content://" + AUTHORITY + "/book/" + newbookid);
                break;
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                long newcategoryid = db.insert("book", null, contentValues);
                urireturn = Uri.parse("content://" + AUTHORITY + "/book/" + newcategoryid);
                break;
            default:
                break;
        }
        return urireturn;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        int deleteRows = 0;
        switch (uriMatcher.match(uri)) {
            case Book_DIR:
                deleteRows = db.delete("book", s, strings);
                break;
            case BOOK_ITEM:
                String bookid = uri.getPathSegments().get(1);
                deleteRows = db.delete("book", "id=?", new String[]{bookid});
                break;
            case CATEGORY_DIR:
                deleteRows = db.delete("category", s, strings);
                break;
            case CATEGORY_ITEM:
                String categoryid = uri.getPathSegments().get(1);
                deleteRows = db.delete("category", "id=?", new String[]{categoryid});
                break;
            default:
                break;
        }
        return deleteRows;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        int updateRows = 0;
        switch (uriMatcher.match(uri)) {
            case Book_DIR:
                updateRows = db.update("book", contentValues, s, strings);
                break;
            case BOOK_ITEM:
                String bookid = uri.getPathSegments().get(1);
                updateRows = db.update("book", contentValues, "id=?", new String[]{bookid});
                break;
            case CATEGORY_DIR:
                updateRows = db.update("category", contentValues, s, strings);
                break;
            case CATEGORY_ITEM:
                String categoryid = uri.getPathSegments().get(1);
                updateRows = db.update("category", contentValues, "id=?", new String[]{categoryid});
                break;
            default:
                break;
        }
        return updateRows;
    }
}
