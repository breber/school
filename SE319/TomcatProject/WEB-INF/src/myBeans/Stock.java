package myBeans;

// Stock of books 
public class Stock
{
   private Book[] books;

   public Stock()
   {
     Initializer init = new Initializer();
     books = init.books();
   }

   // get methods
   public Book[] getBooks() throws InterruptedException { Thread.sleep(5000); return books; }
   
   // Cart will contain user selection 
   public Cart getCart()
   {
	   Cart cart = new Cart();
	   return cart;
   }
   
   // inner classes
   
   // Book class: name, author, price
   public class Book
   {
       private String name, author, price;

       public Book(String n, String a, String p)
       {
         name = n; author = a; price = p;
       }
       public String getName() { return name; }
       public String getAuthor() { return author; }
       public String getPrice()  { return price; }

   }
   
   // Cart class: add to cart, retrieve items from cart
   public class Cart
   {
	   private Book[] items = new Book[0];
	   
	   public void add(String name, String author, String price)
	   {
		   Stock.Book[] temp = new Book[items.length+1];
			int i;	
			for (i=0; i<items.length; i++)
			{
				temp[i] = items[i];
			}
			temp[i] = new Book(name, author, price);
			items = temp;
	   }
	   
	   public Book[]  getItems()
	   {
		   return items;
	   }
   }
   
   // Initializing the Stock
   private class Initializer
   {
      public Book[] books() {
                              Book[] bookArray = { new Book("Love in the Time of Cholera", "G. G. Marquez", "$11.20"), 
                                                   new Book("War and Peace", "L. Tolstoy", "$11.75"),
                                                   new Book("Gitanjali", "R. Tagore", "$9.95"),
                                                   new Book("Beloved", "T. Morrison", "$10.99"),
                                                   new Book("Harold Pinter: Plays", "H. Pinter", "$17.52"),
                                                   new Book("The Tin Drum", "G. Grass", "$10.85"),
                                                   new Book("The Cairo Trilogy", "N. Mahfouz", "$10.85") };
                              return bookArray;
                            };  
   }
}