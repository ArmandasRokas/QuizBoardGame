// Create administrator window, to create new questions


package rokas.armandas.quizboardgame;

import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapperConfig;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {

    private TextView lblWelcome;
    private TextView lblChooseNumberPlayer;
    private TextView lblPlayer1Point;
    private TextView lblPlayer2Point;
    private TextView lblPlayer3Point;
    private TextView lblPlayer4Point;
    private TextView lblPlayer5Point;
    private TextView lblQuestion;
    private TextView lblPlayerTurn;
    private Button btn2Player;
    private Button btn3Player;
    private Button btn4Player;
    private Button btn5Player;
    private Button btnGo;
    private Button btnYesAnswer;
    private Button btnNoAnswer;
    private Game game;
    private DynamoDBMapper dynamoDBMapper;
    private ArrayList<QuestionsDO> questions;
    private File cacheFile;


    public void createQuestion(String content, String id) {
        final QuestionsDO newsItem = new QuestionsDO();

        newsItem.setQuestionId(id);
        newsItem.setContent(content);

        new Thread(new Runnable() {
            @Override
            public void run() {
                dynamoDBMapper.save(newsItem);
                // Item saved
            }
        }).start();
        System.out.println("Creating question");
    }


    public void newGame(int numberPlayer){
        switch (numberPlayer){
            case 2:
                game = new Game(2,questions);
                break;
            case 3:
                game = new Game(3,questions);
                break;
            case 4:
                game = new Game(4,questions);
                break;
            case 5:
                game = new Game(5,questions);
                break;
            default:
                break;
        }



        showGame(game.getNumberOfPlayer());
    }

    @Override
    public void onBackPressed() {
        openMenuContent();
    }

    public void showGame(int numberOfPlayers){

        setContentView(R.layout.activity_game);
        lblPlayer1Point = (TextView) findViewById(R.id.lblPlayer1Point);
        lblPlayer2Point = (TextView) findViewById(R.id.lblPlayer2Point);
        lblPlayer3Point = (TextView) findViewById(R.id.lblPlayer3Point);
        lblPlayer4Point = (TextView) findViewById(R.id.lblPlayer4Point);
        lblPlayer5Point = (TextView) findViewById(R.id.lblPlayer5Point);
        lblPlayer1Point.setText("Player 1 has " + game.getPlayerPoint(1) + " points");
        lblPlayer2Point.setText("Player 2 has " + game.getPlayerPoint(2) + " points");
        lblPlayer3Point.setText("Player 3 has " + game.getPlayerPoint(3) + " points");
        lblPlayer4Point.setText("Player 4 has " + game.getPlayerPoint(4) + " points");
        lblPlayer5Point.setText("Player 5 has " + game.getPlayerPoint(5) + " points");
        btnGo = (Button) findViewById(R.id.btnGo);

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go();
            }
        });


        lblPlayer3Point.setVisibility(View.GONE);
        lblPlayer4Point.setVisibility(View.GONE);
        lblPlayer5Point.setVisibility(View.GONE);
        // switch does not working properly. Maybe if statment?
        switch (numberOfPlayers){
            case 3:
                lblPlayer3Point.setVisibility(View.VISIBLE);
                break;
            case 4:
                lblPlayer3Point.setVisibility(View.VISIBLE);
                lblPlayer4Point.setVisibility(View.VISIBLE);
                break;
            case 5:
                lblPlayer3Point.setVisibility(View.VISIBLE);
                lblPlayer4Point.setVisibility(View.VISIBLE);
                lblPlayer5Point.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }


    }

    private void addCurrentQuestionIdToCache(){


            final ArrayList<String> questionIdList = readQuestionsIdCache();
        System.out.println(" AddCurrentQuestionIDToCache method has index: " + game.getCurrentQuestionID());
            questionIdList.add(game.getCurrentQuestionID());

                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter(cacheFile));
                            for (String q: questionIdList) {
                                writer.write(q + " ");
                            }
                            writer.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();
            }

    private ArrayList<String> readQuestionsIdCache(){
        try{
            ArrayList<String> listQuestionsID = new ArrayList<String>();
            cacheFile = new File(MainActivity.this.getCacheDir(), "usedQuestionsIDs.txt");
            if (cacheFile.exists() == false) {
                cacheFile.createNewFile();
            }
            Scanner s = new Scanner(cacheFile);
            while (s.hasNext()){
                listQuestionsID.add(s.next());
            }
            for(String questionID:listQuestionsID){
                System.out.println(questionID + " ");
            }

            s.close();
            return listQuestionsID;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    public void go(){

        String question = game.getQuestion(readQuestionsIdCache());
        if (question == null){
            question = "There are no more questions";
        }

        addCurrentQuestionIdToCache();

        setContentView(R.layout.go);
        lblQuestion = (TextView) findViewById(R.id.lblQuestion);
        lblPlayerTurn = (TextView) findViewById(R.id.lblPlayerTrun);
        btnYesAnswer = (Button) findViewById(R.id.btnYesAnswer);
        btnNoAnswer = (Button) findViewById(R.id.btnNoAnswer);

        lblPlayerTurn.setText("Player" + game.getWhichPlayerHasTurn() + " turn");
        lblQuestion.setText(question);
        // next task. To find out how to add point to the player

        btnYesAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.givePoint();
                game.nextTurn();
                showGame(game.getNumberOfPlayer());
            }
        });

        btnNoAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.nextTurn();
                showGame(game.getNumberOfPlayer());
            }
        });

    }

    public void openMenuContent(){

        setContentView(R.layout.activity_main);

        lblWelcome = (TextView) findViewById(R.id.lblWelcome);
        lblChooseNumberPlayer = (TextView) findViewById(R.id.lblChooseNumberPlayer);
        btn2Player = (Button) findViewById(R.id.btn2Players);
        btn3Player = (Button) findViewById(R.id.btn3Players);
        btn4Player = (Button) findViewById(R.id.btn4Players);
        btn5Player = (Button) findViewById(R.id.btn5Players);

        btn2Player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame(2);
            }
        });
        btn3Player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame(3);
            }
        });
        btn4Player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame(4);
            }
        });
        btn5Player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame(5);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.GONE);



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openMenuContent();

       AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                Log.d("YourMainActivity", "AWSMobileClient is instantiated and you are connected to AWS!");
            }
        }).execute();

        AWSMobileClient.getInstance().initialize(this).execute();
        AWSCredentialsProvider credentialsProvider = AWSMobileClient.getInstance().getCredentialsProvider();
        AWSConfiguration configuration = AWSMobileClient.getInstance().getConfiguration();

        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(credentialsProvider);

        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(configuration)
                .build();

  //      QuestionsDO item = dynamoDBMapper.load(QuestionsDO.class, 1,
  //              new DynamoDBMapperConfig(DynamoDBMapperConfig.ConsistentReads.CONSISTENT));
  //      dynamoDBMapper.delete(item);

   //     createQuestion("Enter 4 things you can find in the kitchen", "0");
    //    createQuestion("Enter 4 movie titles", "1");
    //    createQuestion("Enter 4 rock bands", "2");
    //    createQuestion("Enter 4 car brands", "3");
    //    createQuestion("Enter 4 animals", "4");



        Thread thread = new Thread(new Runnable(){
            public void run() {
                try {
                    questions = getQuestionsFromDB(); //
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        System.out.println(readQuestionsIdCache());

    }
    public ArrayList<QuestionsDO> getQuestionsFromDB() throws NetworkOnMainThreadException{
        DynamoDBScanExpression dynamoDBScanExpression = new DynamoDBScanExpression();
        DynamoDBMapperConfig config = new DynamoDBMapperConfig(DynamoDBMapperConfig.PaginationLoadingStrategy.EAGER_LOADING);
        PaginatedScanList<QuestionsDO> paginatedScanList = dynamoDBMapper.scan(QuestionsDO.class, dynamoDBScanExpression, config);
        paginatedScanList.loadAllResults();

        ArrayList<QuestionsDO> list = new ArrayList<>();

        Iterator<QuestionsDO> iterator = paginatedScanList.iterator();
        while (iterator.hasNext()) {
            QuestionsDO element = iterator.next();
            list.add(element);
        }

        return list;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
