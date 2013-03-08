import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility methods for reading and writing a CS229 image.
 * 
 * @author Brian Reber
 */
public class ImageUtil {

	/**
	 * Read a CS229 image from the given file.
	 * 
	 * @param f
	 * The file to read from
	 * @return
	 * The CS229Image representing the image
	 * @throws IOException 
	 */
	public static CS229Image readInCS229Image(File f) throws IOException {
		CS229Image image = new CS229Image();
		InputStream input;
		int tempByte;
		int tempSize;

		input = new FileInputStream(f);
		
		// Read Magic Number and check
		if (input.read() != 0x42) {
			throw new IOException("Not a CS229 Image");
		}
		
		tempByte = input.read();
		
		if (tempByte == 0x01 || tempByte == 0x02 || tempByte == 0x04) {
			image.setChannelSizeR(tempByte);
		}
		
		tempByte = input.read();
		
		if (tempByte == 0x01 || tempByte == 0x02 || tempByte == 0x04) {
			image.setChannelSizeG(tempByte);
		}
		
		tempByte = input.read();
		
		if (tempByte == 0x01 || tempByte == 0x02 || tempByte == 0x04) {
			image.setChannelSizeB(tempByte);
		}
		
		tempByte = input.read();
		
		if (tempByte == 0x00 || tempByte == 0xFF) {
			image.setBW(tempByte == 0x00);
		}
		
		tempSize = input.read();
		tempSize |= (input.read() << 8);
		tempSize |= (input.read() << 8);
		tempSize |= (input.read() << 8);
		
		if (tempSize > 0) {
			image.setWidth(tempSize);
		}
		
		tempSize = input.read();
		tempSize |= (input.read() << 8);
		tempSize |= (input.read() << 8);
		tempSize |= (input.read() << 8);
		
		if (tempSize > 0) {
			image.setHeight(tempSize);
		}
		
		readImageData(input, image);
		
		return image;
	}
	
	/**
	 * Reads the image pixel data from the input stream to the given image data
	 * 
	 * @param input
	 * The input stream containing the data 
	 * @param image
	 * The image to put the pixel data into
	 * @throws IOException
	 */
	private static void readImageData(InputStream input, CS229Image image) throws IOException {
		CS229Image.Pixel[] data = new CS229Image.Pixel[image.getWidth() * image.getHeight() + 2];
		for (int i = 0; i < data.length; i++) {
			data[i] = new CS229Image.Pixel();
		}
		int[] currentBit = new int[3];
		int currentChannel = 0;
		int pos = 0;
		
		if (image.isBW()) {
			/* 
			 * While we haven't finished the file, read in bit by bit
			 * the values of the image. 
			 */
			while ((currentBit[0] = input.read()) != -1) {
				if (image.getChannelSizeR() == 1) {
					/* 4 bit - we want to read 2 pixels for each bit */
					data[pos++].red = (currentBit[0] >> 4) & 0x0F;
					data[pos++].red = currentBit[0] & 0x0F;
				} else if (image.getChannelSizeR() == 2) {
					/* 8 bit - we want to read 1 pixels for each bit */
					data[pos++].red = currentBit[0];
				} else if (image.getChannelSizeR() == 4 && currentChannel != 1) {
					/* 
					 * 16 bit - we want to read 1/2 of a pixel for each bit 
					 * here we set the current position to the current read 
					 * left shifted by 4 bits.
					 */
					data[pos].red = currentBit[0] << 8;
					currentChannel = 1;
				} else if (image.getChannelSizeR() == 4 && currentChannel == 1) {
					/* 
					 * 16 bit (part 2) - we want to read 1/2 of a pixel for each bit 
					 * here we or the current position to the current position, and  
					 * then increment our position
					 */
					data[pos++].red |= currentBit[0];
					currentChannel = 0;
				}
			}
		} else {
			int[] tempData = new int[image.getWidth() * image.getHeight() * 3];
			int i = 0;
			int tempBit = 0;
			int size;
			
			OffsetReadUpper ret = new OffsetReadUpper();
			ret.offset = 0;
			ret.readUpper = 0xF0;
			
			/* 
			 * While we haven't finished the file, read in bit by bit
			 * the values of the image. Since this is a color image,
			 * we need to handle 3 bits at a time, so we need to keep 
			 * track of the current 'channel'
			 */
			while ((tempBit = input.read()) != -1) {
				tempData[i++] = tempBit;
			}
			size = i;
			i = 0;
			while (ret.offset < size) {
				data[i].red = readPackedBits(tempData, ret, image.getChannelSizeR());
				data[i].green = readPackedBits(tempData, ret, image.getChannelSizeG());
				data[i].blue = readPackedBits(tempData, ret, image.getChannelSizeB());
				i++;
			}
		}
		
		image.setData(data);
	}
	
