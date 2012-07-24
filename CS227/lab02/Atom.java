package lab02;

/**
 * A model of an atom used to find number of protons, neutrons, electrons.
 */
public class Atom {
   /**
    * Number of protons in this atom.
    */
	int protons;
   /**
    * Number of neutrons in this atom.
    */
	int neutrons;
   /**
    * Number of electrons in this atom.
    */
	int electrons;
	
   /**
    * Construct an atom with the given number of protons, neutrons & electrons.
    *
    * @param givenProtons
    * The number of protons in this atom.
    * @param givenNeutrons
    * The number of neutrons in this atom.
    * @param givenElectrons
    * The number of electrons in this atom.
    */
	public Atom (int givenProtons, int givenNeutrons, int givenElectrons)
	{
		protons = givenProtons;
		neutrons = givenNeutrons;
		electrons = givenElectrons;
	}
	
   /**
    * Get the number of protons in this atom.
    *
    * @return
    * Number of protons in this atom.
    */
	public int getProtons()
	{
		return protons;
	}
	
   /**
    * Get the number of neutrons in this atom.
    *
    * @return
    * Number of neutrons in this atom.
    */
	public int getNeutrons()
	{
		return neutrons;
	}
	
   /**
    * Get the number of electrons in this atom.
    *
    * @return
    * Number of electrons in this atom.
    */	
	public int getElectrons()
	{
		return electrons;
	}
	
   /**
    * Get the atomic mass number of this atom.
    *
    * @return
    * The atomic mass number of this atom.
    */
	public int getAtomicMassNumber()
	{
		return protons + neutrons;
	}
	
   /**
    * Get the atomic charge of this atom.
    *
    * @return
    * Atomic charge of this atom.
    */
	public int getAtomicCharge()
	{
		return protons - electrons;
	}
}
