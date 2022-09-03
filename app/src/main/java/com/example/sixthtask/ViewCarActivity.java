package com.example.sixthtask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class ViewCarActivity extends AppCompatActivity {

    private Toolbar toolbar ;
    private TextInputEditText et_model , et_color , et_Dpl , et_Desc ;
    private ImageView imageView;
    Uri ImageUri ;
    private DatabaseAccess DBA ;
    private String model , color , desc , image="" ;
    private double dpi ;
    static final int PIC_IMG_GAL = 5;
    private int carId = -1 ;

    // for menu :
    public static final int ADD_CAR_RES_CODE = 1;
    public static final int EDIT_CAR_RES_CODE = 2;
    public static final int DEL_CAR_RES_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view_car);

        toolbar = findViewById(R.id.AVCToolBar);
        setSupportActionBar(toolbar);
        et_model = findViewById(R.id.et_details_model);
        et_color = findViewById(R.id.et_details_color);
        et_Dpl = findViewById(R.id.et_details_DPL);
        et_Desc = findViewById(R.id.et_details_Desc);
        imageView = findViewById(R.id.DetailsImgView);

        DBA = DatabaseAccess.getInstance(this);

        Intent intent = getIntent();
        carId = intent.getIntExtra(MainActivity.CAR_KEY, carId);

        if(carId == -1){
            // adding process
            enableFields();
            // if there's a previous adding / editing process do :
            clearFields();
        }else{
            // show process
            disableFields();
            DBA.open();
            // get car info where id = * .
            Car c = DBA.getCar(carId);
            DBA.close();

            if (c != null ){
                fillCarToFields(c);
            }
        }

        // uploading pic from gallery :
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gal =  new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gal, PIC_IMG_GAL);
            }
        });

    }

    private void fillCarToFields (Car c){
        if(c.getImage() != null && !c.getImage().isEmpty()){
        imageView.setImageURI(Uri.parse(c.getImage()));}
        et_model.setText(c.getModel());
        et_color.setText(c.getColor());
        et_Dpl.setText(c.getDpl()+"");
        et_Desc.setText(c.getDescription());
    }

    // method to disable feilds when user click show button :
    private void disableFields (){
        imageView.setEnabled(false);
        et_model.setEnabled(false);
        et_color.setEnabled(false);
        et_Dpl.setEnabled(false);
        et_Desc.setEnabled(false);
    }

    private void enableFields (){
        imageView.setEnabled(true);
        et_model.setEnabled(true);
        et_color.setEnabled(true);
        et_Dpl.setEnabled(true);
        et_Desc.setEnabled(true);
    }

    private void clearFields (){
        imageView.setImageURI(null);
        et_model.setText("");
        et_color.setText("");
        et_Dpl.setText("");
        et_Desc.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu,menu);
        MenuItem save = menu.findItem(R.id.DetailsSave);
        MenuItem edit = menu.findItem(R.id.DetailsEdit);
        MenuItem delete = menu.findItem(R.id.DetailsDelete);

        if(carId == -1){
            // adding process
            save.setVisible(true);
            edit.setVisible(false);
            delete.setVisible(false);
        }else{
            // editing process
            save.setVisible(false);
            edit.setVisible(true);
            delete.setVisible(true);
        }

        return true;
    }

    //---------------------------------------------------- MENU --------------------------------------------------------------


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Boolean result ;
        DBA.open();

        switch (item.getItemId()) {
            case R.id.DetailsSave:
                model = et_model.getText().toString();
                color = et_color.getText().toString();
                desc = et_Desc.getText().toString();
                dpi = Double.parseDouble(et_Dpl.getText().toString());

                if (ImageUri!=null){image = ImageUri.toString();}

                // insert data to item :
                Car c = new Car(carId , model , color , dpi , image , desc);

                // how save button works if it's an editing process or inserting :
                // = -1 = insert process
                if(carId == -1){
                    result = DBA.insertCar(c) ;

                    // if process done successfully
                    if (result){
                    Toast.makeText(this,"Car Added successfully ",Toast.LENGTH_SHORT).show();
                    setResult(ADD_CAR_RES_CODE,null);   // null = no data to get back
                    finish(); }}

                // update process
                else {
                    result = DBA.updateCar(c) ;

                    // if process done successfully
                    if (result){
                    Toast.makeText(this,"Car updated successfully ",Toast.LENGTH_SHORT).show();
                    setResult(EDIT_CAR_RES_CODE,null);   // null = no data to get back
                    finish(); }}

                return true;

            case R.id.DetailsEdit:
                enableFields();
                MenuItem save = toolbar.getMenu().findItem(R.id.DetailsSave);
                MenuItem edit = toolbar.getMenu().findItem(R.id.DetailsEdit);
                MenuItem delete = toolbar.getMenu().findItem(R.id.DetailsDelete);
                save.setVisible(true);
                edit.setVisible(false);
                delete.setVisible(false);
                return true;

            case R.id.DetailsDelete:

                // delete process only needs ID so other values isn't necessary
                c = new Car(carId , null , null , 0 , null , null);

                // delete process
                    result = DBA.deleteCar(c) ;
                    if (result){
                    Toast.makeText(this,"Car deleted successfully From DataBase ",Toast.LENGTH_SHORT).show();
                    setResult(DEL_CAR_RES_CODE,null);   // null = no data to get back
                    finish(); }
                return true;
        }

        DBA.close();
        return false;
    }

    //--------------------------------------------IMAGE PIC RESULT--------------------------------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // FROM GALLERY
         if(requestCode == PIC_IMG_GAL && resultCode == RESULT_OK){
            if (null != data){
                // saving image from Galery directory in a var .
                ImageUri = data.getData();
                imageView.setImageURI(ImageUri);
                Toast.makeText(getBaseContext(),"DONE ",Toast.LENGTH_SHORT).show();}

            else if ( resultCode == RESULT_CANCELED){
                Toast.makeText(getBaseContext(),"CANCELED",Toast.LENGTH_SHORT).show(); } }
    }

}