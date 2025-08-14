package org.example.Model;

import java.util.List;

public class Thief extends Thread {
    private Backpack backpack;


    public Thief(String name, int maximumWeight) {
        super(name);
        this.backpack = new Backpack(maximumWeight);
    }

    public void run() {
        try {
            List<Item> stolen = Apartment.steal(backpack.getMaximumWeight());
            if (!stolen.isEmpty()) {
                backpack.addItemsToBackpack(stolen);
                System.out.println(Thread.currentThread().getName() + " Вор украл в рюкзак: " + backpack);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(Thread.currentThread().getName() + " Ошибка при воровстве " + e.getMessage());
        }
    }

    public Backpack getBackpack() {
        return backpack;
    }


}
