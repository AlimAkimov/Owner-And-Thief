package org.example.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class Item {
    private final int weight;
    private final int price;
    private final long id; // Уникальный идентификатор
    private static final Random random = new Random();
    private static final AtomicLong idGenerator = new AtomicLong(0);

    public Item(int weight, int price) {
        if (price < 0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("Вес не может быть отрицательным");
        }
        this.weight = weight;
        this.price = price;
        this.id = idGenerator.getAndIncrement(); // Уникальный ID для каждого Item
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

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return weight == item.weight && price == item.price && id == item.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(weight, price, id);
    }

    @Override
    public String toString() {
        return String.format("Item-%d (%dkg, %d$)", id, weight, price);
    }
}
