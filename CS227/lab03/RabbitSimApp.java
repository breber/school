package lab03;

public class RabbitSimApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		RabbitSim sim = new RabbitSim();
		RabbitModel rab = new RabbitModel();
		sim.simulate(rab);
	}

}
