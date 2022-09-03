package com.example.sixthtask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseAccess {
// access DB clas
    private SQLiteDatabase database ; // to make instace / obj from the DB and get writeable and readable
    private SQLiteOpenHelper openHelper ; // to open and close DB
    private static DatabaseAccess instance ;

    // Singleton Design Pattern :
    // if there's no obj from Db then Create it , if there's one already then use it .
    // this method stops user from making obj's from DB the known way .
    private DatabaseAccess(Context context) {
        this.openHelper = new MyDatabase(context);
    }

    // the method will be used from user to create BD obj's
    public static DatabaseAccess getInstance(Context context){
        // if there's no obj already created then Create one
        if(instance==null){
            instance = new DatabaseAccess(context); }
        // if there's obj already created then use it
        return instance; }

        // open Db to use it and close it to able another user to use it .
    public void open () {
        this.database = this.openHelper.getWritableDatabase();  }

    public void close (){
        // if Db obj exists and opened to wrtite in .
        if(this.database!=null){ this.database.close(); } }

//-------------------------------------------------------------------------

    public boolean insertCar(Car car) {
        ContentValues CV = new ContentValues();
        CV.put(MyDatabase.CAR_COL_MODEL,car.getModel());
        CV.put(MyDatabase.CAR_COL_COLOR,car.getColor());
        CV.put(MyDatabase.CAR_COL_DPL,car.getDpl());
        CV.put(MyDatabase.CAR_COL_IMAGE,car.getImage());
        CV.put(MyDatabase.CAR_COL_DESC,car.getDescription());

        long result = database.insert(MyDatabase.CAR_TB_NAME,null,CV);
        // if inserting return true > 1 if false = -1
        return result != -1 ;
    }

    public boolean updateCar(Car car) {
        ContentValues CV = new ContentValues();
        CV.put(MyDatabase.CAR_COL_MODEL,car.getModel());
        CV.put(MyDatabase.CAR_COL_COLOR,car.getColor());
        CV.put(MyDatabase.CAR_COL_DPL,car.getDpl());
        CV.put(MyDatabase.CAR_COL_IMAGE,car.getImage());
        CV.put(MyDatabase.CAR_COL_DESC,car.getDescription());

        // string array so should convert any num to array .
        // array because we can add new columns to update .
        String args [] = {String.valueOf(car.getId())};
        long result = database.update(MyDatabase.CAR_TB_NAME,CV,"id=?",args);
        // number of columns updated
        return result > 0 ;
    }

    public boolean getCarsCount () {
        // "where clause is optional in update query " we can add where clause to return specific columns by doing for EX :
        //String args [] = {String.valueOf(car.getId())}; >>> then >>> (CAR_TB_NAME,CV,"id=?",args);

        long result = DatabaseUtils.queryNumEntries(database,MyDatabase.CAR_TB_NAME);
        return result > 0 ;
    }

    public boolean deleteCar(Car car) {

        // "where clause is obligatory in delete query "
        // delete rows as condition ( delete where id = X ) :
        String args [] = {String.valueOf(car.getId())};
        long result = database.delete(MyDatabase.CAR_TB_NAME,"id=?",args);
        // number of rows deleted
        return result > 0 ;
    }


    public ArrayList<Car> getAllCars() {
        // return all cars :
        ArrayList<Car> cars = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + MyDatabase.CAR_TB_NAME,null );
        //cursor.moveToFirst(); >>> returns boolean , if there's an obj returns true , else returns false .

        // to know if cursor contains data or not :
        if(cursor != null && cursor.moveToFirst()){
            do {
                int id = cursor.getInt(cursor.getColumnIndex(MyDatabase.CAR_COL_ID));
                String Model = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_COL_MODEL));
                String Color = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_COL_COLOR));
                double Dpl = cursor.getInt(cursor.getColumnIndex(MyDatabase.CAR_COL_DPL));
                String Image = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_COL_IMAGE));
                String Description = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_COL_DESC));
                // call constructor :
                Car c = new Car(id,Model,Color,Dpl,Image,Description);
                cars.add(c);

            }while (cursor.moveToNext());
            cursor.close();
        }

        return cars;
    }

    public Car getCar(int carId) {

        Cursor cursor = database.rawQuery("SELECT * FROM " + MyDatabase.CAR_TB_NAME + " WHERE " + MyDatabase.CAR_COL_ID + " =? ",new String[] {String.valueOf(carId)});
        //cursor.moveToFirst(); >>> returns boolean , if there's an obj returns true , else returns false .

        // to know if cursor contains data or not :
        if(cursor != null && cursor.moveToFirst()){

            int id = cursor.getInt(cursor.getColumnIndex(MyDatabase.CAR_COL_ID));
            String Model = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_COL_MODEL));
            String Color = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_COL_COLOR));
            double Dpl = cursor.getInt(cursor.getColumnIndex(MyDatabase.CAR_COL_DPL));
            String Image = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_COL_IMAGE));
            String Description = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_COL_DESC));
            // call constructor :
            Car c = new Car(id,Model,Color,Dpl,Image,Description);

            cursor.close();
            return c;
        }

        return null;
    }

    // model search method
    public ArrayList<Car> getCars(String modelSearch) {

        // return all cars :
        ArrayList<Car> cars = new ArrayList<>();

        //like "select" method but contains a condition " where " to do some search  .
        Cursor cursor = database.rawQuery("SELECT * FROM " + MyDatabase.CAR_TB_NAME + " WHERE " + MyDatabase.CAR_COL_MODEL + " LIKE ? ",new String[] {"%" + modelSearch + "%"});

        // to know if cursor contains data or not :
        if(cursor != null && cursor.moveToFirst()){
            do {
                int id = cursor.getInt(cursor.getColumnIndex(MyDatabase.CAR_COL_ID));
                String Model = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_COL_MODEL));
                String Color = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_COL_COLOR));
                double Dpl = cursor.getInt(cursor.getColumnIndex(MyDatabase.CAR_COL_DPL));
                String Image = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_COL_IMAGE));
                String Description = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_COL_DESC));
                // call constructor :
                Car c = new Car(id,Model,Color,Dpl,Image,Description);
                cars.add(c);

            }while (cursor.moveToNext());
            cursor.close();
        }

        return cars;
    }

}