	/**
	 * A small inner class to hold the offset, and which bits to read
	 * for the reading of CS229 images.
	 * 
	 * @author Brian Reber
	 */
	private static class OffsetReadUpper {
		int offset;
		int readUpper;		
	}
	
	/**
	 * Reads bits from the int array given the channel size, and offset
	 * 
	 * @param readByte
	 * The array of data
	 * @param vals
	 * Object containing the offset and which bits to read
	 * @param channelSize
	 * the size of the current channel to read
	 * @return
	 * the value of the data in the given bits
	 */
	private static int readPackedBits(int[] readByte, OffsetReadUpper vals, int channelSize) {
		int data = 0;
		
		if (channelSize == 1) {
			if (vals.readUpper == 0xF0) {
				data = (readByte[vals.offset] & 0xF0) >> 4;
			vals.readUpper = 0x0F;
			} else if (vals.readUpper == 0x0F) {
				data = readByte[vals.offset] & 0x0F;
				vals.readUpper = 0xF0;
				vals.offset++;
			}
		} else if (channelSize == 2) {
			if (vals.readUpper == 0xF0) {
				data = readByte[vals.offset];
				vals.offset++;
				vals.readUpper = 0xF0;
			} else if (vals.readUpper == 0x0F) {
				data = readByte[vals.offset] & 0x0F;
				vals.offset++;
				data |= ((readByte[vals.offset] & 0xF0) >> 4);
				vals.readUpper = 0x0F;
			}
		} else {
			if (vals.readUpper == 0xF0) {
				data = (readByte[vals.offset] << 8);
				vals.offset++;
				data |= readByte[vals.offset];
				vals.offset++;
				vals.readUpper = 0xF0;
			} else if (vals.readUpper == 0x0F) {
				data = ((readByte[vals.offset] & 0x0F) << 12);
				vals.offset++;
				data |= (readByte[vals.offset] << 4);
				vals.offset++;
				data |= ((readByte[vals.offset] & 0xF0) >> 4);
				vals.readUpper = 0x0F;
			}
		}

		return data;
	}
	
