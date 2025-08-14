package org.example.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Item {
    private int weight;
    private int price;
    public static Random random = new Random();

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

    public static List<Item> generateRandomItems() {
        int itemCount = random.nextInt(5) + 1;
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < itemCount; i++) {
            int weight = random.nextInt(10) + 1;
            int value = random.nextInt(50) + 1;
            items.add(new Item(weight, value));
        }
        return items;
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
