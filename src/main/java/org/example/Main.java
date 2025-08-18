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

    private static Runnable createTask(Runnable runnable, CountDownLatch latch) {
        return () -> {
            try {
                latch.await();
                runnable.run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
    }

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

            List<Item> initialItems = owners.stream()
                    .flatMap(owner -> owner.getOwnerItems().stream())
                    .toList();
            System.out.println("Контрольный список вещей Хозяев до запуска (" + initialItems.size() + "): " + initialItems);


            try (ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
                CountDownLatch startLatch = new CountDownLatch(1);

                List<Runnable> allTasks = new ArrayList<>();
                owners.forEach(owner -> allTasks.add(createTask(owner, startLatch)));
                thieves.forEach(thief -> allTasks.add(createTask(thief, startLatch)));
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

            // Собираем список вещей после завершения (в квартире + в рюкзаках)
            List<Item> finalItems = new ArrayList<>();
            finalItems.addAll(apartment.getItems());
            System.out.println("Вещи, оставшиеся в квартире (" + apartment.getItems().size() + "): " + apartment.getItems());
            for (Thief thief : thieves) {
                List<Item> stolenItems = thief.getBackpack().getStolenItems();
                System.out.println("Вещи в рюкзаке " + thief.getName() + " (" + stolenItems.size() + "): " + stolenItems);
                finalItems.addAll(stolenItems);
            }
            System.out.println("Итоговый список вещей после завершения (" + finalItems.size() + "): " + finalItems);

            // Сравниваем списки пообъектно
            boolean itemsMatch = initialItems.size() == finalItems.size() &&
                    initialItems.containsAll(finalItems) &&
                    finalItems.containsAll(initialItems);
            String resultMessage;
            if (itemsMatch) {
                resultMessage = "Все вещи учтены";
            } else {
                resultMessage = "Обнаружены расхождения";
            }
            System.out.println("Результат сравнения списков: " + resultMessage);
            if (!itemsMatch) {
                System.out.println("Изначальные вещи: " + initialItems);
                System.out.println("Итоговые вещи: " + finalItems);
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка инициализации: " + e.getMessage());
        }
    }
}
