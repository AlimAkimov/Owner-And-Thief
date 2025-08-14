package org.example.Model;

public class Item {
    private int weight;
    private int price;

    public Item(int weight, int price) {
        if (price < 0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("Вес не может быть отрицательным");
        }
        this.weight = weight;
        this.price = price;
    }

    public double getValuePerWeight() {
        return (double) price / weight;
    }

    public int getWeight() {
        return weight;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return " Вес= " + weight + ", стоимость= " + price;
    }
}
