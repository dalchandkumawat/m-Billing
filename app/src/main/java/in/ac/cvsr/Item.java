package in.ac.cvsr.newapplication;

public class Item {
    private String id,name,price,quantity,amount;
    public Item(String id,String name,String price){
        this.id=id;
        this.name=name;
        this.price=price;
    }
    public Item(String id,String name,String price,String quantity,String amount){
        this.id=id;
        this.name=name;
        this.price=price;
        this.quantity=quantity;
        this.amount=amount;
    }
    public Item(){}

    public String getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
    public String getQuantity(){return quantity;}
    public String getAmount(){return amount;}
}
