import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * An abstraction of a CS229 image. Can also be viewed as a JPanel
 * 
 * @author Brian Reber
 */
public class CS229Image extends JPanel {

	/**
	 * Load the convolvejni library
	 */
	static {
		System.loadLibrary("convolvejni");
	}
	
	/**
	 * Represents a pixel in an image, with a red, green and blue channel.
	 * 
	 * @author Brian Reber
	 */
	static class Pixel {
		public int red;
		public int green;
		public int blue;
	}

	/**
	 * The width of this image
	 */
	private int width;
	
	/**
	 * The height of this image
	 */
	private int height;
	
	/**
	 * Whether this image is black and white or not
	 */
	private boolean isBW;
	
	/**
	 * The red channel size
	 */
	private int channelSizeR;
	
	/**
	 * The green channel size
	 */
	private int channelSizeG;
	
	/**
	 * The blue channel size
	 */
	private int channelSizeB;
	
	/**
	 * The image pixel data
	 */
	private Pixel[] data;

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the isBW
	 */
	public boolean isBW() {
		return isBW;
	}

	/**
	 * @param isBW the isBW to set
	 */
	public void setBW(boolean isBW) {
		this.isBW = isBW;
	}

	/**
	 * @return the channelSizeR
	 */
	public int getChannelSizeR() {
		return channelSizeR;
	}

	/**
	 * @param channelSizeR the channelSizeR to set
	 */
	public void setChannelSizeR(int channelSizeR) {
		this.channelSizeR = channelSizeR;
	}

	/**
	 * @return the channelSizeG
	 */
	public int getChannelSizeG() {
		return channelSizeG;
	}

	/**
	 * @param channelSizeG the channelSizeG to set
	 */
	public void setChannelSizeG(int channelSizeG) {
		this.channelSizeG = channelSizeG;
	}

	/**
	 * @return the channelSizeB
	 */
	public int getChannelSizeB() {
		return channelSizeB;
	}

	/**
	 * @param channelSizeB the channelSizeB to set
	 */
	public void setChannelSizeB(int channelSizeB) {
		this.channelSizeB = channelSizeB;
	}

	/**
	 * @return the data
	 */
	public synchronized Pixel[] getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public synchronized void setData(Pixel[] data) {
		this.data = data;
	}
	
	/**
	 * Gets the maximum value for the given channel size
	 * 
	 * @param channelSize
	 * The channel size to test
	 * @return
	 * The maximum value for this channel width
	 */
	private int getMaxVal(int channelSize) {
		switch (channelSize) {
			case 0x01: return 0xF;
			case 0x02: return 0xFF;
			case 0x04: return 0xFFFF;
			default: return 1;
		}
	}
	
	@Override
	protected synchronized void paintComponent(Graphics gr) {
		for (int i = 0; i < width; i++) {
        	for (int j = 0; j < height; j++) {
        		Pixel current = data[i + j * width];
        		int tempR = current.red;
        		int tempG = current.green;
        		int tempB = current.blue;
        		
        		// If it is a black and white image, set all channels to the red channel value
        		if (isBW()) {
        			tempG = tempR;
        			tempB = tempR;
        		}
        		
        		// Scale all values to 8 bit for viewing
        		int r = (int) (tempR * 0xFF / getMaxVal(getChannelSizeR()));
        		int g = (int) (tempG * 0xFF / getMaxVal(getChannelSizeG()));
        		int b = (int) (tempB * 0xFF / getMaxVal(getChannelSizeB()));
        		
        		try {
        			gr.setColor(new Color(r, g, b));
        		} catch (IllegalArgumentException e) {
        			System.err.println(String.format("Invalid color at {%d %d}: %d, %d, %d", i, j, r, g, b));
        		}
                gr.fillRect(i, j, 1, 1);
        	}
        }
    }
	
	/**
	 * Performs a convolution on the current image instance.
	 * 
	 * @param dimension
	 * The size of the convolution kernel
	 * @param kernelData
	 * The actual kernel data
	 */
	public synchronized native void convoluteFromJava(int dimension, double[] kernelData);
}
