package org.example.Model;

import java.util.List;

import static org.example.Model.Item.random;

public class Owner extends Thread {
    private List<Item> ownerItems;

    public Owner(String name, List<Item> ownerItems) {
        super(name);
        this.ownerItems = ownerItems;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(random.nextInt(50)); // Случайная задержка 0–50 мс
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        try {
            Apartment.ownerAddItemsToApartment(ownerItems);
        } catch (IllegalArgumentException e) {
            System.out.println(Thread.currentThread().getName() + "Ошибка при добавлении предметов: " + e.getMessage());
        }
    }

}
