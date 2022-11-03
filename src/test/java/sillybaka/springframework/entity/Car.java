package sillybaka.springframework.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Date: 2022/10/15
 * Time: 16:15
 *
 * @Author SillyBaka
 * Description：
 **/
public class Car {
    private String brand;
    private double price;
    private String owner;

    private CarRoll carRoll;

    public Car(){

    }
    public Car(String brand, double price, String owner, CarRoll carRoll) {
        this.brand = brand;
        this.price = price;
        this.owner = owner;
        this.carRoll = carRoll;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public CarRoll getCarRoll() {
        return carRoll;
    }

    public void setCarRoll(CarRoll carRoll) {
        this.carRoll = carRoll;
    }

    @Override
    public String toString() {
        return "Car{" +
                "brand='" + brand + '\'' +
                ", price=" + price +
                ", owner='" + owner + '\'' +
                ", carRoll=" + carRoll +
                '}';
    }
}
