package lab02;

/**
 * A model of a basketball used in quality control simulations.
 */
public class Basketball {

   /**
    * Inflation state of this basketball.
    */
   private boolean isInflated;

   /**
    * Circumference of this basketball in inches.
    */
   private double circumference;

   /**
    * Construct a basketball with the given circumference in inches.
    *
    * @param givenCircumference
    * The circumference of the basketball.
    */
   public Basketball(double givenCircumference) {
      isInflated = false;
      circumference = givenCircumference;
   }

   /**
    * Get the status of this basketball's dribbleability.
    *
    * @return
    * True if the basketball can be dribbled, false otherwise.
    */
   public boolean isDribbleable() {
      return isInflated;
   }

   /**
    * Get the circumference of this basketball.
    *
    * @return
    * Circumference of basketball.
    */
   public double getCircumference() {
      return circumference;
   }

   /**
    * Inflate the basketball.
    */
   public void inflate() {
      isInflated = true;
   }
}
