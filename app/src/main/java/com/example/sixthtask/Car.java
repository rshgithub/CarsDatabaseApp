package com.example.sixthtask;

public class Car {
    private String Model , Color , Image , Description;
    private int id;
    private double Dpl ;

    // constructor with id to recall data from table
    public Car(int id, String model, String color,  double dpl, String image, String description) {
        Model = model;
        Color = color;
        Image = image;
        Description = description;
        this.id = id;
        Dpl = dpl;
    }

    // constructor withOUT id to insert data to table
    public Car(String model, String color,  double dpl, String image, String description) {
        Model = model;
        Color = color;
        Image = image;
        Description = description;
        Dpl = dpl;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDpl() {
        return Dpl;
    }

    public void setDpl(double dpl) {
        Dpl = dpl;
    }
}
