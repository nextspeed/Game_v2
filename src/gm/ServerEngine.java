package gm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

public class ServerEngine implements Messagable{
    public static final String ID_PREFIX = "ID";
    public static final int PR_PERIOD = 1000;  //ms
    public static final int ITEM_PERIOD = 2000; //ms
    public static final int MAX_LOCX = 99;
    public static final int MAX_LOCY = 99;
    
    int currentId = 0;
    
    private static ServerEngine engine;
    public static ServerEngine getInstance(){
        if(engine == null){
            engine = new ServerEngine();
        }
        return engine;
    }
    
    HashMap<String, ClientHandler> handlers = new HashMap<String, ClientHandler>();
    HashMap<String, Player> players = new HashMap<String, Player>();
    List<Item> currentItems = new ArrayList<Item>();
    
    ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    ItemGenerator itemGenerator = new SimpleItemGenerator();
    private ServerEngine(){
        
        service.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                sendPlayerReportToAllPlayers();
            }
        }, 1000, PR_PERIOD, TimeUnit.MILLISECONDS);
        
        service.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                generateItems();
            }
        }, 3000, ITEM_PERIOD, TimeUnit.MILLISECONDS);
    }
    
    public void sendPlayerReportToAllPlayers(){
        for(String sourceId : players.keySet()){
            for(String _sourceId : players.keySet())
            sendPlayerReport(sourceId, players.get(_sourceId));
        }
    }
    
    public void generateItems(){
        Item items[] = itemGenerator.generateItem();
        
        for(Item item: items){
            currentItems.add(item);
            GameMessage m = item.createItemMessage();
            for(String sourceId : players.keySet()){
                handlers.get(sourceId).outgoing(m.toString());
            }
        }
    }
    
    public void pickupItem(Player p, Item item){
        currentItems.remove(item);
        p.obtain(item);
    }
    
    public Item checkPlayerItem(Player p){
        for(Item item : currentItems){
            if(p.locx == item.locx && p.locy == item.locy){
               return item; 
            }
        }
        return null;
    }
    
    public void register(ClientHandler handler){
        String givenId = ID_PREFIX + (++currentId);
        Player player = new Player();
        players.put(givenId, player);
        
        handler.setMessagable(this, givenId);
        GameMessage message = new GameMessage("HELO");
        message.addParam("id", givenId);
        message.addParam("locx", String.valueOf(player.locx));
        message.addParam("locy", String.valueOf(player.locy));
        handler.outgoing(message.toString());
        handlers.put(givenId, handler);   
        
    }
    
    public void unregister(String sourceId){
        handlers.remove(sourceId);
    }

    @Override
    public void incoming(String line, String sourceId) {
        System.out.println(line);
        GameMessage m = GameMessage.parse(line);
        Player player = players.get(sourceId);
        if("MV".equals(m.command)){
            player.move(m.i("dirx"), m.i("diry"));
            Item item = checkPlayerItem(player);
            if(item != null){
                pickupItem(player, item);
            }
            sendPlayerReport(sourceId, player);
        }else if("USE".equals(m.command)){
            player.useKeptItem(m.s("id"));
        }        
    }
    
    public void sendPlayerReport(String sourceId, Player player){
        GameMessage m = player.createPrMessage();
        handlers.get(sourceId).outgoing(m.toString());
        System.out.println("sending " + m.toString());
    }
    
    
}
    
