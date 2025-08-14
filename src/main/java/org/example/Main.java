package org.example;

import org.example.Model.Item;
import org.example.Model.Owner;
import org.example.Model.Thief;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        Необходимо реализовать многопоточное приложение, которое решает следующую задачу:
//        Есть два типа пользователя (два типа потока).
//        Один - Хозяин, имеет в своем арсенале список вещей (Вещь: цена и вес),
//        второй - Вор, имеет рюкзак (Рюкзак: предельный вес, который может в себя вместить).
//        Поток Хозяина выполняет работу по выкладыванию вещей в квартиру.
//        Поток Вора - забирает вещи из квартиры. При этом Вор должен забрать такие вещи,
//        чтобы их ценность была максимальной и вес их должен быть меньше предельного веса,
//        который может поместиться в рюкзак.
//
//        Объектные модели:
//        1. Вещь; атрибуты: вес, ценность
//        2. Хозяин; атрибуты: Вещи; действия: внести вещи в квартиру
//        3. Рюкзак; атрибуты: предельный вес
//        3. Вор; атрибуты: рюкзак. Действия: сложить вещи в рюкзак.
//
//        Ограничения:
//        1. Если работает поток Хозяина, то вор не должен класть вещи в рюкзак.
//        2. Если работает Вор, то Хозяин не может войти в квартиру
//
//        Возможные ограничения системы:
//        1. Хозяев может быть 1..n.
//        2. потоки Хозяев БЕЗ взаимной блокировки: несколько хозяев могут выкладывать вещи в квартиру одновременно
//        3. Воров может быть 1..m.
//        4. Потоки Воров со ВЗАИМНОЙ блокировкой: воровать одновременно может только 1 вор.
        try {
            List<Item> owner1Items = Arrays.asList(new Item(1, 10), new Item(3, 8));
            List<Item> owner2Items = Arrays.asList(new Item(2, 13), new Item(4, 30));
            List<Item> owner3Items = Arrays.asList(new Item(5, 5), new Item(3, 9));

            Owner owner1 = new Owner("Owner-1", owner1Items);
            Owner owner2 = new Owner("Owner-2", owner2Items);
            Owner owner3 = new Owner("Owner-3", owner3Items);

            Thief thief1 = new Thief("Thief-1", 4);
            Thief thief2 = new Thief("Thief-2", 5);

            owner1.start();
            owner2.start();
            owner3.start();


            try {
                owner1.join();
                owner2.join();
                owner3.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Поток прерван: " + e.getMessage());
            }

            thief1.start();
            thief2.start();

            try {
                thief1.join();
                thief2.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Поток прерван: " + e.getMessage());
            }

            System.out.println("Финальное состояние рюкзака Thief-1: " + thief1.getBackpack());
            System.out.println("Финальное состояние рюкзака: " + thief2.getBackpack());
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка при инициализации: " + e.getMessage());
        }
        }

    }