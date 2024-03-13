import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.videoio.VideoCapture;
import YuNet.YuNet;

public class App {
    public static void main(String[] args) {
        // Load the necessary OpenCV library
        System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
        
        // Instantiate a VideoCapture object using default camera
        VideoCapture camera = new VideoCapture(0);
        Mat frame = new Mat();

        if (!camera.isOpened()) {
            System.out.println("Cannot open the camera.");
            System.exit(-1);
        }
        while (true) {
            camera.read(frame);
            if (frame.empty()) {
                System.out.println("Cannot read the frame.");
                System.exit(-1);
            }
            
            // Detect faces and display the frame
            Mat faces = YuNet.detect(frame);
            YuNet.display(frame, faces);

            // Wait for 1ms and check if the user pressed the ESC key
            if (HighGui.waitKey(1) == 27) {
                break;
            }
        }
        System.exit(0);
    }
}