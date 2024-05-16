import java.awt.Dimension; 
import java.awt.EventQueue; 
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 

import javax.swing.ImageIcon; 
import javax.swing.JButton; 
import javax.swing.JFrame; 
import javax.swing.JLabel; 
import javax.swing.JOptionPane; 
import org.opencv.core.Core; 
import org.opencv.core.Mat; 
import org.opencv.core.MatOfByte; 
import org.opencv.imgcodecs.Imgcodecs; 
import org.opencv.videoio.VideoCapture; 
 
public class Camera extends JFrame { 
	private JLabel cameraScreen; 
	private JButton btnCapture; 
	private VideoCapture capture; 
	private Mat image; 
	private boolean clicked = false; 

	public Camera() { 
		// Designing UI 
		setLayout(null); 

		cameraScreen = new JLabel(); 
		cameraScreen.setBounds(0, 0, 640, 480); 
		add(cameraScreen); 

		btnCapture = new JButton("Capture"); 
		int windowWidth = 640;
		int buttonWidth = 80;
		int buttonX = (windowWidth - buttonWidth) / 2;
		btnCapture.setBounds(buttonX, 480, buttonWidth, 40); 
		add(btnCapture);

		btnCapture.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) { 
				clicked = true; 
			} 
		}); 

		setSize(new Dimension(640, 560)); 
		setLocationRelativeTo(null); 
		setTitle("Camera Window");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		setVisible(true); 
	} 

	// Creating a camera 
	public void startCamera() { 
		capture = new VideoCapture(0); 
		image = new Mat(); 
		byte[] imageData; 

		ImageIcon icon; 
		while (true) { 
			// read image to matrix 
			capture.read(image); 

			// convert matrix to byte 
			final MatOfByte buf = new MatOfByte(); 
			Imgcodecs.imencode(".jpg", image, buf); 

			imageData = buf.toArray(); 

			// Add to JLabel 
			icon = new ImageIcon(imageData); 
			cameraScreen.setIcon(icon); 

			// Capture and save to file 
			if (clicked) { 
				// prompt for enter image name 
				String name = null;
				while (name == null || name.trim().isEmpty()) {
					name = JOptionPane.showInputDialog("Enter image name:");
					if (name == null || name.trim().isEmpty()) {
						JOptionPane.showMessageDialog(null, "You must enter a name for the image.");
					}
				}

				// Write to file 
				Imgcodecs.imwrite("images/" + name + ".jpg", 
								image); 

				clicked = false; 
			} 
		} 
	} 

	// Main driver method 
	public static void main(String[] args) { 
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
		EventQueue.invokeLater(new Runnable() { 
			// Overriding existing run() method 
			@Override 
			public void run() { 
				final Camera camera = new Camera(); 

				// Start camera in thread 
				new Thread(new Runnable() { 
					@Override 
					public void run() { 
						camera.startCamera(); 
					} 
				}).start(); 
			} 
		}); 
	} 
}
