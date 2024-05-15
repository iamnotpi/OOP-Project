import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

public class Database {
    private static List<float[]> embeddings;
    private static List<String> names;
    public final static int MAX_PEOPLE = 10;
    public static int currentPeople = 0;

    public Database() {
        embeddings = new ArrayList<>();
        names = new ArrayList<>();
    }
    public void addtoDatabase(String name, float[] embedding) {
        names.add(name);
        embeddings.add(embedding);
        currentPeople++;
    }
    public void addtoDatabase(String name, String imagePath, MobileFaceNet fr, YuNet model) {
        Mat image = Imgcodecs.imread(imagePath);
        Mat faces = model.detect(image);
        List<Rect> faceList = model.detectedFaces(faces);
        for (Rect faceRect : faceList) {
            Mat face = new Mat(image, faceRect);
            float[] embedding = fr.recognizeFace(face);
            addtoDatabase(name, embedding);
        }
    }
    public String recognize(float[] embedding) {
        float maxSimilarity = -1.0f;
        String recognizedName = "Unknown";
        float threshold = 0.5f; 
        for (int i = 0; i < embeddings.size(); i++) {
            float dotProduct = 0.0f;
            float normA = 0.0f;
            float normB = 0.0f;
            for (int j = 0; j < embedding.length; j++) {
                dotProduct += embeddings.get(i)[j] * embedding[j];
                normA += embeddings.get(i)[j] * embeddings.get(i)[j];
                normB += embedding[j] * embedding[j];
            }
            float similarity = dotProduct / (float)(Math.sqrt(normA) * Math.sqrt(normB));
            if (similarity > maxSimilarity) {
                maxSimilarity = similarity;
                if (maxSimilarity > threshold) {
                    recognizedName = names.get(i);
                }
            }
        }
        return recognizedName;
    }
}