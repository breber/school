import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Creates a Swing UI for viewing and convolving CS229 images.
 * 
 * @author Brian Reber
 */
public class DisplayRunner {
	/**
	 * The convolution kernel to use on the image.  If this is updated with a different size, the method
	 * call needs to be updated too.
	 */
	public static final double[] convolutionKernel = new double[] { 0, -1, 2, -1, 0, 0, -1, 2, -1, 0, 0, -1, 2, -1, 0, 0, -1, 2, -1, 0, 0, -1, 2, -1, 0};
	
	/**
	 * The current displayed image
	 */
	private CS229Image currentImage;
	
	/**
	 * The frame holding everything
	 */
	private JFrame frame;
	
	/**
	 * The panel containing the CS229 image
	 */
	private JPanel imagePanel;
	
	/**
	 * Indicates whether the current image has been modified
	 */
	private boolean needsSaving;
	
	public static void main(String[] args) {
		final DisplayRunner runner = new DisplayRunner();
		runner.currentImage = new CS229Image();
		runner.needsSaving = false;
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			/* (non-Javadoc)
			 * @see java.lang.Runnable#run()
			 */
			@Override
			public void run() {
				runner.createAndShowGUI();
			}
		});
	}
	
	/**
	 * Creates and displays the UI, initializing the buttons.
	 */
	private void createAndShowGUI() {
		currentImage = new CS229Image();
		
		frame = new JFrame("CS229 Image Studio");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		imagePanel = new JPanel();
		imagePanel.setSize(600, 600);
		imagePanel.setPreferredSize(new Dimension(600, 600));
		imagePanel.add(currentImage);
		frame.getContentPane().add(imagePanel, BorderLayout.NORTH);
		
		final JButton openButton = new JButton("Open");
		final JButton saveButton = new JButton("Save");
		final JButton convoluteButton = new JButton("Perform Convolution");
		
		openButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {	}
			@Override
			public void mousePressed(MouseEvent e) {	}
			@Override
			public void mouseExited(MouseEvent e) { 	}
			@Override
			public void mouseEntered(MouseEvent e) {	}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				boolean needsReopening = false;
				
				// If the current image needs saving, we prompt the user
				// to see if they want to save their modifications.
				// If so, we prompt them to save, otherwise, we just continue
				if (needsSaving) {
					int ret = promptUserToSave();
					
					if (ret == JOptionPane.YES_OPTION) {
						openButton.setEnabled(false);
						convoluteButton.setEnabled(false);
						saveButton.setEnabled(false);
						
						saveCurrentCS229File();
						
						openButton.setEnabled(true);
						convoluteButton.setEnabled(true);
						saveButton.setEnabled(true);
					}
				}
				
				do {
					needsReopening = false;
					
					final File file = openCS229File();
					if (file != null) {
						imagePanel.remove(currentImage);
						currentImage = new CS229Image();
						frame.repaint();
						
						openButton.setEnabled(false);
						convoluteButton.setEnabled(false);
						saveButton.setEnabled(false);
						
						try {
							currentImage = ImageUtil.readInCS229Image(file);
							
							// If the image is too big, we will prompt the user to try again
							if (currentImage.getWidth() > 600 || currentImage.getHeight() > 600) {
								int val = JOptionPane.showOptionDialog(null, "Image too big for viewer. Try again?", null, JOptionPane.YES_NO_OPTION, 
										JOptionPane.QUESTION_MESSAGE, null, null, null);
								
								needsReopening = (val == JOptionPane.YES_OPTION);
								if (!needsReopening) {
									openButton.setEnabled(true);
									convoluteButton.setEnabled(false);
									saveButton.setEnabled(false);
								}
								continue;
							}
						} catch (IOException er) {
							showErrorMessage(er.getMessage(), true);
						}
						imagePanel.add(currentImage);
						frame.repaint();
						
						openButton.setEnabled(true);
						convoluteButton.setEnabled(true);
						saveButton.setEnabled(true);
					}
				} while (needsReopening);
			}
		});
		
		convoluteButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {	}
			@Override
			public void mousePressed(MouseEvent e) {	}
			@Override
			public void mouseExited(MouseEvent e) { 	}
			@Override
			public void mouseEntered(MouseEvent e) {	}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						openButton.setEnabled(false);
						convoluteButton.setEnabled(false);
						saveButton.setEnabled(false);
						
						needsSaving = true;
						
						currentImage.convoluteFromJava(5, DisplayRunner.convolutionKernel);
						frame.repaint();
						
						openButton.setEnabled(true);
						convoluteButton.setEnabled(true);
						saveButton.setEnabled(true);
					}
				});
				
				thread.setName("Convolute");
				thread.start();
			}
		});
		
		saveButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {	}
			@Override
			public void mousePressed(MouseEvent e) {	}
			@Override
			public void mouseExited(MouseEvent e) { 	}
			@Override
			public void mouseEntered(MouseEvent e) {	}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				openButton.setEnabled(false);
				convoluteButton.setEnabled(false);
				saveButton.setEnabled(false);
				
				saveCurrentCS229File();
				
				openButton.setEnabled(true);
				convoluteButton.setEnabled(true);
				saveButton.setEnabled(true);
			}
		});
		
		frame.getContentPane().add(openButton, BorderLayout.WEST);
		frame.getContentPane().add(convoluteButton, BorderLayout.CENTER);
		frame.getContentPane().add(saveButton, BorderLayout.EAST);
		
		openButton.setEnabled(true);
		convoluteButton.setEnabled(false);
		saveButton.setEnabled(false);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Prompts the user to see if they want to save the current unsaved image.
	 * 
	 * @return the result of the JOptionPane. Use the static variables in JOptionPane to
	 * 		figure out what they did.
	 */
	private int promptUserToSave() {
		int prompt = JOptionPane.showOptionDialog(null, "Would you like to save the current image?", null, JOptionPane.YES_NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE, null, null, null);
		
		return prompt;
	}
	
	/**
	 * Prompts the user to find an image to open, and returns the file they selected.
	 * 
	 * @return The file the user selected
	 */
	private File openCS229File() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CS229 Image", "cs229", "dat", "cs");
		chooser.setFileFilter(filter);
		chooser.showOpenDialog(null);
		
		return chooser.getSelectedFile();
	}
	
	/**
	 * Prompts the user to find a place to save the current image, and then writes the image
	 * to that location.
	 */
	private void saveCurrentCS229File() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CS229 Image", "cs229", "dat", "cs");
		chooser.setFileFilter(filter);
		chooser.showSaveDialog(null);
		
		if (chooser.getSelectedFile() != null) {
			try {
				ImageUtil.writeCS229Image(currentImage, chooser.getSelectedFile());
			} catch (IOException e) {
				showErrorMessage(e.getMessage(), false);
			}
			needsSaving = false;
		}
	}
	
	/**
	 * Shows an error message to the user
	 * 
	 * @param message
	 * The message to show to the user
	 * @param clearImage
	 * Whether to clear the image view or not
	 */
	private void showErrorMessage(String message, boolean clearImage) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
		
		if (clearImage) {
			imagePanel.remove(currentImage);
			currentImage = new CS229Image();
			frame.repaint();
		}
	}
}
