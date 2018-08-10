package rokas.armandas.quizboardgame;

public class Player {
    private int points;
    private String name;

    public Player(){
        this.points = 0;
    }

    public int getPoints(){
        return points;
    }

    public void addPoint(){
        points++;
    }


}
