// writen in WEB-INF/src/myBeans/TestBean.java
package myBeans;

public class TestBean {
   private String name;
   private Address address;
   
   // zero arg constructor
   public TestBean()  { 
      name = "Getafix The Gaul"; 
      address = new Address();
   }
   
   // set/get methods
   public void setName(String uname) {
     name = uname;
   }
   public String getName() {
     return name;
   }
   
   public Address getAddress() {
      return address;
   }
   
   public void setAddress(String bldg) {
	   address.setBldg(bldg);
   }
   
   public class Address {
      String bldg = "Department of Computer Science";
         
      public String getBldg() {
         return bldg;
      }	        
      
      public void setBldg(String building) {
    	  bldg = building;
      }
   }
}

