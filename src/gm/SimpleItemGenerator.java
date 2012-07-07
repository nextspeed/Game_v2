package gm;

import java.util.Random;

public class SimpleItemGenerator implements ItemGenerator{
    Random random = new Random(System.currentTimeMillis());
    
    Class[] avaiableItemTypes = { ScoreItem.class };
    
    @Override
    public Item[] generateItem() {
        int index = random.nextInt(avaiableItemTypes.length + 1) - 1;
        if(index >= 0){
            Class target = avaiableItemTypes[index];
            try{
                Item item = (Item) target.newInstance();
                return new Item[] { item };
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return new Item[0];
    }
}
