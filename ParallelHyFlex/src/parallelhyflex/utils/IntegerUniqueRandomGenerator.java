package parallelhyflex.utils;

public class IntegerUniqueRandomGenerator implements UniqueRandomGenerator<Integer> {

    private int[] values;
    private int ptr = 0x00;

    public IntegerUniqueRandomGenerator(int from, int to) {
        this.values = new int[to - from + 1];
        for (int i = from, j = 0x00; i <= to; i++, j++) {
            this.values[j] = i;
        }
    }

    @Override
    public Integer next() {
        int i = ptr + Utils.nextInt(values.length - ptr);
        int res = values[i];
        values[i] = values[ptr];
        values[ptr++] = res;
        return res;
    }

    @Override
    public void reset() {
        this.ptr = 0;
    }

    @Override
    public boolean hasNext() {
        return this.ptr < values.length;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
