package cz.fav.ir;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;


public class Crawler {
	
	private static final String DELIMITERS = " -_~`|#$%^&*() {}[],.'/\\\":;!?\t\n";
	private static final String PATH = "animes";
	
	public static List<String> tokenize (String tokenizable) {
		List<String> tokens = new LinkedList<String>();
		StringTokenizer tokenizer = new StringTokenizer(tokenizable, DELIMITERS);
		while (tokenizer.hasMoreElements()) {
			String token = (String) tokenizer.nextElement();
			tokens.add(token.toLowerCase());
		}
		return tokens;
	}
	
	public static void main (String[] args) {
		listOfFilesInAPath(PATH).stream()
		.forEach(filename -> System.out.println(tokenize(readPath(PATH, filename))));
	}
	
	public static String readPath(String path, String filename) {
		Path file = FileSystems.getDefault().getPath(path, filename);
		try (InputStream in = Files.newInputStream(file);
		    BufferedReader reader =
		      new BufferedReader(new InputStreamReader(in))) {
		    String line = null;
		    StringBuilder builder = new StringBuilder();
		    while ((line = reader.readLine()) != null) {
		    	builder.append(line.concat("\n"));
		    }
		    return builder.toString();
		} catch (IOException x) {
		    System.err.println(x);
		}
		return null;
	}
	
	public static List<String> listOfFilesInAPath(String stringpath) {
		List<String> names = new LinkedList<String>();
		Path path = FileSystems.getDefault().getPath(stringpath, "");
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
		    for (Path file: stream) {
		        names.add(file.getFileName().toString());
		    }
		    return names;
		} catch (IOException | DirectoryIteratorException x) {
		    // IOException can never be thrown by the iteration.
		    // In this snippet, it can only be thrown by newDirectoryStream.
		    System.err.println(x);
		}
		return null;
	}
	
}
