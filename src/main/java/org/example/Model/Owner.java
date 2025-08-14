package org.example.Model;

import java.util.List;

public class Owner extends Thread {
    private List<Item> ownerItems;

    public Owner(String name, List<Item> ownerItems) {
        super(name);
        this.ownerItems = ownerItems;
    }

    @Override
    public void run() {
        try {
            Apartment.ownerAddItemsToApartment(ownerItems);
        } catch (IllegalArgumentException e) {
            System.out.println(Thread.currentThread().getName() + "Ошибка при добавлении предметов: " + e.getMessage());
        }
    }

}
