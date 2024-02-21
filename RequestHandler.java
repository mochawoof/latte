//latte RequestHandler v1.0
import com.sun.net.httpserver.*;
import java.io.*;
import java.util.Date;
import java.nio.file.Path;
import java.util.HashMap;
public class RequestHandler implements HttpHandler {
    private HashMap<String, String> contentTypes = new HashMap<String, String>();
    public RequestHandler() {
        String[] rawContentTypes = Helper.getResourceAsStringArray("content-types.csv");
        for (String type : rawContentTypes) {
            String[] row = type.trim().split(",");
            contentTypes.put(row[1], row[0]);
        }
    }
    public void handle(HttpExchange exchange) throws IOException {
        String uri = exchange.getRequestURI().normalize().getPath();
        System.out.println(new Date().toString() + ": " + uri);
        
        long responseLength = 0;
        int responseCode = 200;
        String contentType = "text/plain";
        OutputStream out = exchange.getResponseBody();
        File requested = new File(Main.path + uri);
        if (requested.exists() && !requested.isDirectory() && requested.canRead()) {
            Path path = requested.toPath();
            String pathString = path.toString();
            if (pathString.lastIndexOf(".") > -1) { //make sure there is an extension before lookup
                String ext = pathString.substring(pathString.lastIndexOf("."), pathString.length());
                if (contentTypes.containsKey(ext)) {
                    contentType = contentTypes.get(ext);
                }
            }
            
            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.sendResponseHeaders(responseCode, requested.length());
            BufferedReader reader = new BufferedReader(new FileReader(requested));
            int read;
            while ((read = reader.read()) != -1) {
                out.write(read);
            }
            //unstable custom reader below
            /*int length = 1000000;
            int read;
            int[] thisRead = new int[length];
            int progress = 0;
            while ((read = reader.read()) != -1) {
                thisRead[progress] = read;
                progress++;
                if (progress == length) {
                    for (int i=0; i < thisRead.length; i++) {
                        out.write(thisRead[i]);
                    }
                    thisRead = new int[length];
                    progress = 0;
                }
            }
            if (progress < length) {
                for (int i=0; i < thisRead.length; i++) {
                        out.write(thisRead[i]);
                }
            }*/
            
        } else if (requested.isDirectory() && uri.toString().endsWith("/") && requested.canRead()) {
            String listing = "<h2>" + requested.getAbsolutePath() + "</h2><ul>";
            listing += "<li><a href=\"../\">../</a></li>";
            File[] dir = requested.listFiles();
            for (File entry : dir) {
                String append = "";
                if (entry.isDirectory()) { //to ensure links work right!
                    append = "/";
                }
                listing += "<li><a href=\"" + entry.getName() + append + "\">" + entry.getName() + append + "</a></li>";
            }
            listing += "</ul>";
            
            exchange.getResponseHeaders().set("Content-Type", "text/html");
            exchange.sendResponseHeaders(responseCode, listing.length());
            out.write(listing.getBytes());
        } else {
            String msg = "File not found!";
            
            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.sendResponseHeaders(responseCode, msg.length());
            out.write(msg.getBytes());
            responseCode = 404;
        }
        
        out.flush();
        out.close();
    }
}
