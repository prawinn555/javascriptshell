package javascriptshell;

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ResourceUtil {

	static Map<String,FileSystem> fsMap = new HashMap<>();
	
	public static Path toPath(String file) throws Exception {
		URI uri = ClassLoader.getSystemResource(file).toURI();
		System.out.println("uri="+uri);
		if(!uri.toString().contains("!")) {
			return Paths.get(uri);
		} else {
			final String[] array = uri.toString().split("!");
			
		    FileSystem fs = fsMap.get(array[0]);
			if(fs==null) {
				fs = FileSystems.newFileSystem(URI.create(array[0]), 
					new HashMap<>());
				fsMap.put(array[0], fs);
			}
			return fs.getPath(array[1]);
		}
	}
}
