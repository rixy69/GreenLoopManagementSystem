package services;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public final class ResourceHelper {
    private ResourceHelper() {
    }

    public static Image loadImage(Class<?> contextClass, String resourcePath, String... relativePaths) {
        try {
            URL url = contextClass.getResource(resourcePath);
            if (url != null) {
                BufferedImage image = ImageIO.read(url);
                if (image != null) {
                    return image;
                }
            }
        } catch (Exception ignored) {
        }

        for (String relativePath : relativePaths) {
            Image image = loadFromFile(resolveProjectFile(contextClass, relativePath));
            if (image != null) {
                return image;
            }
        }
        return null;
    }

    public static File resolveProjectFile(Class<?> contextClass, String relativePath) {
        File currentDirCandidate = new File(System.getProperty("user.dir"), relativePath);
        if (currentDirCandidate.exists()) {
            return currentDirCandidate;
        }

        File userDir = new File(System.getProperty("user.dir"));
        File parentCandidate = new File(userDir.getParentFile() == null ? userDir : userDir.getParentFile(), relativePath);
        if (parentCandidate.exists()) {
            return parentCandidate;
        }

        try {
            URL codeSourceUrl = contextClass.getProtectionDomain().getCodeSource().getLocation();
            File base = new File(codeSourceUrl.toURI());
            if (base.isFile()) {
                base = base.getParentFile();
            }

            File codeSourceCandidate = new File(base, relativePath);
            if (codeSourceCandidate.exists()) {
                return codeSourceCandidate;
            }

            File codeSourceParentCandidate = new File(base.getParentFile() == null ? base : base.getParentFile(), relativePath);
            if (codeSourceParentCandidate.exists()) {
                return codeSourceParentCandidate;
            }
        } catch (URISyntaxException ignored) {
        }

        return currentDirCandidate;
    }

    private static Image loadFromFile(File file) {
        try {
            if (file != null && file.exists()) {
                BufferedImage image = ImageIO.read(file);
                if (image != null) {
                    return image;
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
