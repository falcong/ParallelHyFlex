package parallelhyflex.parsing;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 *
 * @author kommusoft
 */
public class TokenParser<StreamType> {
    
    private final TreeSet<Token<? extends StreamType>> tokens = new TreeSet<>(TokenPriorityComparator.getInstance());
    private static final Pattern word = Pattern.compile("[^ \n\t]+");
    
    public void addToken (Token<? extends StreamType> token) {
        this.tokens.add(token);
    }
    
    public void addToken (Token<? extends StreamType>... token) {
        for(Token<? extends StreamType> tok : token) {
            this.addToken(tok);
        }
    }
    public void addToken (Iterable<Token<? extends StreamType>> token) {
        for(Token<? extends StreamType> tok : token) {
            this.addToken(tok);
        }
    }
    
    public Iterable<StreamType> getIterable (InputStream stream) {
        return new TokenParserIterable(stream);
    }
    
    private class TokenParserIterable implements Iterable<StreamType> {
        
        private final InputStream stream;
        
        public TokenParserIterable (InputStream stream) {
            this.stream = stream;
        }

        @Override
        public Iterator<StreamType> iterator() {
            return new TokenParserIterator(this.stream);
        }
        
    }
    
    private class TokenParserIterator implements Iterator<StreamType> {
        
        private final Scanner scanner;
        
        public TokenParserIterator (InputStream stream) {
            this.scanner = new Scanner(stream);
        }

        @Override
        public boolean hasNext() {
            return this.scanner.hasNext(word);
        }

        @Override
        public StreamType next() {
            String tok = this.scanner.next(word);
            for(Token<? extends StreamType> token : tokens) {
                if(token.validate(tok)) {
                    return token.generate(tok);
                }
            }
            return null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported by the TokenParserIterator.");
        }
        
    }
    
}