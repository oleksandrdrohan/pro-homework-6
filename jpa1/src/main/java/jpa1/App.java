package jpa1;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    static EntityManagerFactory emf;
    static EntityManager em;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            emf = Persistence.createEntityManagerFactory("Menu");
            em = emf.createEntityManager();
            try {
                while (true) {
                    System.out.println("1: add dish");
                    System.out.println("2: delete dish");
                    System.out.println("3: change menu");
                    System.out.println("4: view menu");
                    System.out.println("5: view dishes with discount");
                    System.out.println("6: view dishes in the cost range");
                    System.out.println("7: view dishes within weight limit");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            addDish(sc);
                            break;
                        case "2":
                            deleteDish(sc);
                            break;
                        case "3":
                            changeDish(sc);
                            break;
                        case "4":
                            viewDishes();
                            break;
                        case "5":
                            viewDishesWithDiscount();
                            break;
                        case "6":
                            viewDishesInCostRange();
                            break;
                        case "7":
                            viewDishesWithWeightLimit();
                            break;
                        default:
                            return;
                    }
                }
            } finally {
                sc.close();
                em.close();
                emf.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    private static void addDish(Scanner sc) {
        System.out.print("Enter name of the dish: ");
        String name = sc.nextLine();
        System.out.print("Enter cost of the dish: ");
        String sCost = sc.nextLine();
        double cost = Double.parseDouble(sCost);
        System.out.println("Enter weight of the dish: ");
        String sWeight = sc.nextLine();
        int weight = Integer.parseInt(sWeight);
        System.out.print("Does the dish have a discount? (true or false): ");
        boolean discount = Boolean.parseBoolean(sc.nextLine());

        em.getTransaction().begin();
        try {
            Menu c = new Menu(name, cost, weight, discount);
            em.persist(c);
            em.getTransaction().commit();

            System.out.println(c.getId());
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    private static void deleteDish(Scanner sc) {
        System.out.print("Enter dish id: ");
        String sId = sc.nextLine();
        long id = Long.parseLong(sId);

        Menu c = em.getReference(Menu.class, id);
        if (c == null) {
            System.out.println("Dish is not found!");
            return;
        }

        em.getTransaction().begin();
        try {
            em.remove(c);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    private static void changeDish(Scanner sc) {
        System.out.print("Enter dish name: ");
        String name = sc.nextLine();

        System.out.print("Enter new cost: ");
        String sCost = sc.nextLine();
        int cost = Integer.parseInt(sCost);

        Menu c = null;
        try {
            Query query = em.createQuery("SELECT x FROM Menu x WHERE x.dishName = :name", Menu.class);
            query.setParameter("name", name);

            c = (Menu) query.getSingleResult();
        } catch (NoResultException ex) {
            System.out.println("Dish is not found!");
            return;
        } catch (NonUniqueResultException ex) {
            System.out.println("Non unique result!");
            return;
        }

        em.getTransaction().begin();
        try {
            c.setCost(cost);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    private static void viewDishes() {
        Query query = em.createQuery("SELECT c FROM Menu c", Menu.class);
        List<Menu> list = (List<Menu>) query.getResultList();

        for (Menu c : list)
            System.out.println(c);
    }

    private static void viewDishesWithDiscount() {
        Query query = em.createQuery("SELECT m FROM Menu m WHERE m.discount = true", Menu.class);
        List<Menu> list = (List<Menu>) query.getResultList();

        for (Menu m : list) {
            System.out.println(m);
        }
    }

    private static void viewDishesInCostRange() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter lower cost limit: ");
        String sLowerLimit = sc.nextLine();

        System.out.println("Enter higher cost limit: ");
        String sHigherLimit = sc.nextLine();

        if (sLowerLimit.isEmpty() || sHigherLimit.isEmpty()) {
            System.out.println("Please enter valid lower and higher cost limits.");
            return;
        }

        try {
            double lowerLimit = Double.parseDouble(sLowerLimit);
            double higherLimit = Double.parseDouble(sHigherLimit);

            Query query = em.createQuery("SELECT m FROM Menu m WHERE m.cost >= :lowerLimit AND m.cost <= :higherLimit", Menu.class);
            query.setParameter("lowerLimit", lowerLimit);
            query.setParameter("higherLimit", higherLimit);

            List<Menu> list = (List<Menu>) query.getResultList();

            for (Menu m : list) {
                System.out.println(m);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter valid numeric values for cost limits.");
        } finally {
            sc.close();
        }
    }

    private static void viewDishesWithWeightLimit() {
        Query query = em.createQuery("SELECT m FROM Menu m", Menu.class);
        List<Menu> allDishes = query.getResultList();

        List<Menu> selectedDishes = new ArrayList<>();
        int totalWeight = 0;

        for (Menu dish : allDishes) {
            if (totalWeight + dish.getWeight() <= 1000) {
                selectedDishes.add(dish);
                totalWeight += dish.getWeight();
            } else {
                break;
            }
        }
        for (Menu m : selectedDishes) {
            System.out.println(m);
        }
    }
}


