package org.example;

import org.example.Model.Item;
import org.example.Model.Owner;
import org.example.Model.Thief;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.example.Model.Item.generateRandomItems;
import static org.example.Model.Item.random;

public class Main {
    public static void main(String[] args) {

        try {
            int maxOwners = 10;
            int maxThieves = 8;

            int numberOfOwners = random.nextInt(maxOwners) + 1;
            int numberOfThieves = random.nextInt(maxThieves) + 1;

            System.out.println("Создалось хозяев: " + numberOfOwners + ", а воров: " + numberOfThieves);

            List<Owner> owners = new ArrayList<>();
            List<Thief> thieves = new ArrayList<>();

            for (int i = 0; i < numberOfOwners; i++) {
                List<Item> ownerItems = generateRandomItems();
                owners.add(new Owner("Owner-" + i, ownerItems));
            }

            for (int i = 0; i < numberOfThieves; i++) {
                int maxWeight = random.nextInt(10) + 1;
                thieves.add(new Thief("Thief-" + i, maxWeight));
            }

            List<Thread> allThreads = new ArrayList<>();
            allThreads.addAll(owners);
            allThreads.addAll(thieves);

            for (Thread thread : allThreads) {
                thread.start();
            }

            for (Thread thread : allThreads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Поток остановлен: " + e.getMessage());
                }
            }

            for (Thief thief : thieves) {
                System.out.println("Рюкзак после грабежа " + thief.getName() + ": " + thief.getBackpack());
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка инициализации: " + e.getMessage());
        }
    }
}
