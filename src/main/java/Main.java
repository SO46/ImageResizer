import javax.print.attribute.standard.MediaSize;
import java.io.File;

public class Main {

    public static final int NEW_WIDTH = 300;
    public static final String SRC_FOLDER = "data/src/";
    public static final String DST_FOLDER = "data/dst/";

    public static void main(String[] args) {

        File srcDir = new File(SRC_FOLDER);

        long start = System.currentTimeMillis();

        File[] files = srcDir.listFiles();

        int coresCount = Runtime.getRuntime().availableProcessors();

        int partLength = (files.length > coresCount) ? files.length / coresCount : 1;
        int remains = files.length % coresCount;
        boolean flag = true;
        int first = 0;
        for (int i = 1; i <= coresCount; i++) {
            if (i > files.length){
                break; // if files count less than cores count
            }
            if ((coresCount - remains < i ) && flag) {
                flag = false;
                partLength++;
            }
            File[] filesCopy = new File[partLength];
            System.arraycopy(files, first, filesCopy, 0, filesCopy.length);
            new Thread(new ImageResizer(filesCopy, NEW_WIDTH, DST_FOLDER, start)).start();
            first += partLength;
        }
    }

}
