import com.ibm.streams.operator.*;
import java.util.*;
import java.io.*;
import java.nio.ByteBuffer;

public class DeSimilarDocs extends AbstractOperator {

	private Map<String, String> files = new HashMap<String, String>();
	private int currentHour = 0;

	@Override
	public void initialize(OperatorContext context) throws Exception {
		super.initialize(context); 
	}

	public void process(StreamingInput stream, Tuple tuple) throws Exception {
		String timestamp = tuple.getString("ts");
		String fileName = tuple.getString("filename");
		int hour = Integer.parseInt(timestamp.substring(0, timestamp.indexOf(":")));

		if (hour != currentHour) {
			processDocuments();

			currentHour = hour;
			files.clear();
		}

		files.put(fileName, timestamp);
	}

	private void processDocuments() throws Exception {
		final StreamingOutput<OutputTuple> output = getOutput(0);
		final int[] seeds = new int[] { 100, 54351, 2416, 3426 };//, 983, 7582, 1305, 54325, 83021, 4332, 9476, 4328 };

		Map<String, List<String>> hashGroup = new HashMap<String, List<String>>();

		for (String name : files.keySet()) {
			int[] minHash = new int[seeds.length];

			// Set all the minhash values to the maximum int value to start with
			for (int i = 0; i < minHash.length; i++) {
				minHash[i] = Integer.MAX_VALUE;
			}

			// Read the file
			String documentContents = readFile("/datasets/Lab10/Documents/" + name);

			// Get a list of the shingles (9-shingles in this case)
			List<String> shingles = getKShingles(documentContents, 9);

			// Go through and calculate the minhashes of the document
			for (String shingle : shingles) {
				for (int i = 0; i < minHash.length; i++) {
					minHash[i] = Math.min(minHash[i], hash(shingle.getBytes(), seeds[i]));
				}
			}

			List<String> group;
			String keyName = "";
			for (int i : minHash) {
				keyName += i;
			}
			
			if (hashGroup.containsKey(keyName)) {
				group = hashGroup.get(keyName);
			} else {
				group = new ArrayList<String>();
			}
			
			group.add(name);
			hashGroup.put(keyName, group);
		}

		// Output one document per group
		for (String s : hashGroup.keySet()) {
			OutputTuple outputTuple = output.newTuple();
			List<String> docs = hashGroup.get(s);

			outputTuple.setString("ts", files.get(docs.get(0)));
			outputTuple.setString("filename", docs.get(0));
			output.submit(outputTuple);
		}
	}

	/**
	 * Calculate the k-shingles of the given string
	 * 
	 * @param s the string to calculate the shingles for
	 * @param k how many characters in each shingle
	 * @return a list of the k-shingles
	 */
	private List<String> getKShingles(String s, int k) {
		List<String> shingles = new ArrayList<String>();

		for (int i = 0; i <= s.length() - k; i++) {
			shingles.add(s.substring(i, i + k));
		}

		return shingles;
	}

	/*
	 * Below is a hash function that can be used throughout your Application to
	 * help with minhashing. New hash functions can be derived by using a new
	 * seed value.
	 */
	private static int hash(byte[] b_con, int i_seed) {
		String content = new String(b_con);

		int seed = i_seed;
		int m = 0x5bd1e995;
		int r = 24;

		int len = content.length();
		byte[] work_array = null;

		int h = seed ^ content.length();

		int offset = 0;

		while (len >= 4) {
			work_array = new byte[4];
			ByteBuffer buf = ByteBuffer.wrap(content.substring(offset, offset + 4).getBytes());

			int k = buf.getInt();
			k = k * m;
			k ^= k >> r;
			k *= m;
			h *= m;
			h ^= k;

			offset += 4;
			len -= 4;
		}

		switch (len) {
		case 3:
			h ^= work_array[2] << 16;
		case 2:
			h ^= work_array[1] << 8;
		case 1:
			h ^= work_array[0];
			h *= m;
		}

		h ^= h >> 13;
		h *= m;
		h ^= h >> 15;

		return h;
	}

	/**
	 * Reads an entire input stream as a String. Closes the input stream.
	 */
	private String readFile(String pathname) throws IOException {
		File file = new File(pathname);
		StringBuilder fileContents = new StringBuilder((int)file.length());
		Scanner scanner = new Scanner(file);
		String lineSeparator = System.getProperty("line.separator");

		try {
			while (scanner.hasNextLine()) {        
				fileContents.append(scanner.nextLine() + lineSeparator);
			}
			return fileContents.toString();
		} finally {
			scanner.close();
		}
	}
}