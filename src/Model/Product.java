package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Public Product class
 */

/**
 * @author Tricia Smith
 */

    public class Product {
        private int id;
        private String name;
        private double price;
        private int stock;
        private int min;
        private int max;
        private ObservableList<Part> asscParts = FXCollections.observableArrayList();

    /**
     * product class constructors
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param asscParts
     */
        public Product(int id, String name, double price, int stock, int min, int max, ObservableList<Part> asscParts) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.stock = stock;
            this.min = min;
            this.max = max;
            this.asscParts = asscParts;
        }

    /**
     * sets the Product class getters and setters
     * @return
     */
        public int  getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

    /**
     * returns all current associated parts to a product
     * @return
     */
        public ObservableList<Part> getAsscParts() {
            return asscParts;
        }

    /**
     * sets the associated parts to a product
     * @param asscParts
     */
        public  void setAsscParts (ObservableList<Part> asscParts) {this.asscParts = asscParts;
        }

    /**
     * adds an associated part
     * @param part
     */
        public void addAsscPart(Part part){asscParts.add(part);}

    /**
     * deletes an associated part from the product
     * @param selectedPart
     */
        public void deleteAsscPart (Part selectedPart) {asscParts.remove(selectedPart);}
    }
