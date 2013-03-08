/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parallelhyflex.utils;

import java.util.Random;

/**
 *
 * @author kommusoft
 */
public class Utils {
    
    private Utils () {}
    
    public static final Random StaticRandom = new Random();
    
    public static String stringReverse (String inp) {
        StringBuilder sb = new StringBuilder();
        for(int i = inp.length()-1; i >= 0; i--) {
            sb.append(inp.charAt(i));
        }
        return sb.toString();
    }
    
}
