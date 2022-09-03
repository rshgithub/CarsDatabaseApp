package com.example.sixthtask;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CarRVAdapter extends RecyclerView.Adapter<CarRVAdapter.CarViewHolder> {

    private ArrayList<Car> cars ;
    private onRecyclerViewItemClickListener listener ;


    public ArrayList<Car> getCars() {
        return cars;
    }

    public void setCars(ArrayList<Car> cars) {
        this.cars = cars;
    }

    // constructor :
    public CarRVAdapter(ArrayList<Car> cars , onRecyclerViewItemClickListener listener) {
        this.cars = cars ;
        this.listener = listener ; }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custon_car_layout,null,false);
        CarViewHolder CVH = new CarViewHolder(v);
        return CVH ; }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {

        Car c = cars.get(position);

        if(c.getModel() != null && !c.getModel().isEmpty()){ holder.tv_Model.setText(c.getModel()); }

        if(c.getColor() != null && !c.getColor().isEmpty()){ holder.tv_Color.setText(c.getColor()); }
        // change color text color as color input :
        try {holder.tv_Color.setTextColor(Color.parseColor(c.getColor())); } catch (Exception e){ }

        if(c.getDpl() != 0){ holder.tv_Dpl.setText(String.valueOf(c.getDpl())); }

        // parsing image from uri to string BC DB store's strings when saving image .
        // parsing image from string to uri when calling image from DB , BC ImageView is Uri .
        if(c.getImage() != null && !c.getImage().isEmpty()){ holder.image.setImageURI(Uri.parse(c.getImage())); }
        // if user didn't insert image from Gallery set default image from drawable :
        else{ holder.image.setImageResource(R.drawable.carimage); }

        holder.share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Sending_Text = "Check out this " + c.getModel() + " New Car Model , it's only " + c.getDpl() + " Distance per liter " ;
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, Sending_Text);
                view.getContext().startActivity(Intent.createChooser(shareIntent, "Send To")); }});

        // Tag is saving hidden var inside any item above and recall it from external file
        holder.id= c.getId();

    }

    @Override
    public int getItemCount() {
        return cars.size();
    }



    // Holder Class --------------------------------------------------------------------------------------
    class CarViewHolder extends RecyclerView.ViewHolder {

        TextView tv_Model  , tv_Color , tv_Dpl ;
        ImageView image ;
        Button share_btn ;
        int id ;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Model = itemView.findViewById(R.id.Card_Car_Model);
            tv_Color = itemView.findViewById(R.id.Card_Car_color);
            tv_Dpl = itemView.findViewById(R.id.Card_Car_DPL);
            share_btn = itemView.findViewById(R.id.Share_btn);
            image = itemView.findViewById(R.id.imageView);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // when image is clicked :
                    listener.onItemClick(id);
                }
            });

        }
}
}
