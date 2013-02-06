/**
  * Cpr E 419 - Lab 3
  * 
  * @author breber (Brian Reber)
  */

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparator;
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
	} // End main
	
	public int run ( String[] args ) throws Exception {
		
		String input = "/datasets/Lab3/Graph";
		String temp = "/user/breber/Lab3/temp";
		String output = "/user/breber/Lab3/output/";
		
		int reduce_tasks = 2;  // The number of reduce tasks that will be assigned to the job
		Configuration conf = new Configuration();
		
		// Create job for round 1
		
		// Create the job
		Job job_one = new Job(conf, Experiment1.class.getName() + " Round One"); 
		
		// Attach the job to this Driver
		job_one.setJarByClass(Experiment1.class); 
		
		// Fix the number of reduce tasks to run
		// If not provided, the system decides on its own
		job_one.setNumReduceTasks(reduce_tasks);
		
		// The datatype of the Output Key 
		// Must match with the declaration of the Reducer Class
		job_one.setOutputKeyClass(Text.class); 
		
		// The datatype of the Output Value 
		// Must match with the declaration of the Reducer Class
		job_one.setOutputValueClass(Text.class);
		
		// The class that provides the map method
		job_one.setMapperClass(Map_One.class); 
		
		// The class that provides the reduce method
		job_one.setReducerClass(Reduce_One.class);
		
		// Decides how the input will be split
		// We are using TextInputFormat which splits the data line by line
		// This means wach map method recieves one line as an input
		job_one.setInputFormatClass(TextInputFormat.class);  
		
		// Decides the Output Format
		job_one.setOutputFormatClass(TextOutputFormat.class);
		
		// The input HDFS path for this job
		// The path can be a directory containing several files
		// You can add multiple input paths including multiple directories
		FileInputFormat.addInputPath(job_one, new Path(input)); 
		
		// The output HDFS path for this job
		// The output path must be one and only one
		// This must not be shared with other running jobs in the system
		FileOutputFormat.setOutputPath(job_one, new Path(temp + "round1"));
		
		// Run the job
		job_one.waitForCompletion(true); 
		
		
		// Create job for round 2
		// The output of the previous job can be passed as the input to the next
		// The steps are as in job 1
		Job job_two = new Job(conf, Experiment1.class.getName() + " Round Two"); 
		job_two.setJarByClass(Experiment1.class); 
		job_two.setNumReduceTasks(reduce_tasks); 
		
		job_two.setOutputKeyClass(Text.class); 
		job_two.setOutputValueClass(Text.class);
		
		// If required the same Map / Reduce classes can also be used
		// Will depend on logic if separate Map / Reduce classes are needed
		// Here we show separate ones
		job_two.setMapperClass(Map_Two.class); 
		job_two.setReducerClass(Reduce_Two.class);
		
		job_two.setInputFormatClass(TextInputFormat.class); 
		job_two.setOutputFormatClass(TextOutputFormat.class);
		
		// The output of previous job set as input of the next
		FileInputFormat.addInputPath(job_two, new Path(temp + "round1")); 
		FileOutputFormat.setOutputPath(job_two, new Path(temp + "round2"));
		
		// Run the job
		job_two.waitForCompletion(true);
		
		
		// Create job for round 3
		// The output of the previous job can be passed as the input to the next
		// The steps are as in job 1

		Job job_three = new Job(conf, Experiment1.class.getName() + " Round Three"); 
		job_three.setJarByClass(Experiment1.class); 
		job_three.setNumReduceTasks(reduce_tasks); 

		job_three.setOutputKeyClass(Text.class); 
		job_three.setOutputValueClass(IntWritable.class);

		// If required the same Map / Reduce classes can also be used
		// Will depend on logic if separate Map / Reduce classes are needed
		// Here we show separate ones
		job_three.setMapperClass(Map_Three.class); 
		job_three.setReducerClass(Reduce_Three.class);

		job_three.setInputFormatClass(TextInputFormat.class); 
		job_three.setOutputFormatClass(TextOutputFormat.class);

		// The output of previous job set as input of the next
		FileInputFormat.addInputPath(job_three, new Path(temp + "round1"));
		FileInputFormat.addInputPath(job_three, new Path(temp + "round2"));
		
		FileOutputFormat.setOutputPath(job_three, new Path(temp + "round3"));

		// Run the job
		job_three.waitForCompletion(true); 
		
		
		// Create job for round 4
		// The output of the previous job can be passed as the input to the next
		// The steps are as in job 1

		Job job_four = new Job(conf, Experiment1.class.getName() + " Round Four"); 
		job_four.setJarByClass(Experiment1.class); 
		job_four.setNumReduceTasks(1); 

		job_four.setOutputKeyClass(IntWritable.class); 
		job_four.setOutputValueClass(Text.class);
		job_four.setSortComparatorClass(IntComparator.class);

		// If required the same Map / Reduce classes can also be used
		// Will depend on logic if separate Map / Reduce classes are needed
		// Here we show separate ones
		job_four.setMapperClass(Map_Four.class); 
		job_four.setReducerClass(Reduce_Four.class);

		job_four.setInputFormatClass(TextInputFormat.class); 
		job_four.setOutputFormatClass(TextOutputFormat.class);

		// The output of previous job set as input of the next
		FileInputFormat.addInputPath(job_four, new Path(temp + "round3"));
		
		FileOutputFormat.setOutputPath(job_four, new Path(output));

		// Run the job
		job_four.waitForCompletion(true); 
		
		return 0;
	} // End run
	
	/**
	 * Comparator that allows us to specify that elements
	 * should enter our reducer in reverse order (largest to smallest)
	 * 
	 * @author breber
	 */
	public static class IntComparator extends WritableComparator {
		protected IntComparator() {
			super(IntWritable.class, true);
		}
		
		@Override
		public int compare(byte[] arg0, int arg1, int arg2, byte[] arg3,
				int arg4, int arg5) {
			return (-1) * super.compare(arg0, arg1, arg2, arg3, arg4, arg5);
		}
		
		@Override
		public int compare(Object a, Object b) {
			return (-1) * super.compare(a, b);
		}
	}
	
	// The Map Class
	// The input to the map method would be a LongWritable (long) key and Text (String) value
	// Notice the class declaration is done with LongWritable key and Text value
	// The TextInputFormat splits the data line by line.
	// The key for TextInputFormat is nothing but the line number and hence can be ignored
	// The value for the TextInputFormat is a line of text from the input
	// The map method can emit data using context.write() method
	// However, to match the class declaration, it must emit Text as key and IntWribale as value
	public static class Map_One extends Mapper<LongWritable, Text, Text, Text>  {
		/**
		 * @param key - the line nunmber
		 * @param value - a line in format of "citing_patent cited_patent" 
		 * 
		 * Outputs:
		 * 
		 * cited_patent citing_patent
		 */
		public void map(LongWritable key, Text value, Context context) 
								throws IOException, InterruptedException  {
			
			// The TextInputFormat splits the data line by line.
			// So each map method receives one line from the input
			String line = value.toString();
			
			// Tokenize to get the two vertices
			StringTokenizer tokens = new StringTokenizer(line);
			
			String vertex1 = tokens.nextToken();
			String vertex2 = tokens.nextToken();
			
			// Output the cited patent, with a value of the citing patent
			context.write(new Text(vertex2), new Text(vertex1));
		} // End method "map"
	} // End Class Map_One
	
	
	// The reduce class
	// The key is Text and must match the datatype of the output key of the map method
	// The value is IntWritable and also must match the datatype of the output value of the map method
	public static class Reduce_One extends Reducer<Text, Text, Text, Text>  {
		/**
		 * @param key - a patent node
		 * @param values - all nodes that cite the key patent
		 * 
		 * Output: All one-hop citations
		 * 
		 * cited_patent  list_of_citing_patents
		 */
		public void reduce(Text key, Iterable<Text> values, Context context) 
											throws IOException, InterruptedException  {
			
			StringBuilder output = new StringBuilder(); 
			for (Text val : values) {
				output.append(val.toString() + " ");
			}

			context.write(key, new Text(output.toString()));
		} // End method "reduce" 
	} // End Class Reduce_One
	
	// The second Map Class
	public static class Map_Two extends Mapper<LongWritable, Text, Text, Text>  {
		/**
		 * @param key - the line nunmber
		 * @param value - a line in format of "cited_patent [TAB] citing_patent citing_patent citing_patent ..."
		 * 
		 * Output:
		 * 
		 * citing_patent cited_patent
		 */
 		public void map(LongWritable key, Text value, Context context) 
				throws IOException, InterruptedException  {
			String line = value.toString();
			StringTokenizer tokens = new StringTokenizer(line);
			
			String citedPatent = tokens.nextToken();
			
			while (tokens.hasMoreTokens()) {
				context.write(new Text(tokens.nextToken()), new Text(citedPatent));
			}
		}  // End method "map"
	}  // End Class Map_Two
	
	// The second Reduce class
	public static class Reduce_Two extends Reducer<Text, Text, Text, Text>  {
		/**
		 * @param key - a patent node
		 * @param values - all nodes that cite the key patent (two hop)
		 * 
		 * Output:  All two-hop citations
		 * 
		 * cited_patent  list_of_citing_patents
		 */
		public void reduce(Text key, Iterable<Text> values, Context context) 
				throws IOException, InterruptedException  {

			StringBuilder output = new StringBuilder(); 
			for (Text val : values) {
				output.append(val.toString() + " ");
			}

			context.write(key, new Text(output.toString()));
		}  // End method "reduce"
	}  // End Class Reduce_Two
	
	
	// The second Map Class
		public static class Map_Three extends Mapper<LongWritable, Text, Text, IntWritable>  {
			/**
			 * @param key - the line nunmber
			 * @param value - a line in format of "cited_patent [TAB] citing_patent citing_patent citing_patent ..."
			 * 
			 * Output:
			 * 
			 * cited_patent number_of_citing_patents
			 */
			public void map(LongWritable key, Text value, Context context) 
					throws IOException, InterruptedException  {
				String line = value.toString();
				StringTokenizer tokens = new StringTokenizer(line);
				
				String citedPatent = tokens.nextToken();
				
				int count = 0;
				while (tokens.hasMoreTokens()) {
					tokens.nextToken();
					count++;
				}
				context.write(new Text(citedPatent), new IntWritable(count));
			}  // End method "map"
		}  // End Class Map_Two

		// The second Reduce class
		public static class Reduce_Three extends Reducer<Text, IntWritable, Text, IntWritable>  {
			/**
			 * @param key - a patent node
			 * @param values - the number of citing patents
			 * 
			 * Output:  The total number of citing patents
			 * 
			 * cited_patent number_of_citing_patents
			 */
			public void reduce(Text key, Iterable<IntWritable> values, Context context) 
					throws IOException, InterruptedException  {

				int sum = 0;
				
				for (IntWritable val : values) {
					sum += val.get();
				}
				
				context.write(key, new IntWritable(sum));
			}  // End method "reduce"
		}  // End Class Reduce_Three
		
		// The second Map Class
		public static class Map_Four extends Mapper<LongWritable, Text, IntWritable, Text>  {
			public void map(LongWritable key, Text value, Context context) 
					throws IOException, InterruptedException  {
				String line = value.toString();

				// Tokenize to get the individual words
				StringTokenizer tokens = new StringTokenizer(line);
				
				String citedPatent = tokens.nextToken();
				String count = tokens.nextToken();
				context.write(new IntWritable(Integer.parseInt(count)), new Text(citedPatent));
			}  // End method "map"
		}  // End Class Map_Two

		// The second Reduce class
		public static class Reduce_Four extends Reducer<IntWritable, Text, Text, IntWritable>  {

			private static int count = 0;

			public void reduce(IntWritable key, Iterable<Text> values, Context context) 
					throws IOException, InterruptedException  {

				// Print out all values that have the higest key value
				// Stop printing when we get to 10 values.
				// This solution only works since we specify 1 reducer.
				for (Text val : values) {
					if (count < 10) {
						context.write(val, key);
						count++;
					} else {
						break;
					}
				}
			}  // End method "reduce"
		}  // End Class Reduce_Two
}