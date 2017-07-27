package i.am.eipeks.corporatestore.welcome.classes;


public class CartItemClass {

    private String name, type, color, size, price;

    public CartItemClass(String name, String type, String color,String size, String price){
        this.name = name;
        this.type = type;
        this.color = color;
        this.size = size;
        this.price = price;
    }

    public void incrementQuantity(int increment){

    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

    public String getSize() {
        return size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {this.price = price;}
}