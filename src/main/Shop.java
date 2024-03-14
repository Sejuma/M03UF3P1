package main;

import model.Product;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import model.Sale;
import model.Amount;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Shop {
    private Amount cash = new Amount(100.00, "€");
    private ArrayList<Product> inventory;
    private int numberProducts;
    private ArrayList<Sale> sales;
    int sale_num = 0;

    final static double TAX_RATE = 1.04;

    public Shop() {
        cash = new Amount(150.0, "€");
        inventory = new ArrayList<Product>();
        sales = new ArrayList<Sale>();
    }

    public static void main(String[] args) throws IOException {
        Shop shop = new Shop();
        
        File inventory = new File(System.getProperty("user.dir") + File.separator + "files/inputInventory.txt");
        
        //shop.loadInventory();
        Shop.importarInventario(inventory);

        Scanner scanner = new Scanner(System.in);
        int opcion = 0;
        boolean exit = false;

        do {
            System.out.println("===========================");
            System.out.println("Menu principal miTienda.com");
            System.out.println("===========================");
            System.out.println("1) Contar caja");
            System.out.println("2) Añadir producto");
            System.out.println("3) Añadir stock");
            System.out.println("4) Marcar producto proxima caducidad");
            System.out.println("5) Ver inventario");
            System.out.println("6) Venta");
            System.out.println("7) Ver ventas");
            System.out.println("8) Ver ventas totales");
            System.out.println("9) Eliminar producto");
            System.out.println("10) Salir programa");
            System.out.print("Seleccione una opción:");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    shop.showCash();
                    break;

                case 2:
                    shop.addProduct();
                    break;

                case 3:
                    shop.addStock();
                    break;

                case 4:
                    shop.setExpired();
                    break;

                case 5:
                    shop.showInventory();
                    break;

                case 6:
                    shop.sale();
                    break;

                case 7:
                    shop.showSales();
                    break;

                case 8:
                	shop.showSalesValue();
                    break;

                case 9:
                	shop.deleteProduct();
                    break;
                    
                case 10:
                	exit = true;
                    break;
            }

        } while (!exit);

        System.out.print("Has salido de la tienda.");

    }

    /**
     * load initial inventory to shop
     
    public void loadInventory() {
	        
    	inventory.add(new Product("Manzana", new Amount(10.00, "€"), true, 10));
	    inventory.add(new Product("Pera", new Amount(20.00, "€"), true, 20));
	    inventory.add(new Product("Hamburguesa", new Amount(30.00, "€"), true, 30));
	    inventory.add(new Product("Fresa", new Amount(5.00, "€"), true, 20));
        
        numberProducts = 4;
    }
*/
   
    public static void importarInventario(File inventory) throws IOException {
		FileReader fichero = new FileReader(inventory);
		BufferedReader fichero1 = new BufferedReader(fichero);
		
		String producto = null;
		double precio = 0.0;
		int cantidadStock = 1;
		
		String linea = fichero1.readLine();
		
		while (linea != null) {
			
			String[] primeraSeparacion = linea.split(";");
			
			for (int i = 0; i < primeraSeparacion.length; i++) {
				
				String[] segundaSeparacion = primeraSeparacion[i].split(":");
				
				switch (i) {
					case 0:
						producto = segundaSeparacion[1];
						break;
					case 1:
						precio = Double.parseDouble(segundaSeparacion[1]);
						break;
					case 2:
						cantidadStock = Integer.parseInt(segundaSeparacion[1]);
						break;
					default:
						break;
				}
				inventory.add(new Product(producto, new Amount(precio, "€"), true, cantidadStock));
			}	
			linea = fichero1.readLine();
		}
		fichero.close();
		fichero1.close();
		
		System.out.println(inventory.toString());
    }
    
    /**
     * show current total cash
     */
    private void showCash() {

        System.out.println("\nDinero actual: " + cash + "\n");
    }

    /**
     * add a new product to inventory getting data from console
     */
    public void addProduct() {
        if (isInventoryFull()) {
            System.out.println("No se pueden añadir más productos");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Nombre: ");
        String name = scanner.nextLine();

        boolean product = productExists(name);

        if (!product) {

            System.out.print("Precio mayorista: ");
            double wholesalerPrice = scanner.nextDouble();
            System.out.print("Stock: ");
            int stock = scanner.nextInt();

            for (int i = 0; i <= inventory.size(); i++) {
                if (inventory == null) {
                	inventory.add(new Product(name, new Amount(wholesalerPrice, "€"), true, stock));
                    numberProducts++;
                    break;
                }
            }
        } else {
            System.out.print("\nEl producto ya existe en el inventario. Seleccione la opción '3. Añadir Stock'.\n\n");
        }
    }

    /**
     * add stock for a specific product
     */
    public void addStock() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Seleccione un nombre de producto: ");
        String name = scanner.next();
        Product product = findProduct(name);

        if (product != null) {
            // ask for stock
            System.out.print("Seleccione la cantidad a añadir: ");
            int stock = scanner.nextInt();
            int newStock = product.getStock() + stock;
            // update stock product
            product.setStock(newStock);
            System.out.println("El stock del producto " + name + " ha sido actualizado a " + product.getStock());

        } else {
            System.out.println("No se ha encontrado el producto con nombre " + name);
        }
    }

    /**
     * set a product as expired
     */
    private void setExpired() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Seleccione un nombre de producto: ");
        String name = scanner.next();

        Product product = findProduct(name);

        product.expire();

        if (product != null) {
            System.out.println("El precio de venta al público del producto " + name + " ha sido actualizado a " + product.getPublicPrice());

        }
    }

    /**
     * show all inventory
     */
    public void showInventory() {
        System.out.println("\n---Contenido actual de la tienda---");
        System.out.print("========================================");
        System.out.println();
        for (Product product : inventory) {
            if (product != null) {
                System.out.print(product);
            } else {
                break;
            }
            System.out.println();
        }
        System.out.println();
    }

    public void showInventorySale() {
        System.out.println("\n---Contenido actual de la tienda---");
        System.out.print("========================================");
        System.out.println();
        for (Product product : inventory) {
            if (product != null) {
                System.out.print(product);
            } else {
                break;
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * make a sale of products to a client
     */
    public void sale() {
        // ask for client name
        Scanner sc = new Scanner(System.in);
        System.out.println("Realizar venta, escribir nombre cliente");
        String client = sc.nextLine();

        // sale product until input name is not 0
        Amount totalAmount = new Amount(0.0, "€");
        String name = "";
        
        LocalDateTime actualDate = LocalDateTime.now();
        DateTimeFormatter formatedActualDate = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        ArrayList<Product> products = new ArrayList<Product>();
        int quantity;

        while (!name.equals("0")) {

            sc = new Scanner(System.in);
            showInventorySale();

            System.out.println("Introduce el nombre del producto, escribir 0 para terminar:");
            name = sc.nextLine();

            if (name.equals("0")) {
                break;
            }

            Product product = findProduct(name);
            boolean productAvailable = false;

            if (product != null && product.isAvailable()) {

                System.out.println("Introduce la cantidad deseada:");
                quantity = sc.nextInt();

                // if no more stock, set as not available to sale
                if (product.getStock() == 0) {
                    product.setAvailable(false);
                    System.out.println("Producto sin stock.");
                } else if (quantity > product.getStock()) {
                    product.setAvailable(false);
                    System.out.println("Cantidad mayor al stock disponible del producto.");
                    break;
                } else {
                    productAvailable = true;
                    products.add(product);
                    totalAmount.setValue(totalAmount.getValue() + (product.getPublicPrice().getValue() * quantity));
                    product.setStock(product.getStock() - quantity);
                    System.out.println("Producto añadido con éxito");
                }
            }

            if (!productAvailable) {
                System.out.println("Producto no encontrado");
            }
        }

        // show cost total
        if (totalAmount.getValue() > 0) {
            totalAmount.setValue(totalAmount.getValue() * TAX_RATE);
            String formatedDate = actualDate.format(formatedActualDate);
            sales.add(new Sale(client, products, totalAmount, formatedDate));
            sale_num++;
            cash.setValue(cash.getValue() + totalAmount.getValue());
            System.out.println("Venta realizada con éxito, total: " + totalAmount);
        } else {
            System.out.println("Venta no realizada.");
        }
    }

    /**
     * show all sales
     */
    private void showSales() {
        System.out.println("Lista de ventas:");
        for (Sale sale : sales) {
            if (sale != null) {
                System.out.println(sale);
            }
        }
    }

    /**
     * show total sales value
     */
    private void showSalesValue() {
        Amount totalSales = new Amount(0.0, "€");
        System.out.print("Precio total de las ventas realizadas: ");
        for (Sale sale : sales) {
            if (sale != null) {
                totalSales.setValue(totalSales.getValue() + sale.getAmount().getValue());
            }
        }
        System.out.println(totalSales);
    }

    /**
     * add a product to inventory
     *
     * @param product
     */
    public void addProduct(Product product) {
        if (isInventoryFull()) {
            System.out.println("No se pueden añadir más productos, se ha alcanzado el máximo de " + inventory.size());
            return;
        }
        inventory.add(product);
        numberProducts++;
    }

    /**
     * check if inventory is full or not
     */
    public boolean isInventoryFull() {
        if (numberProducts == 10) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * find product by name
     *
     * @param product name
     */
    public Product findProduct(String name) {
        for (Product product : inventory) {
            if (product != null && product.getName().equalsIgnoreCase(name)) {
                return product;
            }
        }
        return null;

    }

    public boolean productExists(String name) {
        for (Product product : inventory) {
            if (product != null && product.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;

    }
    
    public void deleteProduct() {
    	
    	Scanner sc = new Scanner(System.in);
        System.out.println("Indica el nombre del artículo a eliminar:");
        String name = sc.nextLine();
        
        boolean deleted = false;
    	
    	for (Product product : inventory) {
            if (product != null && product.getName().equalsIgnoreCase(name)) {
            	inventory.remove(product);
            	System.out.println("El producto " + product.getName() + " ha sido eliminado.");
            	deleted = true;
            	break;
            }
        }
    	
    	if (!deleted) {
    		System.out.println("El producto " + name + " no existe.");
    	}
    }
}
