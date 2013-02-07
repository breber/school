/**
  * Cpr E 419 - Lab 3
  * 
  * @author breber (Brian Reber)
  */

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
		
//		String input = "/datasets/Lab3/Graph";
		String input = "/user/breber/ex1.txt";
		String temp = "/user/breber/Lab3/temp";
		String output = "/user/breber/Lab3/output/";
		
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
		
		
		Job job_two = new Job(conf, Experiment1.class.getName() + " Round Two"); 
		job_two.setJarByClass(Experiment1.class); 
		job_two.setNumReduceTasks(reduce_tasks);

		job_two.setOutputKeyClass(Text.class); 
		job_two.setOutputValueClass(Text.class);
		
		job_two.setMapperClass(Map_Two.class); 
		job_two.setReducerClass(Reduce_Two.class);
		
		job_two.setInputFormatClass(TextInputFormat.class);  
		job_two.setOutputFormatClass(TextOutputFormat.class);
		
		FileInputFormat.addInputPath(job_two, new Path(temp + "round1")); 
		FileOutputFormat.setOutputPath(job_two, new Path(temp + "round2"));
		
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
	
	public static class Map_One extends Mapper<LongWritable, Text, Text, Text>  {
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			
			// Tokenize to get the two vertices
			StringTokenizer tokens = new StringTokenizer(line);
			
			String vertex1 = tokens.nextToken();
			String vertex2 = tokens.nextToken();
			
			// Output the cited patent, with a value of the citing patent
			context.write(new Text(vertex2), new Text(vertex1));
			context.write(new Text(vertex1), new Text("(" + vertex2.toString() + "," + vertex1.toString() + ")"));
		}
	}
	
	public static class Reduce_One extends Reducer<Text, Text, Text, Text>  {
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			List<String> prev = new ArrayList<String>();
			for (Text val : values) {
				String valString = val.toString();
				if (valString.matches("\\(.+?,.+?\\)")) {
					context.write(val, new Text(""));
					
					for (String s : prev) {
						String temp = valString.substring(0, valString.length() - 1);
						temp = temp + "," + s + ")";
						
						context.write(new Text(temp), new Text(""));
					}
				} else {
					prev.add(valString);
					
					context.write(new Text("(" + key.toString() + "," + valString + ")"), new Text(""));
				}
			}
		}
	}
	
	
	public static class Map_Two extends Mapper<LongWritable, Text, Text, Text>  {
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException  {
			String line = value.toString().substring(1, value.toString().length() - 1);
			
			System.out.println("Map Val: " + value.toString());
			
			// Tokenize to get the two vertices
			StringTokenizer tokens = new StringTokenizer(line, ",");
			
			String vertex1 = tokens.nextToken();
			StringBuilder stringBuilder = new StringBuilder("(");
			
			while (tokens.hasMoreTokens()) {
				stringBuilder.append(tokens.nextToken());

				if (tokens.hasMoreTokens()) {
					stringBuilder.append(",");
				}
			}
			
			stringBuilder.append(")");
			
			// Output the cited patent, with a value of the citing patent
			context.write(new Text(vertex1), new Text(stringBuilder.toString()));
		}
	}
	
	public static class Reduce_Two extends Reducer<Text, Text, Text, Text>  {
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			Map<String, String> set = new HashMap<String, String>();
			for (Text val : values) {
				System.out.println("Received Key: " + key.toString() + " Val: " + val.toString());
				String valString = val.toString();
				valString = valString.replace("(", "");
				valString = valString.replace(")", "");
				
				if (valString.contains(",")) {
					set.put(valString.substring(0, valString.indexOf(",")), valString);
				} else {
					set.put(valString, valString);
				}
			}
			
			System.out.println("Set Key: " + set.keySet() + " Vals: " + set.values());
			
			// TODO: could probably be simplified
			for (String s : set.values()) {
				context.write(key, new Text(s));
			}
		}
	}
	
}