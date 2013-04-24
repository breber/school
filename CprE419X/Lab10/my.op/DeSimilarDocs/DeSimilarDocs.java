import com.ibm.streams.operator.*;

public class DeSimilarDocs extends AbstractOperator {

	@Override
	public void initialize(OperatorContext context) throws Exception {
		super.initialize(context); 
	}

	public void process(StreamingInput stream, Tuple tuple) throws Exception {
		final StreamingOutput<OutputTuple> output = getOutput(0);
		String timestamp = tuple.getString("ts");
		String fileName = tuple.getString("filename");

		OutputTuple outputTuple = output.newTuple();
		outputTuple.setString("ts", fileName);
		outputTuple.setString("filename", fileName + "~~" + timestamp);
		output.submit(outputTuple);
	}

}