package lab02;

public class AtomTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Atom hydrogen = new Atom(1,0,2);
		
		System.out.println("Hydrogen:");
		System.out.print("Atomic Charge: ");
		System.out.println(hydrogen.getAtomicCharge());
		System.out.print("Atomic Mass Number: ");
		System.out.println(hydrogen.getAtomicMassNumber());
		System.out.print("Electrons: ");
		System.out.println(hydrogen.getElectrons());
		System.out.print("Neutrons: ");
		System.out.println(hydrogen.getNeutrons());
		System.out.print("Protons: ");
		System.out.println(hydrogen.getProtons());
		
		
		Atom uranium = new Atom(92,146,92);
		
		System.out.print("Uranium-");
		System.out.println(uranium.getAtomicMassNumber());
	}

}
