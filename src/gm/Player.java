package gm;

import static gm.ServerEngine.*;
public class Player {
    int locx;
    int locy;
    int score = 0;
    Item keptItem = null;
    
    public Player(){
        locx = (int)(Math.random() * (MAX_LOCX + 1));
        locy = (int)(Math.random() * (MAX_LOCY + 1));
    }
    
    public void move(int dirx, int diry){
        locx += dirx;
        locy += diry;
    }
    
    public void obtain(Item item){
        if(item.keepable){
            if(keptItem != null){
                keptItem.use(this);
            }
            keptItem = item;
        }else{
            item.use(this);
        }
    }
    
    public void useKeptItem(String itemId){
        if(keptItem != null && keptItem.id.equals(itemId)){
            keptItem.use(this);
        }
    }
    
    public GameMessage createPrMessage(){
        GameMessage m = new GameMessage("PR");
        m.addParam("locx", String.valueOf(locx));
        m.addParam("locy", String.valueOf(locy));
        m.addParam("score", String.valueOf(score));
        return m;
    }
}
