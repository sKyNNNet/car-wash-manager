package darklight;

public class Inventory {

    private String name;
    private int quantity;
    private String unit;
    private String supplier;
    private Double pricePerUnit;

    public Inventory(String name, int quantity, String unit, String supplier, Double pricePerUnit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.supplier = supplier;
        this.pricePerUnit = pricePerUnit;
    }

    public Inventory(){


    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
}
