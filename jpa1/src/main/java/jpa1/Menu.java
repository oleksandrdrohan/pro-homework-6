package jpa1;

import javax.persistence.*;

@Entity
@Table(name="Menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private long id;

    @Column(nullable = false)
    private String dishName;
    @Column(nullable = false)
    private boolean discount;

    private double cost;
    private int weight;

    public Menu() {}

    public Menu(String dishName, double cost, int weight, boolean discount) {
        this.dishName = dishName;
        this.cost = cost;
        this.weight = weight;
        this.discount = discount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public double getCost() {
        return cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isDiscount() {
        return discount;
    }

    public void setDiscount(boolean discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "Menu: " +
                "id = " + id +
                ", dishName = '" + dishName + '\'' +
                ", discount = '" + discount + '\'' +
                ", cost = " + cost +
                ", weight = " + weight +
                '.';
    }
}
