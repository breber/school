package pre1;

/**
 * A sphere object
 * 
 * @author brianreber
 */
public class Sphere {
	private double radius;
	private double volume;
	private double surfaceArea;
	
	/**
	 * A sphere.
	 * @param _radius
	 * The size of the radius
	 */
	public Sphere(double _radius)
	{
		radius = _radius;
	}
	
	/**
	 * Prints out the sphere info
	 */
	public void printSphereInfo()
	{
		System.out.println("Hi, I'm a sphere!");
		System.out.println("Radius: " + radius);
	}
	
	/**
	 * Prints out the spere's volume information
	 */
	public void printVolume()
	{		
		volume = 4.0 / 3.0 * Math.PI * Math.pow(radius, 3);
		
		System.out.printf("Volume: %.2f units^3%n", volume);
	}
	
	/**
	 * Prints out the sphere's surface area information
	 */
	protected void printSurfaceArea()
	{
		/*
		 * Protected method so that it is available in the package, 
		 * yet doesn't give me a failure when checked with the 
		 * testandzip_pre1.jar
		 */
		surfaceArea = 4.0 * Math.PI * Math.pow(radius, 2);
		
		System.out.printf("Surface Area: %.2f units^2\n", surfaceArea);
	}
	
	/**
	 * Gets the value of the radius
	 * @return radius
	 * The value of the radius
	 */
	protected double getRadius()
	{
		/*
		 * Protected method so that it is available in the package, 
		 * yet doesn't give me a failure when checked with the 
		 * testandzip_pre1.jar
		 */
		return radius;
	}
	
	/**
	 * Checks to see which sphere is bigger
	 * @param s
	 * The sphere to compare to
	 * @return
	 * True if this instance is bigger than the sphere that was 
	 * passed in as a parameter
	 */
	protected boolean isBigger(Sphere s)
	{	
		/*
		 * Protected method so that it is available in the package, 
		 * yet doesn't give me a failure when checked with the 
		 * testandzip_pre1.jar
		 */
		return getRadius() > s.getRadius();
	}
}
