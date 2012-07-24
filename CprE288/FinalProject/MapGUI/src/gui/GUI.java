package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Creates and shows a GUI for mapping obstacles in the CprE 288
 * robot obstacle course
 * 
 * @author breber
 */
public class GUI {

	/**
	 * The frame holding the grid
	 */
	private JFrame frame;
	
	/**
	 * The frame holding the buttons
	 */
	private JFrame buttonFrame;
	
	/**
	 * The grid
	 */
	private GridPlan plan;
	
//	static Process child;
//	static OutputStream stream;
	
	public static void main(String[] args) throws IOException {
		final GUI runner = new GUI();
		runner.plan = new GridPlan();
		runner.plan.robot = new Robot(400, 400);
		
//		String portName = JOptionPane.showInputDialog("Port Name:");
//		
//		child = Runtime.getRuntime().exec("Runnable.exe " + portName);
//		stream = child.getOutputStream();
		
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
	 * Generates and displays the GUI
	 */
	private void createAndShowGUI() {
		frame = new JFrame("iRobot Map");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(800, 800));
		frame.setPreferredSize(new Dimension(800, 800));
		frame.getContentPane().add(plan);
		frame.pack();
		frame.setVisible(true);
		
		// Create buttons
		buttonFrame = new JFrame("Controls");
		
		JButton moveForward = new JButton("Move Forward (10 cm)");
		JButton moveBackward = new JButton("Move Backward (5 cm)");
		JButton rotate45 = new JButton("Rotate 45");
		JButton rotateN45 = new JButton("Rotate -45");
		JButton rotate10 = new JButton("Rotate 10");
		JButton rotateN10 = new JButton("Rotate -10");
		JButton addPillar = new JButton("Add Pillar");
		JButton addCliff = new JButton("Add Cliff");
		JButton addVirtWall = new JButton("Add Virtual Wall");
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		moveForward.setAlignmentX(Component.CENTER_ALIGNMENT);
		moveForward.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
//				try {
//					stream.write('w');
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
				// Move forward 10 cm
				for (int i = 0; i < 10; i++) {
					plan.robot.moveForward();
				}
				frame.repaint();
			}
		});
		
		moveBackward.setAlignmentX(Component.CENTER_ALIGNMENT);
		moveBackward.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
//				try {
//					stream.write('s');
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
				// Move backward 5 cm
				for (int i = 0; i < 5; i++) {
					plan.robot.moveBackward();
				}
				frame.repaint();
			}
		});
		
		rotate45.setAlignmentX(Component.CENTER_ALIGNMENT);
		rotate45.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
//				try {
//					stream.write('d');
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
				plan.robot.rotate(45);
				frame.repaint();
			}
		});
		
		rotateN45.setAlignmentX(Component.CENTER_ALIGNMENT);
		rotateN45.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
//				try {
//					stream.write('a');
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
				plan.robot.rotate(-45);
				frame.repaint();
			}
		});
		
		rotate10.setAlignmentX(Component.CENTER_ALIGNMENT);
		rotate10.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
//				try {
//					stream.write('c');
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
				plan.robot.rotate(10);
				frame.repaint();
			}
		});
		
		rotateN10.setAlignmentX(Component.CENTER_ALIGNMENT);
		rotateN10.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
//				try {
//					stream.write('z');
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
				plan.robot.rotate(-10);
				frame.repaint();
			}
		});
		
		addPillar.setAlignmentX(Component.CENTER_ALIGNMENT);
		addPillar.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				plan.addPillar();
				frame.repaint();
			}
		});
		
		addCliff.setAlignmentX(Component.CENTER_ALIGNMENT);
		addCliff.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				plan.addCliff();
				frame.repaint();
			}
		});
		
		addVirtWall.setAlignmentX(Component.CENTER_ALIGNMENT);
		addVirtWall.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				plan.addVirtWall();
				frame.repaint();
			}
		});
		
		panel.add(moveForward);
		panel.add(moveBackward);
		panel.add(rotate45);
		panel.add(rotateN45);
		panel.add(rotate10);
		panel.add(rotateN10);
		panel.add(addCliff);
		panel.add(addPillar);
		panel.add(addVirtWall);
		buttonFrame.getContentPane().add(panel);
		buttonFrame.pack();
		buttonFrame.setVisible(true);
	}
	
}
