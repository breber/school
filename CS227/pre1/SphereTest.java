package pre1;

/**
 * Tests the Sphere class
 * 
 * @author brianreber
 */
public class SphereTest {

	public static void main(String[] args)
	{
		// Creates the required sphere		
		Sphere sphere1 = new Sphere(3.0);
		System.out.println("Should print: \nHi, I'm a sphere!");
		System.out.println("Radius: 3.0 \n\nAcutally prints:");
		sphere1.printSphereInfo();
		System.out.println("\nVolume should be (about): 113.09 units^3");
		sphere1.printVolume();
		
		// Prints out the surface area, as an extra
		sphere1.printSurfaceArea();
		
		// Another sphere object with a different radius for testing purposes
		System.out.println();
		Sphere sphere2 = new Sphere(2.0);
		
		// Checks my method - will print which sphere is bigger
		if (sphere2.isBigger(sphere1))
			System.out.println("Sphere 2 is larger than Sphere 1");
		else System.out.println("Sphere 2 is not larger than Sphere 1");
		
		// Print out the second sphere's information
		sphere2.printSphereInfo();
		sphere2.printSurfaceArea();
		sphere2.printVolume();
		
		// A third sphere
		System.out.println();
		Sphere sphere3 = new Sphere(10.0);
		
		// Which sphere is bigger?
		if (sphere3.isBigger(sphere2))
			System.out.println("Sphere 3 is larger than Sphere 2");
		else System.out.println("Sphere 3 is not larger than Sphere 2");
		
		// Sphere Three's information
		sphere3.printSphereInfo();
		sphere3.printSurfaceArea();
		sphere3.printVolume();
	}
}
