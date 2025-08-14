package org.example.Model;

import java.util.ArrayList;
import java.util.List;

public class Backpack {
    private int maximumWeight;
    private int currentWeight = 0;
    private int totalPrice = 0;
    private List<Item> stolenItems = new ArrayList<>();

    public Backpack(int maximumWeight) {
        if (maximumWeight < 0) {
            throw new IllegalArgumentException("Вес не может быть отрицательным");
        }
        this.maximumWeight = maximumWeight;
    }

    public void addItemsToBackpack(List<Item> apartmentItems) {
        if (apartmentItems == null) {
            throw new IllegalArgumentException("Список вещей не может быть null");
        }
        for (Item item : apartmentItems) {
            if (currentWeight + item.getWeight() > maximumWeight) {
                throw new IllegalArgumentException("Невозможно добавить вещь в рюкзак, превышен максимальный вес");
            }
            stolenItems.add(item);
            currentWeight += item.getWeight();
            totalPrice += item.getPrice();
        }
    }

    public int getMaximumWeight() {
        return maximumWeight;
    }


    @Override
    public String toString() {
        return "Рюкзак(" +
                "Вместимость= " + maximumWeight +
                ", Текущий вес= " + currentWeight +
                ", Общая стоимость= " + totalPrice +
                ", Украденные вещи= " + stolenItems;
    }
}
