package org.example.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Apartment {
    private final List<Item> items = new ArrayList<>();
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock(true);
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    public void ownerAddItemsToApartment(List<Item> newItems, String name) {
        readLock.lock();
        try {
            items.addAll(newItems);
            System.out.println(name + " добавил " + newItems.size() + " вещей. Всего: " + items.size());
        } finally {
            readLock.unlock();
        }
    }

    public List<Item> steal(int maxWeight, String name) {
        writeLock.lock();
        try {
            if (items.isEmpty()) {
                System.out.println(name + " не нашел вещей для кражи");
                return Collections.emptyList();
            }

            List<Item> sorted = items.stream()
                    .sorted(Comparator.comparingDouble(Item::getValuePerWeight).reversed())
                    .toList();

            List<Item> stolen = new ArrayList<>();
            int currentWeight = 0;

            for (Item item : sorted) {
                if (currentWeight + item.getWeight() <= maxWeight) {
                    stolen.add(item);
                    currentWeight += item.getWeight();
                }
            }

            items.removeAll(stolen);
            System.out.println(name + " украл " + stolen.size() + " вещей");
            return stolen;
        } finally {
            writeLock.unlock();
        }
    }
}
