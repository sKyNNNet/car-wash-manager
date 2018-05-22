package skynet;

public class Price {

    private int id;
    private String serviceType;
    private double priceValue;

    public Price(int id, String serviceType, double priceValue) {
        this.id = id;
        this.serviceType = serviceType;
        this.priceValue = priceValue;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public double getPriceValue() {
        return priceValue;
    }

    public void setPriceValue(double priceValue) {
        this.priceValue = priceValue;
    }

    public Price(){

    }

}
