import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * Calculate an 8-bit XOR checksum of bytes 5000000000 to 5000000999
 * of the file /datasets/Lab1/bigdata.
 * 
 * @author breber
 */
public class HDFSXorChecksum {

	public static void main(String[] args) throws IOException {
		// The system configuration
		Configuration conf = new Configuration();

		// Get an instance of the Filesystem
		FileSystem fs = FileSystem.get(conf);

		String path_name = "/datasets/Lab1/bigdata";
		Path path = new Path(path_name);

		// The Input Data Stream to read from
		FSDataInputStream file = fs.open(path);

		// Seek to the right location
		byte[] tempBuf = new byte[1000];
		file.seek((long)5000000000L);
		
		// Read the 1000 bytes from the file
		if (1000 == file.read(tempBuf, 0, 1000)) {
			byte temp = 0;
			
			// Compute the XOR checksum
			for (int i = 0; i < tempBuf.length; i++) {
				temp ^= tempBuf[i];
			}
			
			// Print out the result
			System.out.println("Checksum: " + Integer.toHexString(temp));
		} else {
			System.out.println("Didn't read enough");
		}

		// Close the file and the file system instance
		file.close();
		fs.close();
	}

}
