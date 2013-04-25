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

	public void processPunctuation(StreamingInput<Tuple> stream, StreamingData.Punctuation mark) throws Exception {
		if (mark == StreamingData.Punctuation.FINAL_MARKER) {
			processDocuments();
			files.clear();
		}
	}

	private void processDocuments() throws Exception {
		final StreamingOutput<OutputTuple> output = getOutput(0);
		List<DocumentList> hashGroup = new ArrayList<DocumentList>();
		DocumentList baseDocList = new DocumentList();

		// For each document in the current hour
		for (String name : files.keySet()) {
			// Read the file
			String documentContents = readFile("/datasets/Lab10/Documents/" + name);

			// Calculate the min-hashes of this document
			HashGroup curDoc = new HashGroup(documentContents);

			// Store it in the base document for searching the list
			baseDocList.group = curDoc;

			// Find the DocumentList with above a threshold of
			// matching min-hashes
			int index = hashGroup.indexOf(baseDocList);
			DocumentList found;

			// If one is found, store it
			if (hashGroup.contains(baseDocList)) {
				found = hashGroup.get(index);
			} else {
				// Otherwise, create a new DocumentList and set
				// the min-hashes
				found = new DocumentList();
				found.group = curDoc;
			}

			// Add the current document to the found DocumentList
			found.documents.add(name);

			// Update the DocumentList with the updated item
			if (index != -1) {
				hashGroup.set(index, found);
			} else {
				hashGroup.add(found);
			}
		}

		// Output one document per group
		for (DocumentList s : hashGroup) {
			OutputTuple outputTuple = output.newTuple();
			Iterator<String> iter = s.documents.iterator();
			String document = iter.next();

			outputTuple.setString("ts", files.get(document));
			outputTuple.setString("filename", document + " ~~ " + s.documents.size() + "!!~~!!" + hashGroup.size());
			output.submit(outputTuple);
		}

		System.out.println("Done hour: " + currentHour);
	}

	/**
	 * Calculate the k-shingles of the given string
	 *
	 * @param s the string to calculate the shingles for
	 * @param k how many characters in each shingle
	 * @return a list of the k-shingles
	 */
	private static List<String> getKShingles(String s, int k) {
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

	/**
	 * A group of hashes for a given document
	 */
	private static class HashGroup {
		private static final int[] seeds = new int[] { 100, 54351, 2416, 3426, 983, 7582 };//, 1305, 54325, 83021, 4332, 9476, 4328 };
		private int[] minHash = new int[seeds.length];

		public HashGroup(String docContents) {
			// Set all the minhash values to the maximum int value to start with
			for (int i = 0; i < minHash.length; i++) {
				minHash[i] = Integer.MAX_VALUE;
			}

			// Get a list of the shingles (9-shingles in this case)
			List<String> shingles = getKShingles(docContents, 9);

			// Go through and calculate the minhashes of the document
			for (String shingle : shingles) {
				for (int i = 0; i < minHash.length; i++) {
					minHash[i] = Math.min(minHash[i], hash(shingle.getBytes(), seeds[i]));
				}
			}
		}

		/**
		 * Override equals to ensure at least half of the
		 * hashes match.
		 */
		public boolean equals(Object obj) {
			HashGroup oth = (HashGroup) obj;
			int numSame = 0;

			for (int i = 0; i < minHash.length; i++) {
				if (minHash[i] == oth.minHash[i]) {
					numSame++;
				}
			}

			return numSame >= Math.floor(seeds.length * .5);
		}
	}

	/**
	 * A list of documents with "matching" min-hashes
	 */
	private static class DocumentList {
		HashGroup group = new HashGroup("");
		Set<String> documents = new HashSet<String>();

		/**
		 * Override equals to just compare the group for each
		 */
		public boolean equals(Object obj) {
			DocumentList oth = (DocumentList) obj;

			return group.equals(oth.group);
		}
	}
}
