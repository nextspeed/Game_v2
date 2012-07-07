package gm;

import static gm.ServerEngine.*;

public abstract class Item {
    static ItemIdGenerator idGenerator = new SimpleItemIdGenerator();
    String id;
    int locx;
    int locy;
    boolean keepable = false;
        
    public Item(){
        id = idGenerator.nextId();
        locx = (int)(Math.random() * (MAX_LOCX + 1));
        locy = (int)(Math.random() * (MAX_LOCY + 1));
    }
    
    
    public abstract void use(Player owner);
    public GameMessage createItemMessage(){
        GameMessage m = new GameMessage("ITM");
        m.addParam("id", id);
        m.addParam("locx", String.valueOf(locx));
        m.addParam("locy", String.valueOf(locy));
        return m;
    }
}
