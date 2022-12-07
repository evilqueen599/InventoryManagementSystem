package Model; /**
 * Public Model.InHouse Class - subclass of the Part class
 */

/**
 * @author Tricia Smith
 */

public class InHouse extends Part {

    private int machineId;

    /**
     * constructors of the Model.InHouse class
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param machineId
     */
    public InHouse (int id, String name, double price, int stock, int min, int max, int machineId) {
        super (id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * sets the machine ID
     * @param machineId
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     * gets the machine ID
     * @return
     */
    public int getMachineId() {
        return machineId;
    }
}