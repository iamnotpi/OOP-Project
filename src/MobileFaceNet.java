import org.opencv.core.*;
import org.opencv.dnn.*;

public class MobileFaceNet {
    private Net faceRecognizer;

    public MobileFaceNet(String recognizerModelPath, String recognizerModel_prototxtPath) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        faceRecognizer = Dnn.readNetFromCaffe(recognizerModel_prototxtPath, recognizerModelPath);
    }
    public float[] recognizeFace(Mat face) {
        Mat blob = Dnn.blobFromImage(face, 1.0 / 128.0, new Size(112, 112), new Scalar(127.5, 127.5, 127.5), true, false);
        faceRecognizer.setInput(blob);
        Mat embeddings = faceRecognizer.forward();
        float[] embedding = new float[(int) embeddings.total()];
        embeddings.get(0, 0, embedding);
        return embedding;
    }
}