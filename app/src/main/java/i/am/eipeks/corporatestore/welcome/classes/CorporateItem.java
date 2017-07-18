package i.am.eipeks.corporatestore.welcome.classes;


public class CorporateItem {

    private String category, section, name, type, color, dateReceived, quantity, size, price;

    public CorporateItem(String category, String section, String name,
                         String type, String color, String dateReceived,
                         String quantity, String size, String price){
        this.category = category;
        this.section = section;
        this.name = name;
        this.type = type;
        this.color = color;
        this.dateReceived = dateReceived;
        this.quantity = quantity;
        this.size = size;
        this.price = price;
    }

    public void incrementQuantity(int quantity){
        this.quantity = String.valueOf(Integer.parseInt(this.quantity) + quantity);
    }

    public String getCategory() {
        return category;
    }

    public String getSection() {
        return section;
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

    public String getDateReceived() {
        return dateReceived;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getSize() {
        return size;
    }

    public String getPrice() {
        return price;
    }
}