	/**
	 * Writes a CS229 image to the given file
	 * 
	 * @param image
	 * The image to write
	 * @param file
	 * The file to write to
	 * @throws IOException 
	 */
	public static void writeCS229Image(CS229Image image, File file) throws IOException {
		OutputStream output;
		output = new FileOutputStream(file);
		
		output.write(0x42);
		
		/* Channel Size */
		output.write(image.getChannelSizeR());
		output.write(image.getChannelSizeG());
		output.write(image.getChannelSizeB());

		/* Black and White */
		output.write(image.isBW() ? 0x00 : 0xFF);
		
		/* Image Width */
		
		output.write((image.getWidth() & 0xFF));
		output.write(((image.getWidth() >> 8) & 0xFF));
		output.write(((image.getWidth() >> 16) & 0xFF));
		output.write(((image.getWidth() >> 24) & 0xFF));
		
		/* Image Height */
		output.write((image.getHeight() & 0xFF));
		output.write(((image.getHeight() >> 8) & 0xFF));
		output.write(((image.getHeight() >> 16) & 0xFF));
		output.write(((image.getHeight() >> 24) & 0xFF));
		
		writeImageData(output, image);
	}
	
	
	/**
	 * Packs bytes correctly given the current byte buffer, and the given data.
	 *
	 * @param  file - the OutputStream to write to
	 * @param  byteToWrite - the buffer of bits that is to be written - this method will modify
	 *				this if when the data is packed it doesn't contain a full byte to write
	 * @param  numBitsInByteToWrite - the number of bits that have been added to the buffer
	 * @param  channelSize - the channel size for the current data
	 * @param  data - the data that needs to be written
	 * @throws IOException 
	 */
	private static void packBitsAndWrite(OutputStream file, Byte byteToWrite, Integer numBitsInByteToWrite, int channelSize, int data) throws IOException {
		if (channelSize == 1) {
			if (numBitsInByteToWrite == 0) {
				/* 
				 * The current channel is 4 bits, and there isn't anything in the buffer 
				 * We will add the data to the buffer
				 */
				byteToWrite = (byte) data;
				numBitsInByteToWrite = 4;
			} else if (numBitsInByteToWrite == 4) {
				/* 
				 * The current channel is 4 bits, and there are 4 bits in the buffer.
				 * We will left-shift the buffer by 4 bits, and or the data to the buffer,
				 * write the data to the file, and clear the buffer
				 */
				byteToWrite = (byte) ((byteToWrite << 4) | data);
				file.write(byteToWrite);
				numBitsInByteToWrite = 0;
				byteToWrite = 0;
			}
		} else if (channelSize == 2) {
			if (numBitsInByteToWrite == 0) {
				/* 
				 * The current channel is 8 bits, and there are 0 bits in the buffer.
				 * We will write the byte size data to the file, and clear the buffer
				 * for good measure
				 */
				file.write(data);
				numBitsInByteToWrite = 0;
				byteToWrite = 0;
			} else if (numBitsInByteToWrite == 4) {
				/* 
				 * The current channel is 8 bits, and there are 4 bits in the buffer.
				 * We will left-shift the buffer by 4 bits, and or the upper 4 bits of data to the buffer,
				 * write the buffer to the file, and then add the lower 4 bits to the buffer
				 */
				byteToWrite = (byte) ((byteToWrite << 4) | ((data & 0xF0) >> 4));
				file.write(byteToWrite);
				numBitsInByteToWrite = 4;
				byteToWrite = (byte) (data & 0x0F);
			}
		} else {
			if (numBitsInByteToWrite == 0) {
				/* 
				 * The current channel is 16 bits, and there are 0 bits in the buffer.
				 * We will write the 2 bytes to the file, and clear the buffer for good measure
				 */
				file.write(data & 0x00FF);
				file.write((data & 0xFF00) >> 8);
				numBitsInByteToWrite = 0;
				byteToWrite = 0;
			} else if (numBitsInByteToWrite == 4) {
				/* 
				 * The current channel is 16 bits, and there are 4 bits in the buffer.
				 * We will left-shift the buffer by 4 bits, and or the upper-most 4 bits of data to the buffer,
				 * write the data to the file.  Then we will write the middle 2 bits to the file,
				 * and finally add the lowest 4 bits to the buffer.
				 */
				byteToWrite = (byte) ((byteToWrite << 4) | ((data & 0xF000) >> 12));
				file.write(byteToWrite);
				byteToWrite = (byte) ((data & 0x0FF0) >> 4);
				file.write(byteToWrite);
				byteToWrite = (byte) (data & 0x000F);
				numBitsInByteToWrite = 4;
			}
		}
	}
		
	/**
	 * Writes the image (pixel) data to the given file
	 *
	 * @param  file - the OutputStream to write to
	 * @param  image - the image struct containing the data to be written
	 * @throws IOException 
	 */
	private static void writeImageData(OutputStream file, CS229Image image) throws IOException {
		int i;
		Byte byteToWrite = 0;
		Integer numBitsInByteToWrite = 0;
		CS229Image.Pixel[] data = image.getData();
		for (i = 0; i < image.getWidth() * image.getHeight(); i++) {
			if (image.isBW()) {
				packBitsAndWrite(file, byteToWrite, numBitsInByteToWrite, image.getChannelSizeR(), data[i].red);
			} else {
				packBitsAndWrite(file, byteToWrite, numBitsInByteToWrite, image.getChannelSizeR(), data[i].red);
				packBitsAndWrite(file, byteToWrite, numBitsInByteToWrite, image.getChannelSizeG(), data[i].green);
				packBitsAndWrite(file, byteToWrite, numBitsInByteToWrite, image.getChannelSizeB(), data[i].blue);
			}
		}
		
		// If we ended with data still in the byteToWrite buffer, output that, and fil in the rest
		// with zeros
		if (numBitsInByteToWrite == 4) {
			file.write((byteToWrite << 4) & 0xF0);
		}
	}
}
