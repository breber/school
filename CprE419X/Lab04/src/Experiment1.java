/**
 * Cpr E 419 - Lab 4
 * 
 * @author breber (Brian Reber)
 */

import java.io.IOException;
import java.nio.ByteBuffer;

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
		//		String input = "/user/breber/ex1.txt";
		String temp = "/user/breber/Lab4/temp";
		String output = "/user/breber/Lab4/exp1/";

		int reduce_tasks = 2;  // The number of reduce tasks that will be assigned to the job
		Configuration conf = new Configuration();

		// Create the job
		Job job_one = new Job(conf, Experiment1.class.getName() + " Round One");
		job_one.setJarByClass(Experiment1.class);
		job_one.setNumReduceTasks(reduce_tasks);

		job_one.setOutputKeyClass(Text.class);
		job_one.setOutputValueClass(Text.class);

		job_one.setMapperClass(Map_One.class);
		job_one.setReducerClass(Reduce_One.class);

		job_one.setInputFormatClass(TextInputFormat.class);
		job_one.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job_one, new Path(input));
		FileOutputFormat.setOutputPath(job_one, new Path(temp + "round1"));

		job_one.waitForCompletion(true);

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
			int minHash = Integer.MAX_VALUE;

			String documentId = line.substring(0, line.indexOf('-'));
			String documentContents = line.substring(line.indexOf('-'));
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