package org.example;

import org.example.Model.Apartment;
import org.example.Model.Item;
import org.example.Model.Owner;
import org.example.Model.Thief;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final Random random = new Random();

    public static void main(String[] args) {
        try {
            int maxOwners = 5;
            int maxThieves = 5;

            int numberOfOwners = random.nextInt(maxOwners) + 1;
            int numberOfThieves = random.nextInt(maxThieves) + 1;

            Apartment apartment = new Apartment();
            List<Owner> owners = new ArrayList<>();
            List<Thief> thieves = new ArrayList<>();

            for (int i = 0; i < numberOfOwners; i++) {
                List<Item> ownerItems = Item.generateRandomItems();
                owners.add(new Owner("Owner-" + i, ownerItems, apartment));
            }

            for (int i = 0; i < numberOfThieves; i++) {
                int maxWeight = random.nextInt(10) + 1;
                thieves.add(new Thief("Thief-" + i, maxWeight, apartment));
            }

            System.out.println("Создалось хозяев: " + numberOfOwners + ", а воров: " + numberOfThieves);

            try (ExecutorService executor = Executors.newFixedThreadPool(numberOfOwners + numberOfThieves)) {
                CountDownLatch startLatch = new CountDownLatch(1);

                List<Runnable> allTasks = new ArrayList<>();
                owners.forEach(owner -> allTasks.add(() -> {
                    try {
                        startLatch.await();
                        owner.run();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }));
                thieves.forEach(thief -> allTasks.add(() -> {
                    try {
                        startLatch.await();
                        thief.run();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }));
                Collections.shuffle(allTasks);

                allTasks.forEach(executor::execute);
                startLatch.countDown();

                executor.shutdown();
                try {
                    if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                        // если вдруг какой-то поток зависнет, то программа завершится через минуту
                        System.err.println("Пул потоков не завершился за отведенное время");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Ожидание завершения пула прервано: " + e.getMessage());
                }
            }

            for (Thief thief : thieves) {
                System.out.println("Рюкзак после грабежа " + thief.getBackpack());
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка инициализации: " + e.getMessage());
        }
    }
}
