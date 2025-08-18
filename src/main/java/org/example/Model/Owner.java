package org.example.Model;

import java.util.List;

public class Owner implements Runnable {
    private final String name;
    private final List<Item> ownerItems;
    private final Apartment apartment;

    public Owner(String name, List<Item> ownerItems, Apartment apartment) {
        this.name = name;
        this.ownerItems = ownerItems;
        this.apartment = apartment;
    }

    @Override
    public void run() {
        try {
            apartment.ownerAddItemsToApartment(ownerItems, name);
        } catch (IllegalArgumentException e) {
            System.out.println(name + " Ошибка при добавлении предметов: " + e.getMessage());
        }
    }

    public List<Item> getOwnerItems() {
        return ownerItems;
    }
}
