package stephenx.xkcd;

/**
 * A simple tuple type for Java.
 * 
 * @author Stephen Xie &lt;***@andrew.cmu.edu&gt;
 */
public class Tuple<X, Y> {
    
    public final X x;
    public final Y y;
    
    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }
    
}
