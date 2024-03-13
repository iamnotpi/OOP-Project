import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.videoio.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.imgproc.Imgproc;


public class Haar_Cascade_Classifier {
    public static void detectAndDisplay(Mat frame, CascadeClassifier faceCascade) {
        Mat frameGray = new Mat();
        Imgproc.cvtColor(frame, frameGray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(frameGray, frameGray);

        // Detect faces
        MatOfRect faces = new MatOfRect();
        faceCascade.detectMultiScale(frameGray, faces, 1.1, 6, 0 | Objdetect.CASCADE_SCALE_IMAGE, new Size(30, 30));

        for (Rect rect : faces.toArray()) {
            Imgproc.rectangle(frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 100, 0),2);
        }

        // Display the frame
        HighGui.imshow("Camera", frame);
    }

    public static void detectAndDisplay(Mat frame) {
        HighGui.imshow("Camera", frame);
    }

    public static void main(String[] args) {
        // Load the neccessary OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        String haarcascadePath = "C:/Program Files/opencv/build/etc/haarcascades/haarcascade_frontalface_default.xml";
        CascadeClassifier faceCascade = new CascadeClassifier(haarcascadePath);

        if (!faceCascade.load(haarcascadePath)) {
            System.out.println("Cannot load the haarcascade_frontalface_default.xml file.");
            System.exit(-1);
        }

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
            // Display the frame
            detectAndDisplay(frame, faceCascade);

            // Wait for 1ms and check if the user pressed the ESC key
            if (HighGui.waitKey(1) == 27) {
                break;
            }
        }
        System.exit(0);
    }
}