package lab02;

public class BasketballSim {
	   public static void main(String[] args) {
	      Basketball ball = new Basketball(28.5);

	      System.out.println(ball.getCircumference());

	      System.out.println(ball.isDribbleable());
	      ball.inflate();
	      System.out.println(ball.isDribbleable());
	   }
}
