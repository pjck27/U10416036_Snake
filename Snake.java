//U10416036李孟霖

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Application.launch;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.stage.Popup;

public class Snake extends Application {
    
    // Direction
    public enum Direction{
        UP, DOWN, LEFT, RIGHT
    }
    
    // Data field
    public static final int BLOCK_SIZE = 40;
    public static final int APP_W = 20 * BLOCK_SIZE;
    public static final int APP_H = 15 * BLOCK_SIZE;
    
    private int score = 0;
    // Set default direction right
    private Direction direction = Direction.RIGHT;
    
    private boolean moved = false;
    private boolean running = false;
    
    private Timeline timeline = new Timeline();
    private ObservableList<Node> snake;
    
    //create the content of the snake(food, body, direction)
    private Parent createContent(){
        Pane root = new Pane();
        root.setPrefSize(APP_W, APP_H);
        
        Group snakeBody = new Group();
        snake = snakeBody.getChildren();
        
        Text t = new Text();
        t.setX(10.0f);
        t.setY(20.0f);
        t.setText("Score: "+ score);
        t.setFill(Color.BLACK);
        t.setFont(Font.font(null, FontWeight.BOLD, 12));

        //set food size and location
        Rectangle food = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);
        food.setFill(Color.BLUE);
        food.setTranslateX((int)(Math.random() * APP_W - BLOCK_SIZE) / BLOCK_SIZE * BLOCK_SIZE);
        food.setTranslateY((int)(Math.random() * APP_H - BLOCK_SIZE) / BLOCK_SIZE * BLOCK_SIZE);
        
        //
        KeyFrame frame = new KeyFrame(Duration.seconds(0.2), event -> {
            if(!running){
                return;
            }
            boolean toRemove = snake.size() > 1;
            Node tail = toRemove ? snake.remove(snake.size() - 1) : snake.get(0);
            double tailX = tail.getTranslateX();
            double tailY = tail.getTranslateY();
            
            switch (direction){
                case UP:
                    tail.setTranslateX(snake.get(0).getTranslateX());
                    tail.setTranslateY(snake.get(0).getTranslateY() - BLOCK_SIZE);
                    break;
                case DOWN:
                    tail.setTranslateX(snake.get(0).getTranslateX());
                    tail.setTranslateY(snake.get(0).getTranslateY() + BLOCK_SIZE);
                    break;
                case LEFT:
                    tail.setTranslateX(snake.get(0).getTranslateX()- BLOCK_SIZE);
                    tail.setTranslateY(snake.get(0).getTranslateY());
                    break;
                case RIGHT:
                    tail.setTranslateX(snake.get(0).getTranslateX()+ BLOCK_SIZE);
                    tail.setTranslateY(snake.get(0).getTranslateY());
                    break;
            
            }
            moved = true;
            
            if(toRemove)
                snake.add(0, tail);
            for(Node rect : snake){
                if(rect != tail && tail.getTranslateX() == rect.getTranslateX()&& tail.getTranslateY() == rect.getTranslateY()){
                    score = 0;
                    t.setText("Score: " + score);
                    restartGame();
                    break;
                }
            
            }
            if(tail.getTranslateX() < 0|| tail.getTranslateX() >= APP_W 
                    || tail.getTranslateY() < 0 || tail.getTranslateY() >= APP_H){
                score = 0;
                t.setText("Score: " + score);
                restartGame();
            }
            if(tail.getTranslateX() == food.getTranslateX() && tail.getTranslateY() == food.getTranslateY()){
                score++;
                t.setText("Score: " + score);
                food.setTranslateX((int)(Math.random() * APP_W - BLOCK_SIZE) / BLOCK_SIZE * BLOCK_SIZE);
                food.setTranslateY((int)(Math.random() * APP_H - BLOCK_SIZE) / BLOCK_SIZE * BLOCK_SIZE);
                Rectangle rect = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);
                rect.setTranslateX(tailX);
                rect.setTranslateY(tailY);
                snake.add(rect);
            }
        });
        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        root.getChildren().addAll(food,snakeBody,t);
        return root;
    }
    private void restartGame(){
        stopGame();
        startGame();
    }
    
    private void startAlert(){
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Start");
        alert.setHeaderText(null);
        alert.setContentText("遊戲即將開始，請使用↑↓←→控制");
        alert.showAndWait();
    }
    private void startGame(){
        score = 0;
        
        direction = Direction.RIGHT;
        Rectangle head = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);
        snake.add(head);
        timeline.play();
        running = true;
    
    }
    private void stopGame(){
        running = false;
        timeline.stop();
        snake.clear();
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        Scene scene = new Scene(createContent());
        startAlert();
        scene.setOnKeyPressed(event -> {
            if(moved){
                switch(event.getCode()){
                    case UP :
                        if(direction != Direction.DOWN)
                            direction = Direction.UP;
                        break;
                    case DOWN :
                        if(direction != Direction.UP)
                            direction = Direction.DOWN;
                        break;
                    case LEFT :
                        if(direction != Direction.RIGHT)
                            direction = Direction.LEFT;
                        break;
                    case RIGHT :
                        if(direction != Direction.LEFT)
                            direction = Direction.RIGHT;
                        break;
                }
            
            }
            moved = false;
        });                   
        primaryStage.setTitle("貪吃蛇");
        primaryStage.setScene(scene);
        primaryStage.show();
        startGame();
    }
    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
