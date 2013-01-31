/**
  *****************************************
  *****************************************
  * Cpr E 419 - Lab 2 *********************
  * 
  * @author breber (Brian Reber)
  * 
  * For question regarding this code,
  * please contact:
  * Srikanta Tirthapura (snt@iastate.edu)
  * Arko Provo Mukherjee (arko@iastate.edu)
  *****************************************
  *****************************************
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

public class Driver extends Configured implements Tool {
	
	public static void main (String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new Driver(), args);
		System.exit(res); 
	} // End main
	
	public int run ( String[] args ) throws Exception {
		
		String input = "/datasets/Lab2/Gutenberg";    // Change this accordingly
		String temp = "/user/breber/Lab2/tempgutenberg";      // Change this accordingly
		String output = "/user/breber/Lab2/outputgutenberg/";  // Change this accordingly
		
		int reduce_tasks = 2;  // The number of reduce tasks that will be assigned to the job
		Configuration conf = new Configuration();
		
		// Create job for round 1
		
		// Create the job
		Job job_one = new Job(conf, "Driver Program Round One"); 
		
		// Attach the job to this Driver
		job_one.setJarByClass(Driver.class); 
		
		// Fix the number of reduce tasks to run
		// If not provided, the system decides on its own
		job_one.setNumReduceTasks(reduce_tasks);
		
		// The datatype of the Output Key 
		// Must match with the declaration of the Reducer Class
		job_one.setOutputKeyClass(Text.class); 
		
		// The datatype of the Output Value 
		// Must match with the declaration of the Reducer Class
		job_one.setOutputValueClass(IntWritable.class);
		
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
		// FileInputFormat.addInputPath(job_one, new Path(another_input_path)); // This is legal
		
		// The output HDFS path for this job
		// The output path must be one and only one
		// This must not be shared with other running jobs in the system
		FileOutputFormat.setOutputPath(job_one, new Path(temp));
		// FileOutputFormat.setOutputPath(job_one, new Path(another_output_path)); // This is not allowed
		
		// Run the job
		job_one.waitForCompletion(true); 
		
		
		// Create job for round 2
		// The output of the previous job can be passed as the input to the next
		// The steps are as in job 1
		
		Job job_two = new Job(conf, "Driver Program Round Two"); 
		job_two.setJarByClass(Driver.class); 
		job_two.setNumReduceTasks(1); 
		
		job_two.setOutputKeyClass(IntWritable.class); 
		job_two.setOutputValueClass(Text.class);
		job_two.setSortComparatorClass(IntComparator.class);
		
		// If required the same Map / Reduce classes can also be used
		// Will depend on logic if separate Map / Reduce classes are needed
		// Here we show separate ones
		job_two.setMapperClass(Map_Two.class); 
		job_two.setReducerClass(Reduce_Two.class);
		
		job_two.setInputFormatClass(TextInputFormat.class); 
		job_two.setOutputFormatClass(TextOutputFormat.class);
		
		// The output of previous job set as input of the next
		FileInputFormat.addInputPath(job_two, new Path(temp)); 
		FileOutputFormat.setOutputPath(job_two, new Path(output));
		
		// Run the job
		job_two.waitForCompletion(true); 
		
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
	public static class Map_One extends Mapper<LongWritable, Text, Text, IntWritable>  {
		
		private IntWritable one = new IntWritable(1);
		
		// The map method 
		public void map(LongWritable key, Text value, Context context) 
								throws IOException, InterruptedException  {
			
			// The TextInputFormat splits the data line by line.
			// So each map method receives one line from the input
			String line = value.toString();
			
			// Tokenize to get the individual words
			StringTokenizer tokens = new StringTokenizer(line);

			String prev = null;
			
			while (tokens.hasMoreTokens()) {
				boolean isEnd = false;
				String current = tokens.nextToken();
				
				// Convert to lower case
				current = current.toLowerCase();
				
				// Remove whitespace surrounding word
				current = current.trim();
				
				// Check to see if is end of sentence
				isEnd = (current.endsWith(".") || current.endsWith("!") || current.endsWith("?"));
				
				// Remove punctuation
//				current = current.replaceAll("[.;,!?]", "");
				current = current.replaceAll("[^-A-Za-z]", "");
				
				// If we have a bigram, output it with value of 1
				if (prev != null && !"".equals(current)) {
					context.write(new Text(prev + " " + current), one);
				}
				
				// If this isn't the last word in the sentence, store it as the previous
				if (!isEnd && !"".equals(current)) {
					prev = current;
				} else if (!isEnd) {
					// Do nothing...
				} else {
					prev = null;
				}
			} // End while
		} // End method "map"
	} // End Class Map_One
	
	
	// The reduce class
	// The key is Text and must match the datatype of the output key of the map method
	// The value is IntWritable and also must match the datatype of the output value of the map method
	public static class Reduce_One extends Reducer<Text, IntWritable, Text, IntWritable>  {
		// The reduce method
		// For key, we have an Iterable over all values associated with this key
		// The values come in a sorted fasion.
		public void reduce(Text key, Iterable<IntWritable> values, Context context) 
											throws IOException, InterruptedException  {
			int sum = 0;
			for (IntWritable val : values) {
				int value = val.get();
				sum += value;
			}

			context.write(key, new IntWritable(sum));
		} // End method "reduce" 
	} // End Class Reduce_One
	
	// The second Map Class
	public static class Map_Two extends Mapper<LongWritable, Text, IntWritable, Text>  {
		public void map(LongWritable key, Text value, Context context) 
				throws IOException, InterruptedException  {
			String line = value.toString();
			
			// Tokenize to get the individual words
			String[] tokens = line.split("\t");
			
			String bigram = tokens[0];
			String count = tokens[1];
			context.write(new IntWritable(Integer.parseInt(count)), new Text(bigram));
		}  // End method "map"
	}  // End Class Map_Two
	
	// The second Reduce class
	public static class Reduce_Two extends Reducer<IntWritable, Text, Text, IntWritable>  {
		
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