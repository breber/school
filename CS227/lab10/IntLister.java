package lab10;

/**
 * @author brianreber
 *
 */
public class IntLister {

	public static void main(String[] args) {

		IntListSorted list = new IntListSorted();

		list.add(5);
		list.add(4);
		list.add(3);
		list.add(50);
		list.add(49);
		list.add(75);
		list.add(25);
		list.add(0);
		System.out.println(list);
		System.out.println("Size: " + list.size());
		System.out.println("Min: " + list.getMinimum());
		System.out.println("Max: " + list.getMaximum());
		System.out.println("Median: " + list.getMedian());
	}
}

