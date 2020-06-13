/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpygrof;

import java.io.*;
import java.util.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;

/**
 *
 * @author USER
 */
public class GUI extends Application{
    protected static Map<String,Integer> map=new Map();
    protected static LinkedList<Kangaroo>kangaroo=new LinkedList();
    private Scene scene1,scene2,scene3,scene4,scene5,scene6;
    private int numberOfPoint,maxPoint,maxPath,numberOfRows,numPath,index,Threshold;
    private Random r;
    
    public static void start(){
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Jumpy Grof");
        
        ///////////////////////////First Layout///////////////////////////////
        Image image=new Image(new FileInputStream("Sleeping facing right.png"));
        ImageView imageview1=new ImageView(image);
        imageview1.setFitHeight(200);
        imageview1.setFitWidth(300);
        
        Button startButton1 = new Button();
        startButton1.setText("Start");
        startButton1.setMinHeight(50);
        startButton1.setMinWidth(100);
        startButton1.setOnAction(e->primaryStage.setScene(scene2));
        
        Label Welcomelabel=new Label("Welcome to JumpyGrof Simulator");
        Welcomelabel.setFont(new Font("Times New Roman", 48));
        
        VBox box1 = new VBox(20);
        box1.getChildren().addAll(imageview1,Welcomelabel,startButton1);
        box1.setAlignment(Pos.CENTER);
        scene1 = new Scene(box1, 1000, 800);
        primaryStage.setScene(scene1);
        
        ///////////////////////////Second Layout///////////////////////////////
        TextField inputPoint = new TextField();
        inputPoint.setPromptText("Enter number of points in the map");
        inputPoint.setMaxWidth(300);
        inputPoint.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER))
                if(isInteger(inputPoint)){
                    primaryStage.setScene(scene3);
                    numberOfPoint= Integer.parseInt(inputPoint.getText());
                    maxPoint=numberOfPoint;
                    maxPath = numberOfPoint-1;
                }
                else
                    GUIAlertBox.display("Error", inputPoint.getText()+" is not an integer.");
        });;
        
        Label Pointlabel = new Label("Enter number of points in the map:");
        Pointlabel.setFont(new Font("Times New Roman",24));
        
        Button enterButton2 = new Button("Enter");
        enterButton2.setMinHeight(50);
        enterButton2.setMinWidth(100);
        enterButton2.setOnAction(e -> {   
            if(isInteger(inputPoint)){
                primaryStage.setScene(scene3);
                numberOfPoint= Integer.parseInt(inputPoint.getText());
                maxPoint=numberOfPoint;
                maxPath = numberOfPoint-1;
            }
            else
                GUIAlertBox.display("Error", inputPoint.getText()+" is not an integer.");
        });
        
        ImageView imageview2=new ImageView(image);
        imageview2.setFitHeight(200);
        imageview2.setFitWidth(300);
        
        VBox input_layout = new VBox(20);
        input_layout.getChildren().addAll(imageview2,Pointlabel,inputPoint,enterButton2);
        input_layout.setAlignment(Pos.CENTER);
        scene2 = new Scene(input_layout, 1000,800);

        ///////////////////////////Third Layout///////////////////////////////
        ObservableList<Point<String,Integer>> pointlist=FXCollections.observableArrayList();
        
        TableColumn<Map, String> id = new TableColumn<>("ID");
        TableColumn<Map, Integer> food = new TableColumn<>("Number of food");
        TableColumn<Map, Integer> size = new TableColumn<>("Max Kangaroo Size");
        TableColumn<Map, Integer> path = new TableColumn<>("Number of Paths Connected");
        id.setMinWidth(50);
        food.setMinWidth(150);
        size.setMinWidth(200);
        path.setMinWidth(250);
        id.setCellValueFactory(new PropertyValueFactory<>("ID"));
        food.setCellValueFactory(new PropertyValueFactory<>("food"));
        size.setCellValueFactory(new PropertyValueFactory<>("kangaroo_limit"));
        path.setCellValueFactory(new PropertyValueFactory<>("path"));   
        TableView table = new TableView<>();
        table.getColumns().addAll(id,food,size,path);
        
        TextField inputID = new TextField();
        TextField inputFood = new TextField();
        TextField inputSize = new TextField();
        TextField inputPath = new TextField();
        
        Label IDlabel = new Label("ID:");
        Label foodlabel = new Label("Number of food:");
        Label kangaroolabel = new Label("Number of Kangaroo:");
        Label pathlabel = new Label("Number of Path:");
        inputID.setMaxWidth(300);
        inputFood.setMaxWidth(300);
        inputSize.setMinWidth(300);
        inputPath.setMinWidth(300);
        
        Button addButton3 = new Button("Add");
        addButton3.setOnAction(e->{
            boolean valid=true;
            if(inputID.getText().equals("")||inputFood.getText().equals("")||inputSize.getText().equals("")||inputPath.getText().equals("")){
                GUIAlertBox.display("Error", "Incomplete Data");
                valid=false;
                return;
            }
            if(numberOfPoint <1){
                GUIAlertBox.display("Attention","Exceeded number of points.\nPress OK to continue." );
                valid=false;
            }

            if(!isInteger(inputFood)||!isInteger(inputPath)||!isInteger(inputSize)){
                GUIAlertBox.display("Error", "Incorrect data type.");
                valid=false;
            }
                
            if(map.hasPoint(inputID.getText())!=null){
                GUIAlertBox.display("Error", "Redundant Path ID.");
                valid=false;
            }
            
            if(Integer.parseInt(inputPath.getText())>maxPath){
                GUIAlertBox.display("Error", "Number of path should be less than "+ "the total number of path can be connected ("+maxPath+")");
                inputPath.clear();
                valid=false;
                return;
            }
            
            if(valid){
                pointlist.add(map.addPoint(inputID.getText(),Integer.parseInt(inputFood.getText()),Integer.parseInt(inputSize.getText()),Integer.parseInt(inputPath.getText())));
                numberOfPoint--;       
                numberOfRows= id.getTableView().getItems().size();
                table.setItems(pointlist);
            }
            inputID.clear();
            inputFood.clear();
            inputSize.clear();
            inputPath.clear();
        });
        
        Button nextButton3 = new Button("Next");
        nextButton3.setOnAction(e->{primaryStage.setScene(scene4);});
        
        Button randomButton3= new Button("Random");
        randomButton3.setOnAction(e->{
            r=new Random();
            inputID.setText(r.nextInt(100)+"");
            inputFood.setText(r.nextInt(100)+20+"");
            inputSize.setText(r.nextInt(25)+2+"");
            inputPath.setText(r.nextInt(maxPath)+"");
        });
        
        Button backButton3=new Button("Back");
        backButton3.setOnAction(e -> {   
            primaryStage.setScene(scene2);
        });
        
        GridPane grid3 = new GridPane();
        grid3.setPadding(new Insets(70,20,20,0));
        grid3.setVgap(8);
        grid3.setHgap(20);
        GridPane.setConstraints(IDlabel, 11, 0);GridPane.setConstraints(inputID, 12, 0);
        GridPane.setConstraints(foodlabel, 11, 2);GridPane.setConstraints(inputFood, 12, 2);
        GridPane.setConstraints(kangaroolabel, 11, 4);GridPane.setConstraints(inputSize, 12, 4);
        GridPane.setConstraints(pathlabel, 11, 6);GridPane.setConstraints(inputPath, 12, 6);
        grid3.getChildren().addAll(IDlabel, foodlabel, kangaroolabel, pathlabel, inputID, inputFood, inputSize, inputPath);
        
        GridPane buttons=new GridPane();
        buttons.setHgap(50);
        GridPane.setConstraints(backButton3,2,0);
        GridPane.setConstraints(addButton3,4,0);
        GridPane.setConstraints(randomButton3,6,0);
        GridPane.setConstraints(nextButton3,8,0);
        buttons.getChildren().addAll(backButton3,addButton3,randomButton3,nextButton3);
        buttons.setPadding(new Insets(0,100,100,100));
        BorderPane pane3 = new BorderPane();
        pane3.setTop(table);
        pane3.setCenter(grid3);
        pane3.setBottom(buttons);
        scene3 = new Scene(pane3,1000,800);
        scene3.setOnKeyPressed(e->{
            if(e.getCode().equals(KeyCode.ENTER))
                inputPath.setOnAction(addButton3.getOnAction());
        });
        ///////////////////////////Fourth Layout///////////////////////////////
        ObservableList<Path<Integer>> pathlist=FXCollections.observableArrayList();
        TableColumn<Path, String> source = new TableColumn<>("Source");
        TableColumn<Path, String> destination = new TableColumn<>("Destination");
        TableColumn<Path, Integer> obstacle = new TableColumn<>("Height");
        source.setMinWidth(100);
        destination.setMinWidth(150);
        obstacle.setMinWidth(100);
        source.setCellValueFactory((new PropertyValueFactory<>("source")));
        destination.setCellValueFactory(new PropertyValueFactory<>("destination"));
        obstacle.setCellValueFactory(new PropertyValueFactory<>("obstacle_height"));   
        TableView pathtable = new TableView<>();
        pathtable.getColumns().addAll(source,destination,obstacle);
        
        TextField inputDestination = new TextField();
        TextField inputHeight = new TextField();
        TextField inputSource = new TextField();
        Label Sourcelabel = new Label("Source:");
        Label Destinationlabel = new Label("Destination:");
        Label Heightlabel = new Label("Height:");
         
        inputDestination.setMaxWidth(300);
        inputHeight.setMaxWidth(300);
        inputSource.setMaxWidth(300);
        
        Button addButton4 = new Button("Add Path");
        addButton4.setOnAction(e->{
            boolean valid=true;
            
            if(inputDestination.getText().equals("")||inputHeight.getText().equals("")){
                GUIAlertBox.display("Error", "Incomplete Data");
                valid=false;
                return;
            }
            
            if(map.hasPoint(inputSource.getText()).getPath()<1){
                GUIAlertBox.display("Attention","Exceeded number of path." );
                valid=false;
            }

            if(!isInteger(inputHeight)){
                GUIAlertBox.display("Error", "Incorrect data type.");
                valid=false;
            }
                
            if(map.isPath(inputSource.getText(),inputDestination.getText())){
                GUIAlertBox.display("Error", "Redundant Path ID.");
                valid=false;
            }
            
            if(inputSource.getText().equals(inputDestination.getText())){
                GUIAlertBox.display("Error", "Same ID.");
                valid=false;
            }
            
            if(valid){
                pathlist.add(map.addPath(inputSource.getText(),inputDestination.getText(),Integer.parseInt(inputHeight.getText()),false));
//                pathlist.add(map.addPath(inputSource.getText(),inputDestination.getText(),Integer.parseInt(inputHeight.getText())));
                pathlist.get(pathlist.size()-1).setSource(map.hasPoint(inputSource.getText()));
                numPath=map.hasPoint(inputSource.getText()).getPath();
                map.hasPoint(inputSource.getText()).setPath(numPath-1);
                pathtable.setItems(pathlist);
            }
            inputSource.clear();
            inputDestination.clear();
            inputHeight.clear();
        });
        
        Button nextButton4 = new Button("Next");
        nextButton4.setOnAction(e->{primaryStage.setScene(scene5);});
        
        Button randomButton4= new Button("Random");
        randomButton4.setOnAction(e->{
            r=new Random();
            inputSource.setText(map.get(r.nextInt(map.size())).getID()+"");
            inputDestination.setText(map.get(r.nextInt(map.size())).getID()+"");
            inputHeight.setText((r.nextInt(15)+1)+"");
        });
        
        Button backButton4=new Button("Back");
        backButton4.setOnAction(e -> {   
            primaryStage.setScene(scene3);
        });
        
        GridPane grid4 = new GridPane();
        grid4.setPadding(new Insets(50,20,20,0));
        grid4.setVgap(8);
        grid4.setHgap(20);
        GridPane.setConstraints(Sourcelabel,15,0);GridPane.setConstraints(inputSource, 16, 0);
        GridPane.setConstraints(Destinationlabel, 15, 2);GridPane.setConstraints(inputDestination, 16, 2);
        GridPane.setConstraints(Heightlabel, 15, 4);GridPane.setConstraints(inputHeight, 16, 4);
        grid4.getChildren().addAll(Sourcelabel,inputSource,Destinationlabel, Heightlabel,inputDestination, inputHeight);
        GridPane buttons4=new GridPane();
        buttons4.setPadding(new Insets(0,100,100,100));
        buttons4.setHgap(50);
        GridPane.setConstraints(backButton4,2,0);
        GridPane.setConstraints(addButton4,4,0);
        GridPane.setConstraints(randomButton4,6,0);
        GridPane.setConstraints(nextButton4,8,0);
        buttons4.getChildren().addAll(backButton4,addButton4,randomButton4,nextButton4);
        BorderPane pane4 = new BorderPane();
        pane4.setTop(pathtable);
        pane4.setCenter(grid4);
        pane4.setBottom(buttons4);
        scene4 = new Scene(pane4,1000,800);
        scene4.setOnKeyPressed(e->{
            if(e.getCode().equals(KeyCode.ENTER))
                inputHeight.setOnAction(addButton4.getOnAction());
        });
        ///////////////////////////Fifth Layout///////////////////////////////
        ObservableList<Kangaroo> kangaroolist=FXCollections.observableArrayList();
        
        TableColumn<Kangaroo, Point> location = new TableColumn<>("Location");
        TableColumn<Kangaroo, Character> gender = new TableColumn<>("Gender");
        TableColumn<Kangaroo, Integer> maxfood=new TableColumn<>("Maximum food");
        location.setMinWidth(150);
        gender.setMinWidth(200);
        maxfood.setMinWidth(150);
        location.setCellValueFactory(new PropertyValueFactory<>("point"));
        gender.setCellValueFactory(new PropertyValueFactory<>("gender"));   
        maxfood.setCellValueFactory(new PropertyValueFactory<>("food_limit"));
        TableView kangarootable = new TableView<>();
        kangarootable.getColumns().addAll(location,gender,maxfood);
        
        TextField inputLocation = new TextField();
        TextField inputGender = new TextField();
        TextField inputMaxFood = new TextField();
        Label Locationlabel = new Label("Location:");
        Label Genderlabel = new Label("Gender:");
        Label MaxFoodlabel = new Label("Maximum food:");
         
        inputLocation.setMaxWidth(300);
        inputGender.setMaxWidth(300);
        inputMaxFood.setMaxWidth(300);
        
        Button addButton5 = new Button("Add Kangaroo");
        addButton5.setOnAction(e->{
            boolean valid=true;
            
            if(inputLocation.getText().equals("")||inputGender.getText().equals("")||inputMaxFood.getText().equals("")){
                GUIAlertBox.display("Error", "Incomplete Data");
                valid=false;
                return;
            }
            if(map.hasPoint(inputLocation.getText()).isFull()){
                GUIAlertBox.display("Attention","The point is full" );
                valid=false;
            }

            if(!isInteger(inputMaxFood)||!(inputGender.getText().toUpperCase().charAt(0)==('M'))&&!(inputGender.getText().toUpperCase().charAt(0)==('F'))){
                GUIAlertBox.display("Error", "Incorrect data type.");
                valid=false;
            }
                
            if(map.hasPoint(inputLocation.getText())==null){
                GUIAlertBox.display("Error", "The point do not exist");
                valid=false;
            }
            
            if(valid){
                kangaroo.add(new Kangaroo(map.hasPoint(inputLocation.getText()),inputGender.getText().charAt(0),Integer.parseInt(inputMaxFood.getText())));
//                kangaroolist.add(new Kangaroo(map.hasPoint(inputLocation.getText()),inputGender.getText().charAt(0),Integer.parseInt(inputMaxFood.getText())));
                kangaroolist.add(kangaroo.getLast());
                kangarootable.setItems(kangaroolist);
            }
            inputLocation.clear();
            inputGender.clear();
            inputMaxFood.clear();
        });
        
        Button nextButton5 = new Button("Next");
        nextButton5.setOnAction(e->{primaryStage.setScene(scene6);
        });
        
        Button randomButton5= new Button("Random");
        randomButton5.setOnAction(e->{
            r=new Random();
            inputLocation.setText(map.get(r.nextInt(map.size())).getID()+"");
            char temp=r.nextInt(2)>0?'M':'F';
            inputGender.setText(temp+"");
            inputMaxFood.setText((r.nextInt(13)+1)+"");
        });
        
        Button backButton5=new Button("Back");
        backButton5.setOnAction(e -> {   
            primaryStage.setScene(scene4);
        });
        
        GridPane grid5 = new GridPane();
        grid5.setPadding(new Insets(70,20,20,0));
        grid5.setVgap(8);
        grid5.setHgap(20);
        GridPane.setConstraints(Locationlabel,15,0);GridPane.setConstraints(inputLocation, 16, 0);
        GridPane.setConstraints(Genderlabel, 15, 2);GridPane.setConstraints(inputGender, 16, 2);
        GridPane.setConstraints(MaxFoodlabel, 15, 4);GridPane.setConstraints(inputMaxFood, 16, 4);
        grid5.getChildren().addAll(Locationlabel,inputLocation,Genderlabel,MaxFoodlabel,inputGender, inputMaxFood);
        GridPane buttons5=new GridPane();
        buttons5.setPadding(new Insets(0,100,100,100));
        buttons5.setHgap(50);
        GridPane.setConstraints(backButton5,2,0);
        GridPane.setConstraints(addButton5,4,0);
        GridPane.setConstraints(randomButton5,6,0);
        GridPane.setConstraints(nextButton5,8,0);
        buttons5.getChildren().addAll(backButton5,addButton5,randomButton5,nextButton5);
        BorderPane pane5 = new BorderPane();
        pane5.setTop(kangarootable);
        pane5.setCenter(grid5);
        pane5.setBottom(buttons5);
        scene5 = new Scene(pane5,1000,800);
        scene5.setOnKeyPressed(e->{
            if(e.getCode().equals(KeyCode.ENTER))
                inputMaxFood.setOnAction(addButton5.getOnAction());
        });
        ///////////////////////////Sixth Layout///////////////////////////////
        TextField inputThreshold = new TextField();
        inputThreshold.setPromptText("Enter colony threshold");
        inputThreshold.setMaxWidth(300);
        
        Label Thresholdlabel=new Label("Enter the threshold to form colony:");
        
        Button enterButton6 = new Button("Enter");
        enterButton6.setOnAction(e->{
            if(isInteger(inputThreshold))
                map.setThreshold(Integer.parseInt(inputThreshold.getText()));
            else
                GUIAlertBox.display("Error", inputThreshold.getText()+" is not an integer.");
        });
        
        Button backButton6=new Button("Back");
        backButton6.setOnAction(e -> {   
            primaryStage.setScene(scene5);
        });
        
        Button startButton6=new Button("Start");
        enterButton6.setOnAction(e->{
            try{
                JumpyGrof.run();
            }catch(Exception i){}
        });
        
        ImageView imageview3=new ImageView(image);
        imageview3.setFitHeight(200);
        imageview3.setFitWidth(300);
        
        BorderPane buttons6=new BorderPane();
        buttons6.setPadding(new Insets(10,400,10,400));
        buttons6.setLeft(backButton6);
        buttons6.setCenter(enterButton6);
        buttons6.setRight(startButton6);
        
        VBox input_layout1 = new VBox(20);
        input_layout1.getChildren().addAll(imageview3,Thresholdlabel,inputThreshold,buttons6);
        input_layout1.setAlignment(Pos.CENTER);
        scene6 = new Scene(input_layout1, 1000,800);
        
        scene6.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER))
                inputThreshold.setOnAction(enterButton6.getOnAction());
        });
        
        primaryStage.show();
    }
    
    private boolean isInteger(TextField a){
        try{
            int number = Integer.parseInt(a.getText());
            return true;
        }catch(NumberFormatException e){ return false;}
    }
}
