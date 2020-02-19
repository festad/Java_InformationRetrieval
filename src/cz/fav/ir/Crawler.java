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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;


public class Crawler {
	
	private static final String DELIMITERS = " -_~`|#$%^&*() {}[],.'/\"\\:;!?\t\n\u201D\u201C";
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
	
	public static List<String> listOfFilesInAPath (String stringpath) {
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
	
	public static Map<String, Set<String>> indexed_dictionary (String stringpath) {
		Map<String, Set<String>> indexed_dictionary = new ConcurrentSkipListMap<String, Set<String>>();
		List<String> filenames = listOfFilesInAPath(stringpath);
		filenames.stream().forEach(filename -> {
			new Thread(() -> {
				String filename_content = readPath(stringpath, filename);
				StringTokenizer tokenizer = new StringTokenizer(filename_content, DELIMITERS);
				while (tokenizer.hasMoreElements()) {
					String token = (String) tokenizer.nextElement().toString().toLowerCase();
					if (indexed_dictionary.containsKey(token))
						indexed_dictionary.get(token).add(filename);
					else {
						indexed_dictionary.put(token, new HashSet<String>());
						indexed_dictionary.get(token).add(filename);
					}
				}
			}).start();
		});
		return indexed_dictionary;
	}
	
	public static String printMap (Map<String, Set<String>> indexed_dictionary) {
		StringBuilder builder = new StringBuilder();
		indexed_dictionary.keySet()
		.stream()
		.forEach(key -> {
			builder.append("\n" + key + " -> ");
			indexed_dictionary.get(key)
			.stream()
			.forEach(filename -> {
				builder.append("\n\t" + filename);
			});
		});
		return builder.toString();
	}
	
	public static void main (String[] args) {
		System.out.println(printMap(indexed_dictionary(PATH)));
	}
}
