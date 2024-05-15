import java.util.ArrayList;
import java.util.List;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.FaceDetectorYN;

public class YuNet {
    private static FaceDetectorYN face_detector;
    private Size size;
    private float scoreThreshold;
    private float nmsThreshold;
    private int topK;

    public YuNet(String YuNetModelPath) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        size = new Size(320, 320);
        scoreThreshold = 0.8f;
        nmsThreshold = 0.3f;
        topK = 5000;
        face_detector = FaceDetectorYN.create(YuNetModelPath, "", size, scoreThreshold, nmsThreshold, topK);
    }
    public void display(Mat image, Mat faces) {
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
        HighGui.waitKey();
    }
    public Mat detect(Mat image) {
        /**
         * Detect faces in the input image
         * @param image: the input image
         */

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
    public List<Rect> detectedFaces(Mat faces) {
        /**
         * Get the detected faces as a list of Rect objects
         * @param faces: the detected faces
         */
        List<Rect> faceList = new ArrayList<>();
        for (int i = 0; i < faces.rows(); i++) {
            double x = faces.get(i, 0)[0];
            double y = faces.get(i, 1)[0];
            double w = faces.get(i, 2)[0];
            double h = faces.get(i, 3)[0];
            faceList.add(new Rect((int)x, (int)y, (int)w, (int)h));
        }
        return faceList;
    }
}