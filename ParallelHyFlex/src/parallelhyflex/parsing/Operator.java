package parallelhyflex.parsing;

/**
 *
 * @author kommusoft
 */
public interface Operator<T, TL extends Token, TR extends Token> extends Token<T> {

    void setLeft(TL left) throws ParsingException;

    void setRight(TR right) throws ParsingException;

    TL getLeft();

    TR getRight();

    boolean canSetLeft(TL token);

    boolean canSetRight(TR token);

    void process();
}
