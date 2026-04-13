package session1.fileio;

import java.io.*;

public class FileIO {
    public static void main(String[] args) {
        String filename = "sample.txt";

        // I Create sample file if it doesn't exist and write some lines to it using FileWriter. Then I read the file using BufferedReader and print its contents to the console. I also handle potential exceptions that may occur during file creation and reading, such as FileNotFoundException and IOException, to ensure that the program runs smoothly even if there are issues with file access.
        createSampleFile(filename);

        System.out.println("Reading file: " + filename);
        
        //I used BufferedReader to read the contents of the file line by line. 
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
 
    //I created a helper method createSampleFile that checks if the specified file exists. If it doesn't, it creates the file and writes some sample lines to it using FileWriter. This method is called at the beginning of the main method to ensure that there is a file to read from when I attempt to read its contents later on.
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