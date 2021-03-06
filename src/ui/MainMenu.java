package ui;

import exceptions.NoPreviousException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.Scene;
import model.Exercise;
import model.Food;
import model.Item;
import model.ItemLog;
import observer.ItemDoneMonitor;

import java.io.*;
import java.util.Scanner;

import static ui.AddBox.display;

public class MainMenu extends Application {
    public static Stage window;
    Scene scene;
    static int totalCals;
    static ItemLog itemLog;
    static Label titleCals;
    static ItemDoneMonitor observer;
    static Label notify;
    static ListView<String> listviewF;
    static ListView<String> listviewE;
    public static Label exception;
    static MediaPlayer mediaPlayer;


    public void setItemLog(ItemLog il){
        this.itemLog = il;
    }

    public MainMenu(){
        this.observer = new ItemDoneMonitor();
    }

    public void run(String[] args){
        try {
            launch(args);
        } catch (RuntimeException e){}
    }

    public static void updateRemove(ItemLog il, Item i){
        totalCals = il.getTotal();
        titleCals.setText("Calories Logged: " + Integer.toString(totalCals));
        notify.setText(observer.updateRemove(i));
        viewListRemove(i);
    }
    public static void updateAdd(ItemLog il, Item i){
        totalCals = il.getTotal();
        titleCals.setText("Calories Logged: " + Integer.toString(totalCals));
        notify.setText(observer.update(i));
        viewList(i);
    }



    @Override
    public void start(Stage primaryStage){
        window = primaryStage;
        window.setTitle("Calorie Counter");
        BorderPane mainUI = new BorderPane();
        mainUI.setPadding(new Insets(20,20,20,20));

        VBox top = new VBox(10);
        top.setAlignment(Pos.CENTER);

        top.setPadding(new Insets(10,10,10,10));

        Label titleString = new Label("Welcome to your calorie counter!");
        ImageView catBurger = new ImageView();
        catBurger.setPreserveRatio(true);
        catBurger.setFitHeight(50);
        try {
            Image image = new Image(new FileInputStream("burger.gif"));
            catBurger.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ImageView catBurger1 = new ImageView();
        catBurger1.setPreserveRatio(true);
        catBurger1.setFitHeight(50);
        try {
            Image image = new Image(new FileInputStream("burger.gif"));
            catBurger1.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        HBox topBox = new HBox(10);
        topBox.getChildren().addAll(catBurger, titleString, catBurger1);
        topBox.setAlignment(Pos.CENTER);

        titleCals = new Label("Calories logged: " + totalCals);
        top.getChildren().addAll(topBox, titleCals);
        VBox leftMenu = new VBox(10);
        leftMenu.setMinSize(200,200);
        leftMenu.setPadding(new Insets(10,10,10,5));

        String file = ("ping.mp3");
        Media sound = new Media(new File(file).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);


        Button addButton = new Button("Add");
        Button removeButton = new Button("Remove");
        Button viewButton = new Button("View Previous");
        Button setButton = new Button("Resume Previous");
        Button exitButton = new Button("Exit");
        addButton.setPadding(new Insets(5, 5, 5, 5));
        removeButton.setPadding(new Insets(5, 5, 5, 5));
        viewButton.setPadding(new Insets(5, 5, 5, 5));
        setButton.setPadding(new Insets(5, 5, 5, 5));
        exitButton.setPadding(new Insets(5, 5, 5, 5));
        leftMenu.getChildren().addAll(addButton,removeButton,viewButton,setButton,exitButton);
        leftMenu.setStyle("-fx-border-color: gray");

        exitButton.setOnAction(e -> closeProgram());
        addButton.setOnAction(e -> addOptions());
        removeButton.setOnAction(e -> removeOptions());
        setButton.setOnAction(e -> resumeLog());
        viewButton.setOnAction(e -> previousLog());

        mainUI.setTop(top);
        mainUI.setLeft(leftMenu);

        Button mealButton = new Button("MEAL OF THE DAY");
        mealButton.setMinSize(200, 200);
        mealButton.setPadding(new Insets(10,10,10,10));
        mealButton.setAlignment(Pos.CENTER);
        mealButton.setOnAction(e -> {
            try {
                viewMeal();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });

        mainUI.setRight(mealButton);

        notify = new Label();
        notify.setPadding(new Insets(10,0,0,0));
        mainUI.setBottom(notify);
        Label food = new Label("Food Logged:");
        food.setPadding(new Insets(10,20,10,20));


        listviewF = new ListView<>();
        StackPane listBorderF = new StackPane();
        StackPane.setMargin(listviewF, new Insets(10,20,10,20));
        listBorderF.getChildren().add(listviewF);

        Label exercise = new Label("Exercise Logged:");
        exercise.setPadding(new Insets(10,20,10,20));

        listviewE = new ListView<>();
        StackPane listBorderE = new StackPane();
        StackPane.setMargin(listviewE, new Insets(10,20,20,20));
        listBorderE.getChildren().add(listviewE);

        exception = new Label();
        exception.setPadding(new Insets(2, 20, 15,20));
        VBox layout = new VBox();
        layout.getChildren().addAll(mainUI, exception, food, listBorderF, exercise, listBorderE);
        scene = new Scene(layout, 550, 700);

        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });

        window.setScene(scene);
        window.show();
    }

    private void closeProgram() {
        Boolean answer = ConfirmBox.display("", "Sure you want to exit?");
        if(answer)
            window.close();
    }

    private void removeOptions() {
        RemoveBox.display(); }

    private void addOptions() {
        AddBox.display();
    }

    private void viewMeal() throws FileNotFoundException {
        Scanner inFile = new Scanner(new FileReader("meal.txt"));
        String sFile = "";

        while(inFile.hasNextLine())
            sFile = sFile + inFile.nextLine() + "\n";
        MealBox.display(sFile);
    }

    public static void viewList(Item i){
        if (i instanceof Food)
            listviewF.getItems().add(i.summary(i));
        if (i instanceof Exercise)
            listviewE.getItems().add(i.summary(i));

        window.show();
    }

    public static void viewListRemove(Item i){
        if (i instanceof Food)
            listviewF.getItems().remove(i.summary(i));
        if (i instanceof Exercise)
            listviewE.getItems().remove(i.summary(i));

        window.show();
    }

    public static void resumeLog(){
        try {
            itemLog.optionResume();
        } catch (IOException e) {
        } catch (NoPreviousException e) {
        } finally { window.show();}
    }

    private void previousLog(){ PreviousBox.display(); }
}
