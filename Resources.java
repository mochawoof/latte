import java.net.URL;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.io.InputStreamReader;
class Resources {
    public static URL get(String path) {
        return Resources.class.getResource(path);
    }
    public static ImageIcon getAsImageIcon(String path) {
        return new ImageIcon(get(path));
    }
    public static Image getAsImage(String path) {
        return getAsImageIcon(path).getImage();
    }
    public static String getAsString(String path) {
        try {
            InputStreamReader reader = new InputStreamReader(get(path).openStream());
            int r;
            String s = "";
            while ((r = reader.read()) != -1) {
                s += (char) r;
            }
            return s;
        } catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
}