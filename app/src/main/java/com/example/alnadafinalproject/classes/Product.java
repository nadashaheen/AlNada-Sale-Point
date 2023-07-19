package com.example.alnadafinalproject.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {

    private String id;
    private String name;
    private int code;
    private double price;
    private double wholesale;
    private int unit;
    private String description;
    private String cat_id;
    private String sup_id;
    private String product_img;


    public Product() {

    }

    public Product(String id, String name, int code, double price, double wholesale, int unit, String description, String cat_id, String sup_id, String product_img) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.price = price;
        this.wholesale = wholesale;
        this.unit = unit;
        this.description = description;
        this.cat_id = cat_id;
        this.sup_id = sup_id;
        this.product_img = product_img;
    }

    protected Product(Parcel in) {
        id = in.readString();
        name = in.readString();
        code = in.readInt();
        price = in.readDouble();
        unit = in.readInt();
        description = in.readString();
        cat_id = in.readString();
        sup_id = in.readString();
        product_img = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getWholeSale() {
        return wholesale;
    }

    public void setWholeSale(double wholesale) {
        this.wholesale = wholesale;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getSup_id() {
        return sup_id;
    }

    public void setSup_id(String sup_id) {
        this.sup_id = sup_id;
    }

    public String getProImg() {
        return product_img;
    }

    public void setProImg(String product_img) {
        this.product_img = product_img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeInt(code);
        dest.writeDouble(price);
        dest.writeInt(unit);
        dest.writeString(description);
        dest.writeString(cat_id);
        dest.writeString(sup_id);
        dest.writeString(product_img);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code=" + code +
                ", price=" + price +
                ", wholesale=" + wholesale +
                ", unit=" + unit +
                ", description='" + description + '\'' +
                ", cat_id='" + cat_id + '\'' +
                ", sup_id='" + sup_id + '\'' +
                ", product_img='" + product_img + '\'' +
                '}';
    }
}
