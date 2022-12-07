package Model;
/**
 * Public Outsourced Class - subclass of Part
 */

/**
 * @author Tricia Smith
 */

public class Outsourced extends Part {

    public String companyName;

    /**
     * constructors of Outsourced class
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param companyName
     */
    public Outsourced (int id, String name, double price, int stock, int min, int max, String companyName) {
        super( id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * sets the company name
     * @param companyName
     */
    public void setCompanyName(String companyName) {

        this.companyName = companyName;
    }

    /**
     * gets the company name
     * @return
     */
    public String getCompanyName()  {
        return companyName;
    }
}