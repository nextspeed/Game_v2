package gm;

public class ScoreItem extends Item{
    int score = 0;
    public ScoreItem(){
        score = (int)(Math.random() * 3) + 1;
    }

    @Override
    public void use(Player owner) {
        owner.score += score;
    }
    
    @Override
    public GameMessage createItemMessage(){
        GameMessage m = super.createItemMessage();
        m.addParam("score", String.valueOf(score));
        return m;        
    }
}
