package com.example.sixthtask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyDatabase extends SQLiteAssetHelper {
// building DB class


    public static final String DB_NAME = "Car.db";
    public static final int DB_VERSION = 1 ;

    public static final String CAR_TB_NAME = "Car",
    CAR_COL_ID = "id" , CAR_COL_MODEL = "model" , CAR_COL_COLOR = "color" , CAR_COL_DPL = "DistancePerLiter" , CAR_COL_IMAGE = "image" , CAR_COL_DESC = "description" ;

    public MyDatabase(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //-------------------------------------------------------------------------



}
