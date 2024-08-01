package com.example.toolbox.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;


// 数据库类，用于管理用户账号、用户信息和关联关系
public class TotalUserDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TotalUserDatabase.db";
    private static final int DATABASE_VERSION = 1;

    // 用户账号表相关常量
    public static final String TABLE_USER_ACCOUNT = "users";
    public static final String COLUMN_USER_ACCOUNT_ID = "_id";
    public static final String COLUMN_USER_ACCOUNT_PHONE = "phone";

    // 用户信息表相关常量
    public static final String TABLE_USER_INFO = "userinfo";
    public static final String COLUMN_USER_INFO_ID = "_id";
    public static final String COLUMN_USER_INFO_USERNAME = "username";
    public static final String COLUMN_USER_INFO_AVATAR = "avatar";

    // 用户登录历史表相关常量
    public static final String TABLE_LOGIN_HISTORY = "login_history";
    public static final String COLUMN_HISTORY_ID = "_id";
    public static final String COLUMN_HISTORY_DATE_TIME = "date_time";
    public static final String COLUMN_HISTORY_PHONE_MODEL = "phone_model";
    public static final String COLUMN_HISTORY_LOGIN_AREA = "login_area";
    public static final String COLUMN_HISTORY_LOGIN_STATUS = "login_status";


    /// 关联表相关常量
    public static final String TABLE_ACCOUNT_ASSOCIATION = "account_association";
    public static final String COLUMN_ASSOCIATION_ID = "_id";
    public static final String COLUMN_ASSOCIATION_ACCOUNT_ID = "account_id";
    public static final String COLUMN_ASSOCIATION_USER_INFO_ID = "user_info_id";
    public static final String COLUMN_ASSOCIATION_LOGIN_HISTORY_ID = "login_history_id";


    // 创建用户账号表的 SQL 语句
    private static final String SQL_CREATE_USER_ACCOUNT_TABLE = "CREATE TABLE " +
            TABLE_USER_ACCOUNT + "(" +
            COLUMN_USER_ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USER_ACCOUNT_PHONE + " TEXT" +
            ")";

    // 创建用户信息表的 SQL 语句
    private static final String SQL_CREATE_USER_INFO_TABLE = "CREATE TABLE " +
            TABLE_USER_INFO + "(" +
            COLUMN_USER_INFO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USER_INFO_USERNAME + " TEXT, " +
            COLUMN_USER_INFO_AVATAR + " TEXT" +
            ")";

    // 创建登录历史表的 SQL 语句
    private static final String SQL_CREATE_LOGIN_HISTORY_TABLE = "CREATE TABLE " +
            TABLE_LOGIN_HISTORY + "(" +
            COLUMN_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_HISTORY_DATE_TIME + " TEXT NOT NULL, " +
            COLUMN_HISTORY_PHONE_MODEL + " TEXT NOT NULL, " +
            COLUMN_HISTORY_LOGIN_AREA + " TEXT NOT NULL," +
            COLUMN_HISTORY_LOGIN_STATUS + " TEXT NOT NULL" +
            ")";

    // 创建关联表的 SQL 语句
    private static final String SQL_CREATE_ACCOUNT_ASSOCIATION_TABLE = "CREATE TABLE " +
            TABLE_ACCOUNT_ASSOCIATION + "(" +
            COLUMN_ASSOCIATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ASSOCIATION_ACCOUNT_ID + " INTEGER, " +
            COLUMN_ASSOCIATION_USER_INFO_ID + " INTEGER, " +
            COLUMN_ASSOCIATION_LOGIN_HISTORY_ID + " INTEGER" +
            ")";


    // 删除表的 SQL 语句
    private static final String SQL_DELETE_USER_ACCOUNT_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_USER_ACCOUNT;
    private static final String SQL_DELETE_USER_INFO_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_USER_INFO;
    private static final String SQL_DELETE_LOGIN_HISTORY_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_LOGIN_HISTORY;
    private static final String SQL_DELETE_ACCOUNT_ASSOCIATION_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_ACCOUNT_ASSOCIATION;

    public TotalUserDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建用户账号表
        db.execSQL(SQL_CREATE_USER_ACCOUNT_TABLE);
        // 创建用户信息表
        db.execSQL(SQL_CREATE_USER_INFO_TABLE);
        // 创建登录历史表
        db.execSQL(SQL_CREATE_LOGIN_HISTORY_TABLE);
        // 创建关联表
        db.execSQL(SQL_CREATE_ACCOUNT_ASSOCIATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 如果表存在，则删除表
        db.execSQL(SQL_DELETE_USER_ACCOUNT_TABLE);
        db.execSQL(SQL_DELETE_USER_INFO_TABLE);
        db.execSQL(SQL_DELETE_LOGIN_HISTORY_TABLE);
        db.execSQL(SQL_DELETE_ACCOUNT_ASSOCIATION_TABLE);
        // 重新创建表
        onCreate(db);
    }

    // 插入用户账号
    public long insertUser(String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ACCOUNT_PHONE, phone);
        long id = db.insert(TABLE_USER_ACCOUNT, null, values);
        db.close();
        return id;
    }

    // 插入用户信息
    public long insertUserInfo(String username, String avatar) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_INFO_USERNAME, username);
        values.put(COLUMN_USER_INFO_AVATAR, avatar);
        long id = db.insert(TABLE_USER_INFO, null, values);
        db.close();
        return id;
    }

    // 建立用户账号和用户信息的关联关系
    public long insertAccountAssociation(long userInfoId, long accountId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ASSOCIATION_USER_INFO_ID, userInfoId);
        values.put(COLUMN_ASSOCIATION_ACCOUNT_ID, accountId);
        long id = db.insert(TABLE_ACCOUNT_ASSOCIATION, null, values);
        db.close();
        return id;
    }

    // 更新用户名
    public int updateUsername(String phone, String newUsername) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_INFO_USERNAME, newUsername);
        int rowsAffected = db.update(TABLE_USER_INFO, values, COLUMN_USER_ACCOUNT_PHONE + "=?", new String[]{phone});
        db.close();
        return rowsAffected;
    }

    // 清空登录历史记录
    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOGIN_HISTORY, null, null);
        db.close();
    }

    // 插入登录历史记录
    public long insertLoginHistory(String dateTime, String phoneModel, String loginStatus, String loginArea) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HISTORY_DATE_TIME, dateTime);
        values.put(COLUMN_HISTORY_PHONE_MODEL, phoneModel);
        values.put(COLUMN_HISTORY_LOGIN_STATUS, loginStatus);
        values.put(COLUMN_HISTORY_LOGIN_AREA, loginArea);
        long id = db.insert(TABLE_LOGIN_HISTORY, null, values);
        db.close();
        return id;
    }

    // 动态创建表
    public void createTable(String tableName, String[] columns) {
        SQLiteDatabase db = this.getWritableDatabase();
        String columnsStr = TextUtils.join(",", columns);
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + "(" + columnsStr + ")";
        db.execSQL(sql);
        db.close();
    }

    // 动态插入数据
    public long insertData(String tableName, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insert(tableName, null, values);
        db.close();
        return id;
    }

}
