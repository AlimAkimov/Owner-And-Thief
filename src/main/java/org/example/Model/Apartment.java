package org.example.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Apartment {
    private static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private static List<Item> apartmentsItems = Collections.synchronizedList(new ArrayList<>());

    public Apartment(ReentrantReadWriteLock readWriteLock, List<Item> apartmentsItems) {
        Apartment.readWriteLock = readWriteLock;
        Apartment.apartmentsItems = apartmentsItems;
    }

    public static void ownerAddItemsToApartment(List<Item> ownerItems) {
        if (ownerItems == null) {
            throw new IllegalArgumentException("Cписок добавляемых предметов не может быть null ");
        }
        ownerItems.forEach(item -> {
            if (item == null) {
                throw new IllegalArgumentException("Предмет из списка ownerItems не может быть null");
            }
        });
        readWriteLock.readLock().lock();
        try {
            apartmentsItems.addAll(ownerItems);
            synchronized (apartmentsItems) {
                System.out.println(Thread.currentThread().getName() + "Хозяин добавил вещи и теперь в квартире их: " + apartmentsItems.size());
            }
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public static List<Item> steal(int maximumWeight) {
        if (maximumWeight <= 0) {
            throw new IllegalArgumentException("Вес должен быть положительным");
        }
        readWriteLock.writeLock().lock();
        try {
            if (apartmentsItems.isEmpty()) {
                System.out.println(Thread.currentThread().getName() + " Не оказалось вещей для воровства.");
                return new ArrayList<>();
            }
            //копирование перед сортировкой
            List<Item> currentItems = apartmentsItems.stream()
                    .sorted(Comparator.comparingDouble(Item::getValuePerWeight).reversed())
                    .toList();
            System.out.println(Thread.currentThread().getName() + " отсортированы вещи по соотношению цена/вес: " + currentItems);

            List<Item> selectedToSteal = new ArrayList<>();
            int currentWeight = 0;
            int totalPrice = 0;

            for (Item item : currentItems) {
                if (currentWeight + item.getWeight() <= maximumWeight) {
                    selectedToSteal.add(item);
                    currentWeight += item.getWeight();
                    totalPrice += item.getPrice();
                }
            }
            apartmentsItems.removeAll(selectedToSteal);
            System.out.println(Thread.currentThread().getName() + " украл " + selectedToSteal.size() +
                    " вещей на общую сумму " + totalPrice + ". Осталось вещей: " + apartmentsItems.size());
            return selectedToSteal;
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

}
