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
    public static Map<String,Integer> map=new Map();
    public static LinkedList<Kangaroo>kangaroo=new LinkedList();
    Scene scene1,scene2,scene3,scene4,scene5;
    int numberOfPoint,maxPoint,maxPath,numberOfRows,numPath,index;
    Random r;
    
    public static void main(String[]args){
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
        
        Button startbutton = new Button();
        startbutton.setText("Start");
        startbutton.setMinHeight(50);
        startbutton.setMinWidth(100);
        startbutton.setOnAction(e->primaryStage.setScene(scene2));
        
        Label welcome=new Label("Welcome to JumpyGrof Simulator");
        welcome.setFont(new Font("Times New Roman", 48));
        
        VBox starting_layout = new VBox(20);
        starting_layout.getChildren().addAll(imageview1,welcome,startbutton);
        starting_layout.setAlignment(Pos.CENTER);
        Scene startingScene = new Scene(starting_layout, 1000, 800);
        primaryStage.setScene(startingScene);
        
        ///////////////////////////Second Layout///////////////////////////////
        TextField pointnumber = new TextField();
        pointnumber.setPromptText("Enter number of points in the map");
        pointnumber.setMaxWidth(300);
        pointnumber.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER))
                if(isInteger(pointnumber)){
                    primaryStage.setScene(scene3);
                    numberOfPoint= Integer.parseInt(pointnumber.getText());
                    maxPoint=numberOfPoint;
                    maxPath = numberOfPoint-1;
                }
                else
                    GUIAlertBox.display("Error", pointnumber.getText()+" is not an integer.");
        });;
        
        Label input = new Label("Enter number of points in the map:");
        input.setFont(new Font("Times New Roman",24));
        
        Button enterbutton = new Button("Enter");
        enterbutton.setMinHeight(50);
        enterbutton.setMinWidth(100);
        enterbutton.setOnAction(e -> {   
            if(isInteger(pointnumber)){
                primaryStage.setScene(scene3);
                numberOfPoint= Integer.parseInt(pointnumber.getText());
                maxPoint=numberOfPoint;
                maxPath = numberOfPoint-1;
            }
            else
                GUIAlertBox.display("Error", pointnumber.getText()+" is not an integer.");
        });
        
        ImageView imageview2=new ImageView(image);
        imageview2.setFitHeight(200);
        imageview2.setFitWidth(300);
        
        VBox input_layout = new VBox(20);
        input_layout.getChildren().addAll(imageview2,input,pointnumber,enterbutton);
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
        
        Button addButton = new Button("Add");
        addButton.setOnAction(e->{
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
                inputID.clear();
                inputFood.clear();
                inputSize.clear();
                inputPath.clear();
                valid=false;
            }
            
            if(Integer.parseInt(inputPath.getText())>maxPath){
                GUIAlertBox.display("Error", "Number of path should be less than "+ "the total number of path can be connected ("+maxPath+")");
                inputPath.clear();
                valid=false;
                return;
            }
            
            if(valid){
                map.addPoint(inputID.getText(),Integer.parseInt(inputFood.getText()),Integer.parseInt(inputSize.getText()),Integer.parseInt(inputPath.getText()));
                pointlist.add(new Point(inputID.getText(),Integer.parseInt(inputFood.getText()),Integer.parseInt(inputSize.getText()),Integer.parseInt(inputPath.getText())));
                inputID.clear();
                inputFood.clear();
                inputSize.clear();
                inputPath.clear();
                numberOfPoint--;       
                numberOfRows= id.getTableView().getItems().size();
                table.setItems(pointlist);
            }
        });
        
        Button okButton = new Button("Ok");
        okButton.setOnAction(e->{primaryStage.setScene(scene4);});
        
        Button randomButton= new Button("Random");
        randomButton.setOnAction(e->{
            r=new Random();
            inputID.setText(r.nextInt(100)+"");
            inputFood.setText(r.nextInt(100)+20+"");
            inputSize.setText(r.nextInt(25)+2+"");
            inputPath.setText(r.nextInt(maxPath)+"");
        });
        
        GridPane grid1 = new GridPane();
        grid1.setPadding(new Insets(20));
        grid1.setVgap(8);
        grid1.setHgap(20);
        GridPane.setConstraints(IDlabel, 0, 0);GridPane.setConstraints(inputID, 1, 0);
        GridPane.setConstraints(foodlabel, 0, 2);GridPane.setConstraints(inputFood, 1, 2);
        GridPane.setConstraints(kangaroolabel, 0, 4);GridPane.setConstraints(inputSize, 1, 4);
        GridPane.setConstraints(pathlabel, 0, 6);GridPane.setConstraints(inputPath, 1, 6);
        grid1.getChildren().addAll(IDlabel, foodlabel, kangaroolabel, pathlabel, inputID, inputFood, inputSize, inputPath);
        BorderPane buttons=new BorderPane();
        buttons.setLeft(addButton);
        buttons.setCenter(randomButton);
        buttons.setRight(okButton);
        buttons.setPadding(new Insets(100));
        BorderPane pane = new BorderPane();
        pane.setTop(table);
        pane.setCenter(grid1);
        pane.setBottom(buttons);
        scene3 = new Scene(pane,1000,800);
        
        ///////////////////////////Fourth Layout///////////////////////////////
        ObservableList<Path<Integer>> pathlist=FXCollections.observableArrayList();
        
        //TableColumn<
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
        
        Button addButton2 = new Button("Add Path");
        addButton2.setOnAction(e->{
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
                inputDestination.clear();
                inputHeight.clear();
                valid=false;
            }
            
            if(inputSource.getText().equals(inputDestination.getText())){
                GUIAlertBox.display("Error", "Same ID.");
                inputDestination.clear();
                inputHeight.clear();
                valid=false;
            }
            
            if(valid){
                map.addPath(inputSource.getText(),inputDestination.getText(),Integer.parseInt(inputHeight.getText()));
                pathlist.add(new Path(map.hasPoint(inputDestination.getText()),Integer.parseInt(inputHeight.getText()),null,false));
                pathlist.get(pathlist.size()-1).setSource(map.hasPoint(inputSource.getText()));
                inputDestination.clear();
                inputHeight.clear();
                numPath=map.hasPoint(inputSource.getText()).getPath();
                map.hasPoint(inputSource.getText()).setPath(numPath-1);
                pathtable.setItems(pathlist);
            }
        });
        
        Button okButton2 = new Button("Ok");
        okButton2.setOnAction(e->{primaryStage.setScene(scene5);});
        
        Button randomButton2= new Button("Random");
        randomButton2.setOnAction(e->{
            r=new Random();
            inputDestination.setText(map.get(r.nextInt(map.size())).getID()+"");
            inputHeight.setText((r.nextInt(15)+1)+"");
        });
        
        GridPane grid2 = new GridPane();
        grid2.setPadding(new Insets(20));
        grid2.setVgap(8);
        grid2.setHgap(20);
        GridPane.setConstraints(Sourcelabel,0,0);GridPane.setConstraints(inputSource, 1, 0);
        GridPane.setConstraints(Destinationlabel, 0, 2);GridPane.setConstraints(inputDestination, 1, 2);
        GridPane.setConstraints(Heightlabel, 0, 4);GridPane.setConstraints(inputHeight, 1, 4);
        grid2.getChildren().addAll(Sourcelabel,inputSource,Destinationlabel, Heightlabel,inputDestination, inputHeight);
        BorderPane buttons2=new BorderPane();
        buttons2.setLeft(addButton2);
        buttons2.setCenter(randomButton2);
        buttons2.setRight(okButton2);
        buttons2.setPadding(new Insets(100));
        BorderPane pane2 = new BorderPane();
        pane2.setTop(pathtable);
        pane2.setCenter(grid2);
        pane2.setBottom(buttons2);
        scene4 = new Scene(pane2,1000,800);
        ///////////////////////////Fifth Layout///////////////////////////////
        ObservableList<Kangaroo> kangaroolist=FXCollections.observableArrayList();
        
        TableColumn<Kangaroo, Point> location = new TableColumn<>("Location");
        TableColumn<Kangaroo, Character> gender = new TableColumn<>("Gender");
        TableColumn<Kangaroo, Integer> maxfood=new TableColumn<>("Maximum food");
        location.setMinWidth(250);
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
        
        Button addButton3 = new Button("Add Kangaroo");
        addButton3.setOnAction(e->{
            boolean valid=true;
            
            if(inputLocation.getText().equals("")||inputGender.getText().equals("")||inputMaxFood.getText().equals("")){
                GUIAlertBox.display("Error", "Incomplete Data");
                valid=false;
                return;
            }
            if(map.hasPoint(inputSource.getText()).isFull()){
                GUIAlertBox.display("Attention","The point is full" );
                valid=false;
            }

            if(!isInteger(inputMaxFood)||!inputGender.equals("M")&&!inputGender.equals("F")){
                GUIAlertBox.display("Error", "Incorrect data type.");
                valid=false;
            }
                
            if(map.hasPoint(inputLocation.getText())==null){
                GUIAlertBox.display("Error", "The point do not exist");
                inputDestination.clear();
                inputHeight.clear();
                valid=false;
            }
            
            if(valid){
                kangaroo.add(new Kangaroo(map.hasPoint(inputLocation.getText()),inputGender.getText().charAt(0),Integer.parseInt(inputMaxFood.getText())));
                kangaroolist.add(new Kangaroo(map.hasPoint(inputLocation.getText()),inputGender.getText().charAt(0),Integer.parseInt(inputMaxFood.getText())));
                pathlist.get(pathlist.size()-1).setSource(map.hasPoint(inputSource.getText()));
                inputLocation.clear();
                inputGender.clear();
                inputMaxFood.clear();
                table.setItems(pathlist);
            }
        });
        
        Button okButton3 = new Button("Start");
        okButton3.setOnAction(e->{
            try{
            JumpyGrof.animation();
            }catch(Exception i){}
        });
        
        Button randomButton3= new Button("Random");
        randomButton3.setOnAction(e->{
            r=new Random();
            inputLocation.setText(map.get(r.nextInt(map.size())).getID()+"");
            char temp=r.nextInt(2)>0?'M':'F';
            inputGender.setText(temp+"");
            inputMaxFood.setText((r.nextInt(13)+1)+"");
        });
        
        GridPane grid3 = new GridPane();
        grid3.setPadding(new Insets(20));
        grid3.setVgap(8);
        grid3.setHgap(20);
        GridPane.setConstraints(Locationlabel,0,0);GridPane.setConstraints(inputLocation, 1, 0);
        GridPane.setConstraints(Genderlabel, 0, 2);GridPane.setConstraints(inputGender, 1, 2);
        GridPane.setConstraints(MaxFoodlabel, 0, 4);GridPane.setConstraints(inputMaxFood, 1, 4);
        grid3.getChildren().addAll(Locationlabel,inputLocation,Genderlabel,MaxFoodlabel,inputGender, inputMaxFood);
        BorderPane buttons3=new BorderPane();
        buttons3.setLeft(addButton3);
        buttons3.setCenter(randomButton3);
        buttons3.setRight(okButton3);
        buttons3.setPadding(new Insets(100));
        BorderPane pane3 = new BorderPane();
        pane3.setTop(kangarootable);
        pane3.setCenter(grid3);
        pane3.setBottom(buttons3);
        scene5 = new Scene(pane3,1000,800);

        primaryStage.show();
    }
    
    private boolean isInteger(TextField a){
        try{
            int number = Integer.parseInt(a.getText());
            return true;
        }catch(NumberFormatException e){ return false;}
    }
}