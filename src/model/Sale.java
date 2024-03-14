package model;

import java.util.ArrayList;

public class Sale {
	String client;
	ArrayList<Product> products;
	private Amount amount;
	String actualDate;
	
	
	public Sale(String client, ArrayList<Product> products, Amount amount, String actualDate) {
		super();
		this.client = client;
		this.products = products;
		this.amount = amount;
		this.actualDate = actualDate;
	}
	
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public ArrayList<Product> getProducts() {
		return products;
	}
	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}
	public Amount getAmount() {
		return amount;
	}
	public void setAmount(Amount amount) {
		this.amount = amount;
	}
	public String getActualDate() {
		return actualDate;
	}
	public void setActualDate(String actualDate) {
		this.actualDate = actualDate;
	}

	@Override
	public String toString() {
	    StringBuilder boughtProducts = new StringBuilder();
	    
	    for (Product product : products) {
	        if (product != null) {
	            boughtProducts.append(product.getName()).append(", ");
	        }
	    }
	    
	    String productsString = boughtProducts.toString();
	    
	    return "Venta --> Client = " + client + " | Products = " + productsString + " | Amount = " + amount + " | Fecha/Hora: " + actualDate;
	}
}