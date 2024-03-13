package YuNet;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.FaceDetectorYN;

public class YuNet {
    public static void display(Mat image, Mat faces) {
        /**
         * Draw rectangles around the detected faces
         * @param image: the input image
         * @param faces: the detected faces
         */
        for (int i = 0; i < faces.rows(); i++) {
            double x = faces.get(i, 0)[0];
            double y = faces.get(i, 1)[0];
            double w = faces.get(i, 2)[0];
            double h = faces.get(i, 3)[0];
            Imgproc.rectangle(image, new Rect((int)x, (int)y, (int)w, (int)h), new Scalar(0, 255, 0), 2);
        }
        HighGui.imshow("Face Detection", image);
    }
    public static Mat detect(Mat image) {
        /**
         * Detect faces in the input image
         * @param image: the input image
         */
        // Load the necessary OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Size size = new Size(320, 320);
        float scoreThreshold = 0.8f;
        float nmsThreshold = 0.3f;
        int topK = 5000;

        FaceDetectorYN face_detector = FaceDetectorYN.create(
            "E:/Study/Code/Java/FaceDetection/model/face_detection_yunet_2023mar.onnx", "", size, scoreThreshold, nmsThreshold, topK);

        // Resize the image
        int imgWidth = (int)image.width();
        int imgHeight = (int)image.height();
        Size imgSize = new Size(imgWidth, imgHeight);
        Imgproc.resize(image, image, imgSize);

        face_detector.setInputSize(imgSize);

        Mat faces = new Mat();
        face_detector.detect(image, faces); // See README.md for more details
        return faces;
    }
}