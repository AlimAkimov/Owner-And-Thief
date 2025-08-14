package org.example.Model;

import java.util.List;

import static org.example.Model.Item.random;

public class Thief extends Thread {
    private Backpack backpack;


    public Thief(String name, int maximumWeight) {
        super(name);
        this.backpack = new Backpack(maximumWeight);
    }

    public void run() {
        try {
            Thread.sleep(random.nextInt(50)); // Случайная задержка 0–50 мс
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        try {
            List<Item> stolen = Apartment.steal(backpack.getMaximumWeight());
            if (!stolen.isEmpty()) {
                backpack.addItemsToBackpack(stolen);
                System.out.println(Thread.currentThread().getName() + " вор украл: " + backpack);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(Thread.currentThread().getName() + " ошибка при воровстве " + e.getMessage());
        }
    }

    public Backpack getBackpack() {
        return backpack;
    }


}
