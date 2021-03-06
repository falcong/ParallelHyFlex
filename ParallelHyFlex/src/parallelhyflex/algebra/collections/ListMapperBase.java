package parallelhyflex.algebra.collections;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author kommusoft
 * @param <TKey>
 * @param <TItem>
 */
public class ListMapperBase<TKey, TItem> implements ListMapper<TKey, TItem> {

    private final HashMap<TKey, MultiThreadedList<TItem>> map = new HashMap<>();

    /**
     *
     * @param key
     * @param item
     * @return
     */
    @Override
    public TItem put(TKey key, TItem item) {
        MultiThreadedList<TItem> list;
        if (this.map.containsKey(key)) {
            list = this.map.get(key);
        } else {
            list = new MultiThreadedList<>();
            this.map.put(key, list);
        }
        list.add(item);
        return null;
    }

    /**
     *
     * @param key
     * @return
     */
    @Override
    public Iterator<TItem> iterator(TKey key) {
        if (this.map.containsKey(key)) {
            return this.map.get(key).iterator();
        } else {
            return null;
        }
    }

    /**
     *
     * @return
     */
    @Override
    public int size() {
        int siz = 0;
        for (MultiThreadedList<TItem> l : this.map.values()) {
            siz += l.size();
        }
        return siz;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isEmpty() {
        for (MultiThreadedList<TItem> l : this.map.values()) {
            if (l.size() > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean containsKey(Object o) {
        return this.map.containsKey(o);
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean containsValue(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public TItem get(Object o) {
        if (this.map.containsKey(o)) {
            MultiThreadedList<TItem> item = this.map.get(o);
            if (!item.isEmpty()) {
                return item.get(0);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public TItem remove(Object o) {
        MultiThreadedList<TItem> item = this.map.remove(o);
        if (item != null && !item.isEmpty()) {
            return item.get(0);
        } else {
            return null;
        }
    }

    /**
     *
     * @param map
     */
    @Override
    public void putAll(Map<? extends TKey, ? extends TItem> map) {
        for (Entry<? extends TKey, ? extends TItem> item : map.entrySet()) {
            this.put(item.getKey(), item.getValue());
        }
    }

    /**
     *
     */
    @Override
    public void clear() {
        this.map.clear();
    }

    /**
     *
     * @return
     */
    @Override
    public Set<TKey> keySet() {
        return this.map.keySet();
    }

    /**
     *
     * @return
     */
    @Override
    public Collection<TItem> values() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @return
     */
    @Override
    public Set<Entry<TKey, TItem>> entrySet() {
        return new EntrySet();
    }

    /**
     *
     * @return
     */
    @Override
    public Iterator<Entry<TKey, TItem>> iterator() {
        return new EntryIterator(map.entrySet().iterator());
    }

    private class EntryIterator implements Iterator<Entry<TKey, TItem>> {

        private final Iterator<Entry<TKey, MultiThreadedList<TItem>>> realIterator;
        private TKey subKey;
        private Iterator<TItem> subIterator;

        EntryIterator(Iterator<Entry<TKey, MultiThreadedList<TItem>>> realIterator) {
            this.realIterator = realIterator;
        }

        @Override
        public boolean hasNext() {
            if (subIterator == null) {
                return loadNext();
            } else if (!subIterator.hasNext()) {
                return loadNext();
            } else {
                return true;
            }
        }

        @Override
        public Entry<TKey, TItem> next() {
            hasNext();
            return new AbstractMap.SimpleEntry<>(subKey, subIterator.next());
        }

        @Override
        public void remove() {
            if (this.subIterator != null) {
                this.subIterator.remove();
            }
        }

        private boolean loadNext() {
            while ((subIterator == null || !subIterator.hasNext()) && realIterator.hasNext()) {
                Entry<TKey, MultiThreadedList<TItem>> e = realIterator.next();
                subKey = e.getKey();
                subIterator = e.getValue().iterator();
            }
            return (subIterator != null && subIterator.hasNext());
        }
    }

    private class EntrySet implements Set<Entry<TKey, TItem>> {

        @Override
        public int size() {
            return ListMapperBase.this.size();
        }

        @Override
        public boolean isEmpty() {
            return ListMapperBase.this.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            if (o instanceof Entry) {
                Entry e = (Entry) o;
                Object k = e.getKey();
                if (map.containsKey(k)) {
                    return map.get(k).contains(e.getValue());
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        @Override
        public Iterator<Entry<TKey, TItem>> iterator() {
            return ListMapperBase.this.iterator();
        }

        @Override
        public Object[] toArray() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public <T> T[] toArray(T[] ts) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean add(Entry<TKey, TItem> e) {
            ListMapperBase.this.put(e.getKey(), e.getValue());
            return true;
        }

        @Override
        public boolean remove(Object o) {
            if (o instanceof Entry) {
                Entry e = (Entry) o;
                Object k = e.getKey();
                if (map.containsKey(k)) {
                    return map.get(k).remove(e.getValue());
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        @Override
        public boolean containsAll(Collection<?> clctn) {
            for (Object obj : clctn) {
                if (!this.contains(obj)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends Entry<TKey, TItem>> clctn) {
            for (Entry<TKey, TItem> e : clctn) {
                this.add(e);
            }
            return true;
        }

        @Override
        public boolean retainAll(Collection<?> clctn) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean removeAll(Collection<?> clctn) {
            boolean rm = false;
            for (Object e : clctn) {
                rm |= this.remove(e);
            }
            return rm;
        }

        @Override
        public void clear() {
            ListMapperBase.this.clear();
        }
    }
}