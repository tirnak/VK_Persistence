package tirnak.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageSaver {

    private Path dir;

    public ImageSaver(String dirString) {
        Path dir = Paths.get(dirString);
        if (!Files.isDirectory(dir)) {
            throw new RuntimeException("path given is not a directory"); }
        if (!Files.isWritable(dir)) {
            throw new RuntimeException("dir given is not writable"); }
        this.dir = dir;
    }

    public void save(String pathToUrl, String name) {
        try(InputStream in = new URL(pathToUrl).openStream()){
            Files.copy(in, dir.resolve(name));
        }  catch (IOException  e) {
            e.printStackTrace();
        }
    }
}
