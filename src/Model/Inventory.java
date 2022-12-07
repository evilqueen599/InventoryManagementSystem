package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Public Inventory Class
 */

/**
 * @author Tricia Smith
 */


public class Inventory {

  private static final ObservableList<Part> allParts = FXCollections.observableArrayList();

  private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

  /**
   * adds a part to the all parts list
   * @param newPart
   */
  public static void addPart(Part newPart) {
    allParts.add(newPart);
  }

  /**
   * adds a product to the all products list
   * @param newProduct
   */
  public static void addProduct(Product newProduct) {
    allProducts.add(newProduct);
  }

  /**
   * searches the part list for a part ID match
   * @param partId
   * @return
   */
  public static Part lookupPart(int partId) {
    for(int i = 0, partSize = allParts.size(); i < partSize; i++) {
      Part pt = allParts.get(i);
      if (pt.getId() == partId)
        return pt;
    }
    return null;
  }

  /**
   * searches the product list for a product ID match
   * @param productId
   * @return
   */
  public static Product lookupProduct(int productId) {
    for (int i = 0, productSize = allProducts.size(); i < productSize; i++) {
      Product prd = allProducts.get(i);
      if (prd.getId() == productId)
        return prd;
    }
    return null;
    }

  /**
   * searches the part list for a matching name or partial name match
   * @param partName
   * @return
   */
  public static ObservableList<Part> lookupPart(String partName) {
    ObservableList<Part> parts = FXCollections.observableArrayList();
    for (Part part : getAllParts()) {
      if (part.getName().contains(partName)) {
        parts.add(part);
      }
    }
    return parts;
  }

  /**
   * searches the product list for a matching name or partial name match
   * @param productName
   * @return
   */
  public static ObservableList<Product> lookupProduct(String productName) {
    ObservableList<Product> products = FXCollections.observableArrayList();
    for (Product product : getAllProducts()) {
      if (product.getName().contains(productName)) {
        products.add(product);
      }
    }
    return products;
  }

  /** modifies the selected parts data
   *
   * @param id
   * @param selectedPart
   */
  public static void updatePart(int id, Part selectedPart) {
    int index =-1;
    for (Part Part : getAllParts()) {
      index++;
      if (Part.getId() == id) {
        getAllParts().set(index, selectedPart);
      }
    }
  }

  /**
   * modifies the selected products data
   * @param id
   * @param selectedProduct
   */
  public static void updateProduct(int id, Product selectedProduct) {
    int index = -1;
    for (Product Product : getAllProducts()) {
      index++;
      if (Product.getId() == id) {
        getAllProducts().set(index, selectedProduct);
      }
    }
  }

  /**
   * deletes a part from the all parts list
   * @param part
   */
  public static void deletePart(Part part) {
    Part deletePrt = lookupPart(part.getId());

    if (deletePrt == null) {
    }
    else {
      allParts.remove(deletePrt);
    }
  }

  /**
   * deletes a product from the all products list
   * @param selectedProduct
   */
  public static void deleteProduct(Product selectedProduct) {
    allProducts.remove(selectedProduct);
  }

  /**
   * returns the all parts list
   * @return
   */
  public static ObservableList<Part> getAllParts() {
    return allParts;
  }

  /**
   * returns the all products list
   * @return
   */
  public static ObservableList<Product> getAllProducts() {
    return allProducts;
  }
 }