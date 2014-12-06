import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Project03 {

    enum Type {
        WORD, PDF, JPEG, UNKNOWN
    }

    private List<File> files = new ArrayList<File>();
    private Map<Type, List<String>> typeToFileMap = new HashMap<Type, List<String>>();

    public static void main(String[] args) {
        Project03 project = new Project03();

        project.buildFileList(args[0]);
        project.classifyFiles();

        for (Type t : project.typeToFileMap.keySet()) {
            List<String> subType = project.typeToFileMap.get(t);
            System.out.println(t + " (" + subType.size() + ") " + subType);
        }
    }

    /**
     * Build the list of files in the directory
     *
     * @param directory
     */
    private void buildFileList(String directory) {
        File f = new File(directory);

        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                files.add(file);
            }
        } else if (f.exists()) {
            files.add(f);
        }
    }

    private void classifyFiles() {
        for (File f : files) {
            try {
                byte[] contents = readFile(f);

                if (contents.length != 512) {
                    System.out.println("Contents Length: " + contents.length);
                }

                System.out.print(f.getName() + " ");
                for (int i = 0; i < 3; ++i) {
                    System.out.printf("0x%02x ", contents[i]);
                }
                System.out.println();

                if (isText(contents)) {
                    addToMap(Type.WORD, f);
                } else if (isFullBinary(contents)) {
                    addToMap(Type.JPEG, f);
                } else {
                    addToMap(Type.UNKNOWN, f);
                }
            } catch (IOException e) {
                // addToMap(Type.UNKNOWN, f);
                e.printStackTrace();
            }
        }
    }

    private boolean isText(byte[] contents) {
        int numAscii = 0;

        for (byte b : contents) {
            int value = b;
            if (value > 31 && value < 126) {
                numAscii++;
            }
        }

        return numAscii > (.75 * contents.length);
    }

    private boolean isFullBinary(byte[] contents) {
        int numAscii = 0;

        for (byte b : contents) {
            int value = b;

            if (value > 31 && value < 126) {
                numAscii++;
            }
        }

        return numAscii == 0;
    }

    public static byte[] readFile(File file) throws IOException {
        RandomAccessFile f = new RandomAccessFile(file, "r");
        try {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength) {
                throw new IOException("File size >= 2 GB");
            }
            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        } finally {
            f.close();
        }
    }

    private void addToMap(Type t, File f) {
        if (typeToFileMap.containsKey(t)) {
            typeToFileMap.get(t).add(f.getName());
        } else {
            List<String> tempList = new ArrayList<String>();
            tempList.add(f.getName());
            typeToFileMap.put(t, tempList);
        }
    }
}
