package com.booleanuk.extension;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShopHandlerTest {
    double delta = 0.0001;
    @Test
    public void showItemsShowsAllItemsThatAreNotFillings() {
        ShopHandler sh = new ShopHandler();
        String list = sh.showItems();
//        System.out.println(list);
        for (Item item : ShopHandler.getStock()) {
            if (!item.getName().equals("Filling")) {
                // if item.name & price is in list, good
                assertTrue(list.contains(item.getName() + "\t" + item.getVariant() + (item.getVariant().length() > 6 ? "\t" : "\t\t") + item.getPrice()));
            }
        }
    }

    @Test
    public void showFillingsWorks() {
        ShopHandler sh = new ShopHandler();
        String list = sh.showFillings();
//        System.out.println(list);
        for (Item item : ShopHandler.getStock()) {
            if (item.getName().equals("Filling")) {
                // if item.name & price is in list, good
                assertTrue(list.contains(item.getVariant()));
            }
        }
    }

    @Test
    public void showBagelsWorks() {
        ShopHandler sh = new ShopHandler();
        String list = sh.showBagels();
//        System.out.println(list);
        for (Item item : ShopHandler.getStock()) {
            if (item.getName().equals("Bagel")) {
                // if item.name & price is in list, good
                assertTrue(list.contains(item.getVariant() + (item.getVariant().length() > 6 ? "\t" : "\t\t") + item.getPrice()));
            }
        }
    }

    @Test
    public void showCoffeesWorks() {
        ShopHandler sh = new ShopHandler();
        String list = sh.showCoffees();
//        System.out.println(list);
        for (Item item : ShopHandler.getStock()) {
            if (item.getName().equals("Coffee")) {
                // if item.name & price is in list, good
                assertTrue(list.contains(item.getVariant() + (item.getVariant().length() > 6 ? "\t" : "\t\t") + item.getPrice()));
            }
        }
    }

    @Test
    public void sixpackBagelsDiscount() {
        double delta = 0.0001;
        ShopHandler sh = new ShopHandler();
        for (int i=0; i<7; i++) {
            sh.orderBagel("Onion");
        }
        assertEquals(0.45, sh.calculateDiscounts(), delta);
    }

    @Test
    public void dozenBagelsDiscount() {
        double delta = 0.0001;
        ShopHandler sh = new ShopHandler();
        int oldCapacity = Basket.getCapacity();
        Basket.setCapacity(20);
        for (int i=0; i<13; i++) {
            sh.orderBagel("Onion");
        }
        assertEquals(1.89, sh.calculateDiscounts(), delta);
    Basket.setCapacity(oldCapacity);
    }

    @Test
    public void dozenAndSixBagelsDiscount() {
        ShopHandler sh = new ShopHandler();
        int oldCapacity = Basket.getCapacity();
        Basket.setCapacity(20);
        for (int i=0; i<19; i++) {
            sh.orderBagel("Onion");
        }
        assertEquals(2.34, sh.calculateDiscounts(), delta);
        Basket.setCapacity(oldCapacity);
    }

    @Test
    public void coffeeDiscount() {
        ShopHandler sh = new ShopHandler();
        sh.orderCoffee("Black", "Onion");
        assertEquals(0.23, sh.calculateDiscounts(), delta);
    }

    @Test
    public void comboTest() {
        ShopHandler sh = new ShopHandler();
        int oldCapacity = Basket.getCapacity();
        Basket.setCapacity(100);
        sh.orderBagel("Onion");
        sh.orderBagel("Onion");
        for (int i=0; i<12; i++) {
            sh.orderBagel("Plain");
        }
        for (int i=0; i<6; i++) {
            sh.orderBagel("Everything");
        }
        for (int i=0; i<3; i++) {
            sh.orderCoffee("Black");
        }
        assertEquals(10.43, sh.getCostAfterDiscounts(), delta);
        Basket.setCapacity(oldCapacity);
    }

    @Test
    public void nonemptyBasketPrintsNonemptyReceipt() {
        ShopHandler sh = new ShopHandler();
        int oldCapacity = Basket.getCapacity();
        Basket.setCapacity(100);
        sh.orderBagel("Onion");
        sh.orderBagel("Onion");
        for (int i=0; i<12; i++) {
            sh.orderBagel("Plain");
        }
        for (int i=0; i<6; i++) {
            sh.orderBagel("Everything");
        }
        for (int i=0; i<3; i++) {
            sh.orderCoffee("Black");
        }
        sh.orderCoffee("Cappuccino", "Plain");
        assertNotEquals("", sh.showReceiptWithDiscounts());
        Basket.setCapacity(oldCapacity);
    }

    @Test
    public void comboBasketPrintsNonemptyReceipt() {
        ShopHandler sh = new ShopHandler();
        int oldCapacity = Basket.getCapacity();
        Basket.setCapacity(100);
        sh.orderBagel("Onion", "Ham");
        sh.orderBagel("Onion", "Ham");
        for (int i=0; i<12; i++) {
            sh.orderBagel("Plain", "Cheese");
        }
        for (int i=0; i<6; i++) {
            sh.orderBagel("Everything");
        }
        for (int i=0; i<3; i++) {
            sh.orderCoffee("Black");
        }
        sh.orderCoffee("Cappuccino", "Plain");
//        System.out.println(sh.showReceiptWithDiscounts());
        assertEquals(
                """
                        Bob's Bagels
                        --------
                        2 Bagel Onion 0.98
                        12 Bagel Plain 3.99
                        (-0.69)
                        6 Bagel Everything 2.49
                        (-0.45)
                        3 Coffee Black 2.97
                        1 Coffee Cappuccino 1.25
                        (-0.43)
                        12 Filling Cheese 1.44
                        2 Filling Ham 0.24
                        --------
                        Total: 13.36
                        Savings: 1.57"""
                , sh.showReceiptWithDiscounts());
        Basket.setCapacity(oldCapacity);
    }
}
