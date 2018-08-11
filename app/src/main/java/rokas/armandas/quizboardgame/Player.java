package rokas.armandas.quizboardgame;

public class Player {
    private int points;
    private String name;

    public Player(String name){
        this.points = 0;
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public int getPoints(){
        return points;
    }

    public void addPoint(){
        points++;
    }


}
