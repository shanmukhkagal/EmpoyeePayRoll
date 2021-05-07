package com.payroll;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.IntStream;

public class FileApiTest {
    private String Home = System.getProperty("user.home");
    private String testDirectory = "TestDirectory";

    @Test
    void checkDirectoryAlreadyPresent() throws IOException {
        Path path = Paths.get(Home);
        Assert.assertTrue(Files.exists(path));

        /**
         * if the directory already exists then delete method
         */
        Path newDirectoryPath = Paths.get(testDirectory);
        if(Files.exists(newDirectoryPath)) FilesUtility.deleteFiles(newDirectoryPath.toFile());
        Assert.assertTrue(Files.notExists(newDirectoryPath));

        /**
         * to create new directory
         */
        Files.createDirectory(newDirectoryPath);
        Assert.assertTrue(Files.exists(newDirectoryPath));

        /**
         * for fixed loop , used IntStream
         */
        IntStream.range(1, 10).forEach(fileNumber -> {
            String filePath = testDirectory + "/" + "TempFile" + fileNumber;
            Path tempPath = Paths.get(filePath);
            try {
                Files.createFile(tempPath);
            } catch (IOException e) {
            }
            Assert.assertTrue(Files.exists(tempPath));
        });

        /**
         * Implementing Directory
         * conversion of path object to file object
         */
        Path p = Paths.get(testDirectory);
        File file = p.toFile();
        File[] fileList = file.listFiles();
        for (File f : fileList) {
            System.out.println(f);
        }

        Files.list(Path.of(testDirectory)).filter(Files::isRegularFile).forEach(System.out::println);
        Files.newDirectoryStream(Path.of(testDirectory)).forEach(System.out::println);
        Files.newDirectoryStream(Path.of(testDirectory), pathOne -> pathOne.toFile().isFile() && pathOne.toString().startsWith("Temp")).forEach(System.out::println);
    }
}
