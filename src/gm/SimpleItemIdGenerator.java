package gm;

public class SimpleItemIdGenerator implements ItemIdGenerator{
    int id = 0;
    
    @Override
    public String nextId() {
        return String.valueOf(++id);
    }
    
}
