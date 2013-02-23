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
import java.util.LinkedList;
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
		String input = "/datasets/Lab4/group-files";
//		String input = "/datasets/Lab4/test-files";
		String temp = "/user/breber/Lab4/temp";
		String output = "/user/breber/Lab4/exp1/";

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
		FileOutputFormat.setOutputPath(job_two, new Path(output));

		job_two.waitForCompletion(true);

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
	 * @author breber
	 */
	public static class Map_One extends Mapper<LongWritable, Text, Text, Text> {
		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			
			int[] seeds = new int[] { 100, 54351, 2416, 3426, 983, 7582, 1305, 54325, 83021, 4332, 9476, 4328 };
			int[] minHash = new int[seeds.length];
			
			for (int i = 0; i < minHash.length; i++) {
				minHash[i] = Integer.MAX_VALUE;
			}
			
			String documentContents = line.substring(line.indexOf('-') + 1);

			List<String> shingles = getKShingles(documentContents, 9);

			for (String shingle : shingles) {
				for (int i = 0; i < minHash.length; i++) {
					minHash[i] = Math.min(minHash[i], Experiment1.hash(shingle.getBytes(), seeds[i]));
				}
			}

			for (int i = 0; i < minHash.length; i++) {
				context.write(new Text(minHash[i] + ""), value);
			}
		}

		private List<String> getKShingles(String s, int k) {
			List<String> shingles = new ArrayList<String>();

			for (int i = 0; i <= s.length() - k; i++) {
				shingles.add(s.substring(i, i + k));
			}

			return shingles;
		}
	}

	/**
	 * 
	 * And outputs:
	 * 
	 * 
	 * @author breber
	 */
	public static class Reduce_One extends Reducer<Text, Text, Text, Text> {

		@Override
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			List<String> documentIds = new ArrayList<String>();
			String documentContents = null;

			for (Text t : values) {
				String full = t.toString();
				String docId = full.substring(0, full.indexOf('-'));
				String docContents = full.substring(full.indexOf('-') + 1);

				if (documentContents == null) {
					documentContents = docContents;
				}

				documentIds.add(docId);
			}

			StringBuilder outKey = new StringBuilder();

			outKey.append(documentIds.size() + "~~");

			for (String s : documentIds) {
				outKey.append(s);
				outKey.append(',');
			}

			String outKeyString = outKey.toString();

			context.write(new Text(outKeyString.substring(0, outKeyString.length() - 1)), new Text(documentContents));
		}
	}
	
	
	public static class Map_Two extends Mapper<LongWritable, Text, Text, Text> {
		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			// Remove count
			line = line.substring(line.indexOf("~~") + 2);
			
			// Get the keys
			String keys = line.substring(0, line.indexOf('\t'));
			String[] splitKeys = keys.split(",");
			List<Integer> keyInts = new ArrayList<Integer>();
			
			for (String k : splitKeys) {
				keyInts.add(Integer.parseInt(k));
			}
			
			Collections.sort(keyInts);
			
			context.write(new Text(keyInts.get(0) + ""), new Text(line));
		}
	}
	
	public static class Reduce_Two extends Reducer<Text, Text, Text, Text> {
		private int count = 0;
		@Override
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			Set<Integer> documentIds = new HashSet<Integer>();
			String documentContents = null;

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

			System.out.println("Group " + count++ + ": " + documentIds.size());
			
			if (documentIds.size() > 10) {
				StringBuilder outKey = new StringBuilder();
	
				outKey.append(documentIds.size() + "~~");
	
				List<Integer> docIds = new LinkedList<Integer>(documentIds);
				Collections.sort(docIds);
				for (Integer s : docIds) {
					outKey.append(s);
					outKey.append(',');
				}
	
				String outKeyString = outKey.toString();
	
				context.write(new Text(outKeyString.substring(0, outKeyString.length() - 1)), new Text(/*documentContents*/));
			}
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