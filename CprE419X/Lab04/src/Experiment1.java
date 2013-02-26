/**
 * Cpr E 419 - Lab 4
 * 
 * @author breber (Brian Reber)
 */

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class Experiment1 extends Configured implements Tool {

	public static void main (String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new Experiment1(), args);
		System.exit(res);
	}

	@Override
	public int run ( String[] args ) throws Exception {
//		String input = "/datasets/Lab4/group-files";
		String input = "/datasets/Lab4/second-group-files";
		String temp = "/user/breber/Lab4_secondgroup/temp";
		String output = "/user/breber/Lab4_secondgroup/exp1/";

		Configuration conf = new Configuration();

		// Create the job
		Job job_one = new Job(conf, Experiment1.class.getName() + " Round 1");
		job_one.setJarByClass(Experiment1.class);
		job_one.setNumReduceTasks(2);

		job_one.setOutputKeyClass(Text.class);
		job_one.setOutputValueClass(Text.class);

		job_one.setMapperClass(Map_One.class);
		job_one.setReducerClass(Reduce_One.class);

		job_one.setInputFormatClass(TextInputFormat.class);
		job_one.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job_one, new Path(input));
		FileOutputFormat.setOutputPath(job_one, new Path(temp));
		
		job_one.waitForCompletion(true);
		
		
		Job job_two = new Job(conf, Experiment1.class.getName() + " Round 2");
		job_two.setJarByClass(Experiment1.class);
		job_two.setNumReduceTasks(1);

		job_two.setOutputKeyClass(Text.class);
		job_two.setOutputValueClass(Text.class);

		job_two.setMapperClass(Map_Two.class);
		job_two.setReducerClass(Reduce_Two.class);

		job_two.setInputFormatClass(TextInputFormat.class);
		job_two.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job_two, new Path(temp));
		FileOutputFormat.setOutputPath(job_two, new Path(temp + "2"));

		job_two.waitForCompletion(true);
		
		
		Job job_three = new Job(conf, Experiment1.class.getName() + " Round 3");
		job_three.setJarByClass(Experiment1.class);
		job_three.setNumReduceTasks(1);

		job_three.setOutputKeyClass(Text.class);
		job_three.setOutputValueClass(Text.class);

		job_three.setMapperClass(Map_Three.class);
		job_three.setReducerClass(Reduce_Three.class);

		job_three.setInputFormatClass(TextInputFormat.class);
		job_three.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job_three, new Path(temp + "2"));
		FileOutputFormat.setOutputPath(job_three, new Path(temp + "3"));

		job_three.waitForCompletion(true);

		
		Job job_four = new Job(conf, Experiment1.class.getName() + " Round 4");
		job_four.setJarByClass(Experiment1.class);
		job_four.setNumReduceTasks(1);

		job_four.setOutputKeyClass(Text.class);
		job_four.setOutputValueClass(Text.class);

		job_four.setMapperClass(Map_Four.class);
		job_four.setReducerClass(Reduce_Four.class);

		job_four.setInputFormatClass(TextInputFormat.class);
		job_four.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job_four, new Path(temp + "3"));
		FileOutputFormat.setOutputPath(job_four, new Path(output));

		job_four.waitForCompletion(true);
		
		return 0;
	}

	/**
	 * First round of map reduce takes in
	 * 
	 * document_id-document_contents
	 * 
	 * And outputs:
	 * 
	 * min_hash		(document_id,document_contents)
	 * 
	 * For k different minhash seed values.
	 * 
	 * @author breber
	 */
	public static class Map_One extends Mapper<LongWritable, Text, Text, Text> {
		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			
			// An array of the different seeds to use when calculating minhashes
			int[] seeds = new int[] { 100, 54351, 2416, 3426, 983, 7582, 1305, 54325, 83021, 4332, 9476, 4328 };
			int[] minHash = new int[seeds.length];
			
			// Set all the minhash values to the maximum int value to start with
			for (int i = 0; i < minHash.length; i++) {
				minHash[i] = Integer.MAX_VALUE;
			}
			
			// Parse out the document content from the input
			String documentContents = line.substring(line.indexOf('-') + 1);

			// Get a list of the shingles (9-shingles in this case)
			List<String> shingles = getKShingles(documentContents, 9);

			// Go through and calculate the minhashes of the document
			for (String shingle : shingles) {
				for (int i = 0; i < minHash.length; i++) {
					minHash[i] = Math.min(minHash[i], Experiment1.hash(shingle.getBytes(), seeds[i]));
				}
			}

			// Output the minhashes with the entire document contents
			for (int i = 0; i < minHash.length; i += 2) {
				// Combine two hashes together when outputting to help prevent false-positives
				int combinedHash = Experiment1.hash(new String(minHash[i] + "" + minHash[i + 1]).getBytes(), 5000);
				context.write(new Text(combinedHash + ""), value);
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
	}

	/**
	 * The first round of reducing takes in:
	 * 
	 * combined_minhash		list_of_documents_with_minhash
	 * 
	 * And outputs:
	 * 
	 * list,of,docids		contents_of_single_document
	 * 
	 * @author breber
	 */
	public static class Reduce_One extends Reducer<Text, Text, Text, Text> {

		@Override
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			Set<String> documentIds = new HashSet<String>();
			String documentContents = null;

			// Go through each document and add its id to the list, and
			// store the document contents
			for (Text t : values) {
				String full = t.toString();
				String docId = full.substring(0, full.indexOf('-'));
				String docContents = full.substring(full.indexOf('-') + 1);

				if (documentContents == null) {
					documentContents = docContents;
				}

				documentIds.add(docId);
			}

			// Build the key with all document ids to write out to
			StringBuilder outKey = new StringBuilder();

			for (String s : documentIds) {
				outKey.append(s);
				outKey.append(',');
			}

			// Write the document contents with the key just created
			String outKeyString = outKey.toString();
			context.write(new Text(outKeyString.substring(0, outKeyString.length() - 1)), new Text(documentContents));
		}
	}
	
	/**
	 * The second round of mapping takes in:
	 * 
	 * list,of,docids		contents_of_single_document
	 * 
	 * And outputs:
	 * 
	 * min_doc_id		list,of,docids	contents_of_single_document
	 * 
	 * @author breber
	 */
	public static class Map_Two extends Mapper<LongWritable, Text, Text, Text> {
		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			
			// Get the keys
			String keys = line.substring(0, line.indexOf('\t'));
			String[] splitKeys = keys.split(",");

			// Get a list of the document ids
			List<Integer> keyInts = new ArrayList<Integer>();
			for (String k : splitKeys) {
				keyInts.add(Integer.parseInt(k));
			}
			
			// Sort them, minimum first in list
			Collections.sort(keyInts);
			
			// Write with key being the smallest item in the list
			context.write(new Text(keyInts.get(0) + ""), new Text(line));
		}
	}
	
	/**
	 * The second round of reducing takes in:
	 * 
	 * min_doc_id		list,of,docids	contents_of_single_document
	 * 
	 * And outputs:
	 * 
	 * group_num~~list,of,docids		contents_of_single_document
	 * 
	 * @author breber
	 */
	public static class Reduce_Two extends Reducer<Text, Text, Text, Text> {
		private int count = 0;
		@Override
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			Set<Integer> documentIds = new HashSet<Integer>();
			String documentContents = null;

			// Go through the results, and combine the groups of keys
			for (Text t : values) {
				String full = t.toString();
				String keys = full.substring(0, full.indexOf('\t'));
				String docContents = full.substring(full.indexOf('\t') + 1);

				if (documentContents == null) {
					documentContents = docContents;
				}

				String[] splitKeys = keys.split(",");
				
				for (String k : splitKeys) {
					documentIds.add(Integer.parseInt(k));
				}
			}

			System.out.println("Group " + count + ": " + documentIds.size());
			
			StringBuilder outKey = new StringBuilder();
			outKey.append(count + "~~");
			
			for (Integer s : documentIds) {
				outKey.append(s);
				outKey.append(',');
			}

			String outKeyString = outKey.toString();
			context.write(new Text(outKeyString.substring(0, outKeyString.length() - 1)), new Text(documentContents));
			
			count++;
		}
	}
	
	/**
	 * The third round of mapping takes in:
	 * 
	 * group_num~~list,of,docids		contents_of_single_document
	 * 
	 * And outputs:
	 * 
	 * doc_id		group~~size_of_group~~contents_of_single_document
	 * 
	 * @author breber
	 */
	public static class Map_Three extends Mapper<LongWritable, Text, Text, Text> {
		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			
			// Get the keys
			String keys = line.substring(0, line.indexOf('\t'));
			String docContents = line.substring(line.indexOf('\t') + 1);
					
			String clusterId = keys.substring(0, keys.indexOf("~~"));
			keys = keys.substring(keys.indexOf("~~") + 2);
			
			String[] splitKeys = keys.split(",");

			// Get a list of the document ids
			for (String k : splitKeys) {
				context.write(new Text(k), new Text(clusterId + "~~" + splitKeys.length + "~~" + docContents));
			}
		}
	}
	
	/**
	 * The third round of reducing takes in:
	 * 
	 * doc_id		group~~size_of_group~~contents_of_single_document
	 * 
	 * And outputs:
	 * 
	 * group		doc_id~~contents_of_single_document
	 * 
	 * @author breber
	 */
	public static class Reduce_Three extends Reducer<Text, Text, Text, Text> {
		@Override
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			String documentContents = "";
			String groupId = "";
			int maxGroupSize = -1;
			
			// Go through the results, and combine the groups of keys
			for (Text t : values) {
				String full = t.toString();
				String group = full.substring(0, full.indexOf("~~"));
				full = full.substring(full.indexOf("~~") + 2);
				String groupSize = full.substring(0, full.indexOf("~~"));
				String docContents = full.substring(full.indexOf("~~") + 2);

				if (Integer.parseInt(groupSize) > maxGroupSize) {
					documentContents = docContents;
					maxGroupSize = Integer.parseInt(groupSize);
					groupId = group;
				}
			}

			context.write(new Text(groupId), new Text(key.toString() + "~~" + documentContents));
		}
	}
	
	/**
	 * The fourth round of mapping takes in:
	 * 
	 * group		doc_id~~contents_of_single_document
	 * 
	 * And outputs:
	 * 
	 * group		doc_id~~contents_of_single_document
	 * 
	 * @author breber
	 */
	public static class Map_Four extends Mapper<LongWritable, Text, Text, Text> {
		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			
			// Get the keys
			String groupId = line.substring(0, line.indexOf('\t'));
			String docIdAndContents = line.substring(line.indexOf('\t') + 1);
					
			context.write(new Text(groupId), new Text(docIdAndContents));
		}
	}
	
	/**
	 * The fourth round of reducing takes in:
	 * 
	 * group		doc_id~~contents_of_single_document
	 * 
	 * And outputs:
	 * 
	 * list_of_doc_ids		contents_of_single_document
	 * 
	 * @author breber
	 */
	public static class Reduce_Four extends Reducer<Text, Text, Text, Text> {
		private int count = 0;
		
		@Override
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			Set<Integer> documentIds = new HashSet<Integer>();
			String documentContents = null;
			
			// Go through the results, and combine the groups of keys
			for (Text t : values) {
				String full = t.toString();
				String docId = full.substring(0, full.indexOf("~~"));
				String docContents = full.substring(full.indexOf("~~") + 2);

				documentIds.add(Integer.parseInt(docId));
				
				if (documentContents == null) {
					documentContents = docContents;
				}
			}
			
			System.out.println("Group " + count++ + ": " + documentIds.size());
			
			StringBuilder outKey = new StringBuilder();
			
			for (Integer s : documentIds) {
				outKey.append(s);
				outKey.append(',');
			}
			
			String outKeyString = outKey.toString();
			context.write(new Text(outKeyString.substring(0, outKeyString.length() - 1)), new Text(documentContents));
		}
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
}