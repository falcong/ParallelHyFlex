package parallelhyflex.communication.serialisation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author kommusoft
 */
public final class SerialisationUtils {

    public static void writeLongArray(DataOutputStream dos, long... array) throws IOException {
        dos.writeInt(array.length);
        for (long v : array) {
            dos.writeLong(v);
        }
    }

    public static void writeIntArray(DataOutputStream dos, int... array) throws IOException {
        dos.writeInt(array.length);
        for (int v : array) {
            dos.writeInt(v);
        }
    }

    public static void writeDoubleArray(DataOutputStream dos, double... array) throws IOException {
        dos.writeInt(array.length);
        for (double v : array) {
            dos.writeDouble(v);
        }
    }

    public static void writeIntArray2d(DataOutputStream dos, int[][] array) throws IOException {
        dos.writeInt(array.length);
        for (int[] v : array) {
            writeIntArray(dos, v);
        }
    }

    public static void writeDoubleArray2d(DataOutputStream dos, double[][] array) throws IOException {
        dos.writeInt(array.length);
        for (double[] v : array) {
            writeDoubleArray(dos, v);
        }
    }

    public static long[] readLongArray(DataInputStream dis) throws IOException {
        long[] res = new long[dis.readInt()];
        for (int i = 0; i < res.length; i++) {
            res[i] = dis.readLong();
        }
        return res;
    }

    public static int[] readIntArray(DataInputStream dis) throws IOException {
        int[] res = new int[dis.readInt()];
        for (int i = 0; i < res.length; i++) {
            res[i] = dis.readInt();
        }
        return res;
    }

    public static int[] readIntArray(DataInputStream dis, int[] values) throws IOException {
        int n = dis.readInt();
        int k = Math.min(n, values.length);
        for (int i = 0; i < k; i++) {
            values[i] = dis.readInt();
        }
        for (int i = k; i < n; i++) {
            dis.readInt();
        }
        return values;
    }

    public static double[] readDoubleArray(DataInputStream dis) throws IOException {
        double[] res = new double[dis.readInt()];
        for (int i = 0; i < res.length; i++) {
            res[i] = dis.readDouble();
        }
        return res;
    }

    public static int[][] readIntArray2d(DataInputStream dis) throws IOException {
        int[][] res = new int[dis.readInt()][];
        for (int i = 0; i < res.length; i++) {
            res[i] = readIntArray(dis);
        }
        return res;
    }

    public static double[] readDoubleArray(DataInputStream dis, double[] values) throws IOException {
        int n = dis.readInt();
        int k = Math.min(n, values.length);
        for (int i = 0; i < k; i++) {
            values[i] = dis.readDouble();
        }
        for (int i = k; i < n; i++) {
            dis.readDouble();
        }
        return values;
    }

    public static double[][] readDoubleArray2d(DataInputStream dis) throws IOException {
        double[][] res = new double[dis.readInt()][];
        for (int i = 0; i < res.length; i++) {
            res[i] = readDoubleArray(dis);
        }
        return res;
    }

    private SerialisationUtils() {
    }
}
