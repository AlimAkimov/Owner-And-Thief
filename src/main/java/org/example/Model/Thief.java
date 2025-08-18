package org.example.Model;
import java.util.List;

public class Thief implements Runnable {
    private final String name;
    private final Backpack backpack;
    private final Apartment apartment;

    public Thief(String name, int maximumWeight, Apartment apartment) {
        this.name = name;
        this.backpack = new Backpack(maximumWeight);
        this.apartment = apartment;
    }

    @Override
    public void run() {
        try {
            List<Item> stolen = apartment.steal(backpack.getMaximumWeight(), name);
            if (!stolen.isEmpty()) {
                backpack.addItemsToBackpack(stolen);
                System.out.println(name + " вор украл: " + backpack);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(name + " ошибка при воровстве " + e.getMessage());
        }

    }

    public Backpack getBackpack() {
        return backpack;
    }

    public String getName() {
        return name;
    }

    public Apartment getApartment() {
        return apartment;
    }
}

