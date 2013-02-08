/**
  * Cpr E 419 - Lab 3
  * 
  * @author breber (Brian Reber)
  */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class Experiment2 extends Configured implements Tool {
	
	public static void main (String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new Experiment2(), args);
		System.exit(res); 
	}
	
	public int run ( String[] args ) throws Exception {
		
		String input = "/datasets/Lab3/Graph";
//		input = "/user/breber/ex1.txt";
		String temp = "/user/breber/Lab3/exp2/temp";
		String output = "/user/breber/Lab3/exp2/output/";
		
		int reduce_tasks = 2;  // The number of reduce tasks that will be assigned to the job
		Configuration conf = new Configuration();
		
		// Create the job
		Job job_one = new Job(conf, Experiment2.class.getName() + " Round One"); 
		job_one.setJarByClass(Experiment2.class); 
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
		
		
		Job job_two = new Job(conf, Experiment2.class.getName() + " Round Two"); 
		job_two.setJarByClass(Experiment2.class); 
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
		
		
		Job job_three = new Job(conf, Experiment2.class.getName() + " Round Three"); 
		job_three.setJarByClass(Experiment2.class); 
		job_three.setNumReduceTasks(1); 

		job_three.setOutputKeyClass(Text.class); 
		job_three.setOutputValueClass(IntWritable.class);

		job_three.setMapperClass(Map_Three.class); 
		job_three.setReducerClass(Reduce_Three.class);

		job_three.setInputFormatClass(TextInputFormat.class); 
		job_three.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job_three, new Path(temp + "round2")); 
		FileOutputFormat.setOutputPath(job_three, new Path(output));

		job_three.waitForCompletion(true); 
		
		return 0;
	}
	
	/**
	 * First round of map reduce takes in
	 * 
	 *  citing_node	cited_node
	 * 
	 * And outputs:
	 * 
	 * citing_node		cited_node
	 * cited_node		citing_node
	 * 
	 * For each node.
	 * 
	 * @author breber
	 */
	public static class Map_One extends Mapper<LongWritable, Text, Text, Text> {
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			
			// Tokenize to get the two vertices
			StringTokenizer tokens = new StringTokenizer(line);
			
			String vertex1 = tokens.nextToken();
			String vertex2 = tokens.nextToken();
			
			// Output the cited patent, with a value of the citing patent
			context.write(new Text(vertex2), new Text(vertex1));
			context.write(new Text(vertex1), new Text(vertex2));
		}
	}
	
	/**
	 * First round of reducer takes in
	 * 
	 * node		list_of_neighboring_nodes
	 * 
	 * And outputs:
	 * 
	 * node		(list_of_neighboring_nodes)]
	 * 
	 * @author breber
	 */
	public static class Reduce_One extends Reducer<Text, Text, Text, Text> {
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			StringBuilder stringBuilder = new StringBuilder("(");
			boolean isFirst = true;
			
			for (Text val : values) {
				if (!isFirst) {
					stringBuilder.append(",");
				} else {
					isFirst = false;
				}
				
				stringBuilder.append(val.toString());
			}
			
			stringBuilder.append(")");
				
			context.write(key, new Text(stringBuilder.toString()));
		}
	}
	
	/**
	 * Second round of mapper takes in
	 * 
	 * node		list_of_neighboring_nodes
	 * 
	 * And outputs a list of neighbors for all of the node's neighbors:
	 * 
	 * neighbor1	[node,(list_of_neighboring_nodes)]
	 * neighbor2	[node,(list_of_neighboring_nodes)]
	 * 
	 * @author breber
	 */
	public static class Map_Two extends Mapper<LongWritable, Text, Text, Text> {
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException  {
			String line = value.toString();
			StringTokenizer tokens = new StringTokenizer(line);
			String lineKey = tokens.nextToken();
			String lineVal = tokens.nextToken();

			// Remove parenthesis from the list of neighbors
			lineVal = lineVal.replace("(", "");
			lineVal = lineVal.replace(")", "");
			
			// Split into tokens to iterate through
			String[] neighbors = lineVal.split(",");
			
			for (String s : neighbors) {
				context.write(new Text(s), new Text("[" + lineKey + ",(" + lineVal + ")]"));
			}
		}
	}
	
	/**
	 * Second round of reducer takes in
	 * 
	 * neighbor1	[node,(list_of_neighboring_nodes)]
	 * 
	 * And outputs all the triangles the node is a part of:
	 * 
	 * <node1,node2,node3>
	 * 
	 * @author breber
	 */
	public static class Reduce_Two extends Reducer<Text, Text, Text, Text> {
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			Map<String, String> neighborsVisited = new HashMap<String, String>();
			String currentNode = key.toString();
			
			for (Text val : values) {
				String fullValStruct = val.toString();
				String node = fullValStruct.substring(1, fullValStruct.indexOf(","));
				String neighbors = fullValStruct.substring(fullValStruct.indexOf(","));
				neighbors = neighbors.replace("(", "");
				neighbors = neighbors.replace(")", "");
				
				neighborsVisited.put(node, neighbors);
			}
			
			for (String node : neighborsVisited.keySet()) {
				String neighbors = neighborsVisited.get(node);
				String[] neighborsList = neighbors.split(",");
				
				for (String s : neighborsList) {
					// If this isn't the node we are currently reducing
					if (!currentNode.equalsIgnoreCase(s)) {
						// And we have visited this node before
						if (neighborsVisited.containsKey(s)) {
							// Create a list to sort containing the three nodes
							List<String> toPrint = new ArrayList<String>(3);
							toPrint.add(currentNode);	// The current key node
							toPrint.add(node);			// The node we are evaluating
							toPrint.add(s);				// The one we previously examined
							
							Collections.sort(toPrint);
							
							context.write(new Text("<" + toPrint.get(0) + "," + toPrint.get(1) + "," + toPrint.get(2) + ">"), new Text());
						}
					}
				}
			}
		}
	}
	
	/**
	 * Third round of mapper takes in
	 * 
	 * <node1,node2,node3>
	 * 
	 * And mirrors the input to the output
	 * 
	 * <node1,node2,node3>
	 * 
	 * @author breber
	 */
	public static class Map_Three extends Mapper<LongWritable, Text, Text, IntWritable> {
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString().trim();

			context.write(new Text(line), new IntWritable(1));
		}
	}

	/**
	 * Third round of mapper takes in
	 * 
	 * <node1,node2,node3>
	 * 
	 * And counts all the times the reduce method is called, which is the
	 * number of triangles.
	 * 
	 * @author breber
	 */
	public static class Reduce_Three extends Reducer<Text, IntWritable, IntWritable, Text> {

		private static int count = 0;

		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			count++;
			
			context.write(new IntWritable(count), new Text());
		}
	}
	
}