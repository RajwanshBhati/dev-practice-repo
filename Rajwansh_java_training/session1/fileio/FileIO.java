package session1.fileio;

import java.io.*;

public class FileIODemo {
    public static void main(String[] args) {
        String filename = "sample.txt";

        // I Create sample file if it doesn't exist
        createSampleFile(filename);

        System.out.println("Reading file: " + filename);
  
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {  // try-with-resources to automatically close the BufferedReader
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private static void createSampleFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            // I used FileWriter to write some sample lines to the file. If the file already exists, it will not be overwritten.
            try (FileWriter fw = new FileWriter(file)) {
                fw.write("Hello from file I/O demo!\n");
                fw.write("Line 2: Java is fun.\n");
                fw.write("Line 3: File reading successful.");
                System.out.println("Sample file created: " + filename);
            } catch (IOException e) {
                System.out.println("Could not create sample file");
            }
        }
    }
}