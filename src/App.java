import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import static org.opencv.imgproc.Imgproc.FONT_HERSHEY_SIMPLEX;

public class App {
    public static void main(String[] args) {
        String YuNetModelPath = "E:/Study/Code/Java/FaceDetection/model/face_detection_yunet_2023mar.onnx";
        String MobileFaceNetPrototxt = "E:/Study/Code/Java/FaceDetection/model/mobilefacenet.prototxt";
        String MobileFaceNetModelPath = "E:/Study/Code/Java/FaceDetection/model/mobilefacenet.prototxt.caffemodel";
        // Load the necessary OpenCV library
        System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
        
        Database db = new Database();
        YuNet model = new YuNet(YuNetModelPath);
        MobileFaceNet fr = new MobileFaceNet(MobileFaceNetModelPath, MobileFaceNetPrototxt);
        
        db.addtoDatabase("Pi", "E:\\Study\\Code\\Java\\FaceDetection\\images\\Pi.jpg", fr, model);
        db.addtoDatabase("Joe Biden", "E:\\Study\\Code\\Java\\FaceDetection\\images\\JB_3.jpg", fr, model);

        VideoCapture camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            System.out.println("Cannot open the camera.");
            System.exit(-1);
        }

        Mat frame = new Mat();
        while (true) {
            camera.read(frame);
            if (frame.empty()) {
                System.out.println("Cannot read the frame.");
                System.exit(-1);
            }
            
            // Detect faces and display the frame
            Mat faces = model.detect(frame);
            List<Rect> faceList = model.detectedFaces(faces);
            for (Rect faceRect : faceList) {
                Mat face = new Mat(frame, faceRect);
                float[] embedding = fr.recognizeFace(face);
                String name = db.recognize(embedding);
                Imgproc.rectangle(frame, faceRect, new Scalar(0, 255, 0), 2);
                Imgproc.putText(frame, name, new org.opencv.core.Point(faceRect.x, faceRect.y - 10), FONT_HERSHEY_SIMPLEX, 0.9, new Scalar(0, 255, 0), 2);
            }
            HighGui.imshow("Face Recognition", frame);
            if (HighGui.waitKey(1) == 27) {
                break;
            }
        }
        System.exit(0);
    }
}