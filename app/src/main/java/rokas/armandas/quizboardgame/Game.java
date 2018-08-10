package rokas.armandas.quizboardgame;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class Game {
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private Player player5;
    private ArrayList<QuestionsDO> questions;
    private int whichPlayerHasTurn;
    private int numberOfPlayer;
    private String currentQuestionID;




    public Game (int numberOfPlayers, ArrayList<QuestionsDO> questions){
        this.numberOfPlayer = numberOfPlayers;
        this.whichPlayerHasTurn = 1;
        player1 = new Player();
        player2 = new Player();
        player3 = new Player();
        player4 = new Player();
        player5 = new Player();
        this.questions = questions;
    }

    public String getCurrentQuestionID(){
        return currentQuestionID;
    }

    public int getWhichPlayerHasTurn(){
        return whichPlayerHasTurn;
    }

    public int getNumberOfPlayer(){
        return numberOfPlayer;
    }

    public int getPlayerPoint(int playerNumber){
        switch(playerNumber){
            case 1:
                return player1.getPoints();
            case 2:
                return player2.getPoints();
            case 3:
                return player3.getPoints();
            case 4:
                return player4.getPoints();
            case 5:
                return player5.getPoints();
        }
        return -1;
    }

    public String getQuestion(ArrayList<String> listOfUsedQuestionsID){
        // currentIndexListOfQuestion is used into while loop. In order to be sure that all indexes is checked every time.
        // So it is removing one by one and check if it is in Cache memory.
        // one disadventage of this algorithm that it spams WHILE loop, since it loops until it gets the right random number.
        // It could take some time if is huge list and it has to find a last item by pushing random numbers.
        // Ant it has to remove all indexes every time in currentIndexListOfQuestion before it can return null.
        LinkedList<String> currentIndexListOfQuestion = new LinkedList<>();
        for (int i = 0; i < questions.size(); i++){
            currentIndexListOfQuestion.add(questions.get(i).getQuestionId());
        }

        while(!currentIndexListOfQuestion.isEmpty()){
            int index = ThreadLocalRandom.current().nextInt(questions.size());
            QuestionsDO currentQuestion = questions.get(index);
            currentQuestionID = currentQuestion.getQuestionId();

            if (!listOfUsedQuestionsID.contains(currentQuestionID)){
               currentQuestionID = currentQuestion.getQuestionId();
               String question = currentQuestion.getContent();
               return question;
            } else {
               currentIndexListOfQuestion.remove(currentQuestionID);
            }
        }
        return null;
    }

    public void nextTurn(){
        switch(numberOfPlayer){
            case 2:
                if(whichPlayerHasTurn < 2){
                    whichPlayerHasTurn++;
                } else whichPlayerHasTurn = 1;
                break;
            case 3:
                if(whichPlayerHasTurn < 3){
                    whichPlayerHasTurn++;
                } else whichPlayerHasTurn = 1;
                break;
            case 4:
                if(whichPlayerHasTurn < 4){
                    whichPlayerHasTurn++;
                } else whichPlayerHasTurn = 1;
                break;
            case 5:
                if(whichPlayerHasTurn < 5){
                    whichPlayerHasTurn++;
                } else whichPlayerHasTurn = 1;
                break;
            default:
                break;
        }
    }

    public void givePoint(){
        switch(whichPlayerHasTurn){
            case 1:
                player1.addPoint();
                break;
            case 2:
                player2.addPoint();
                break;
            case 3:
                player3.addPoint();
                break;
            case 4:
                player4.addPoint();
                break;
            case 5:
                player5.addPoint();
                break;
            default:
                break;
        }
    }
}
