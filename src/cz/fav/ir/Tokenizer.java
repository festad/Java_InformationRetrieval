package cz.fav.ir;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class Tokenizer {
	
	private static final String DELIMITERS = " ,.'/\\\":;!?\t\n";
	
	public static List<String> tokenize (String tokenizable) {
		List<String> tokens = new LinkedList<String>();
		StringTokenizer tokenizer = new StringTokenizer(tokenizable, DELIMITERS);
		while (tokenizer.hasMoreElements()) {
			String token = (String) tokenizer.nextElement();
			tokens.add(token);
			System.out.print(token);
		}
		return tokens;
	}
	
	public static void main(String[] args) {
		tokenize("Neon genesis! \n"
				+ "evangelion");
	}
	
}
