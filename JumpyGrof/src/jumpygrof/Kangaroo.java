package jumpygrof;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import static jumpygrof.JumpyGrof.logger;

public class Kangaroo{
    private final Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
    private final int height =(int)screenSize.getHeight();
    private Point point;
    private final Map map=JumpyGrof.map;
    private final int food_limit;
    private int food_available,x,y;
    private final char gender;
    private boolean colonised,move,left;
    private final Random r;
    private final Image stationaryF = new ImageIcon("image\\Female kangaroo.png").getImage();
    private final Image stationaryM = new ImageIcon("image\\Male kangaroo.png").getImage();
    private final Image jumpLeft = new ImageIcon("image\\Jumping left.gif").getImage();
    private final Image jumpRight = new ImageIcon("image\\Jumping right.gif").getImage();

    public Kangaroo(Point point, char gender, int food_limit) {
        this.point = point;
        this.food_limit = food_limit;
        this.gender = gender;
        //take the food at the first place
        if(point.getFood()>food_limit){
            food_available=food_limit;
            point.setFood(point.getFood()-food_limit);//remaining food at the point
        }
        else{
            food_available=point.getFood();
            point.setFood(0); //no remaining food at the point
        }
        this.point.addKangaroo(this);
        colonised=false;
        r=new Random();
        x=point.getX()+20+point.getKangaroo().size()*20;
        y=point.getY()+20+point.getKangaroo().size()*20;
        move=false;
    }

    public char getGender() {return gender;}
    
    public int getFoodAvailable() {return food_available;}

    public void setFoodAvailable(int food_available) {this.food_available = food_available;}

    public void setColonised(boolean colonised) {this.colonised = colonised;}

    public boolean isColonised() {return colonised;}

    public String getPoint() {return (String)point.getID();}

    public int getFood_limit() {return food_limit;}
    
    
    
    @Override
    public String toString(){return point.getID()+" "+gender+" "+food_available;}
    
    //kangaroo eat the required food at the point
    public void consumeFood(int food_required){
        if(point.getFood()<food_required){//if food at the point not enough
            food_required-=point.getFood();
            point.setFood(0);
            food_available-=food_required; //kangaroo consume the food in its pouch
        }
        else{
            point.setFood(point.getFood()-food_required);//kangaroo straightly consume the food of point
            takeFood();
        }
    }
    
     //kangaroo move point by point (BASIC FEATURES)
    public boolean moveByPoint(){
        Point destination=point;
        ArrayList<Path>path=new ArrayList<>();
        Path pathNode=point.getPathLink();
            
        while(pathNode!=null){
            if(ableToMove(pathNode)){
                if(pathNode.getPointLink().getFood()>destination.getFood()){//if more food at the next point
                    path.clear();
                    path.add(pathNode);
                    destination=pathNode.getPointLink();
                }
                 //else if food at next point is equal to the current point and more female at the next point
                else if(pathNode.getPointLink().getFood()==destination.getFood()&&pathNode.getPointLink().getFemale()>destination.getFemale()){
                    path.clear();
                    path.add(pathNode);
                    destination=pathNode.getPointLink();
                }
            }
            pathNode=pathNode.getPathLink();
        }
            
        if(destination!=point){
            movement(path);
            return true;
        }
        else
            return false;
    }
    
    //Is the kangaroo able to move to the next point through path
    public boolean ableToMove(Path pathNode){
        //No, if the next point is exceed or equal kangaroo limit
        if(pathNode.getPointLink().isFull())
            return false;
        //No, if the next point is colonized and the kangaroo does not have enough food as a gift to join them
        else if(pathNode.getPointLink().isColonised()){
            if(pathNode.getPointLink().getFood()+food_available<pathNode.getFoodRequired(food_available)+pathNode.getPointLink().getKangaroo().size())
                return false;
        }
        //No, if the pouch is empty and the next point has no food
        else if(pathNode.getRemainingFood(food_available)+food_available<0)
            return false;
        return true;
    }
    
    //extra feature 3 move with optimum 
    public boolean moveThroughMap(){
        ArrayList<Point>option=new ArrayList<>();
        //add all the point that has greater food than current point for checking
        for(int i=0;i<map.size();i++){
            if(map.get(i).getFood()>point.getFood())
                option.add(map.get(i));
        }
        sort(option);//sort the option points in descending order
        ArrayList<Path>path=new ArrayList<>();
        for(int i=0;i<option.size();i++){
            //get the best path to get the best destination point
            path=shortestPath(point,option.get(i));
            if(!path.isEmpty())
                break;// some destination point has no path to reach it
        }
        if(!path.isEmpty()){
            movement(path);// move to the best destination point along with the path in the list
            return true;
        }
        else
            return false;
    }
    
    public void sort(ArrayList<Point>option){
        //sort the option points with the worst choice end up with the best choice
        for(int i=0;i<option.size();i++)
            for(int j=0;j<option.size()-i-1;j++){
                if(option.get(j).getFood()>option.get(j+1).getFood()&&option.get(j).getFemale()>option.get(j+1).getFemale()){
                    Point temp=option.get(j);
                    option.set(j, option.get(j+1));
                    option.set(j+1, temp);
                }
                else if(option.get(j).getFood()>option.get(j+1).getFood()){
                    Point temp=option.get(j);
                    option.set(j, option.get(j+1));
                    option.set(j+1, temp);
                }
            }
        //reverse the sorted ascending option points to the descending order
        Collections.reverse(option);
    }
    
    ArrayList<ArrayList<Path>> pathList; 
    
    //looking for the shortest and the optimum path to the best destination
    public ArrayList<Path> shortestPath(Point from,Point to){
        ArrayList<Point> visited = new ArrayList<>(); 
        ArrayList<Path> travel=new ArrayList<>();
        pathList = new ArrayList<>();
          
        shortestPath(from, to, null, visited,travel); 
        return optimumPath();
    }
    
     //recursive method to return the shortest path
    public void shortestPath(Point from,Point to, Path pathNode, ArrayList<Point> visited,ArrayList<Path>travel) { 
        
        visited.add(from); //add the departed point to the visited list
          
        if (from==to){ //base case for the recursive method 
            pathList.add(new ArrayList<>());
            for(int i=0;i<travel.size();i++)
                //add the traveled point in to the pathList
                pathList.get(pathList.size()-1).add(travel.get(i));
            visited.remove(from); 
            return ; 
        } 
        
        pathNode=from.getPathLink();
        while(pathNode!=null){ 
            if (!visited.contains(pathNode.getPointLink())) { // check whether the visited list contain the point
                travel.add(pathNode); 
                //recurse to the same method with the new departed point(the next point)
                shortestPath(pathNode.getPointLink(), to, pathNode, visited, travel); 
                travel.remove(pathNode); 
            } 
            pathNode=pathNode.getPathLink();
        } 
        
        visited.remove(from); 
    } 
    
    //search for the optimum path
    public ArrayList<Path> optimumPath(){
        int food,totalFood,distance;
        int maxFood=-1,maxdistance=Integer.MIN_VALUE;
        boolean end=false;
        ArrayList<Path>optimum=new ArrayList<>();
        //searching for the optimum path to reach the best destination point
        for(ArrayList<Path>option:pathList){
            distance=option.size();
            totalFood=0;
            end=true;
            food=food_available;
            for(int i=0;i<option.size();i++){
                Path path=option.get(i);
                int remainingFood=path.getRemainingFood(food);
                
                if(i!=option.size()-1)
                    //if the point at the middle of the entire path is colonized or the kangaroo will be colonized
                    if(path.getPointLink().isColonised()||path.getPointLink().getKangaroo().size()==path.getPointLink().getColony_limit()-1){
                        end=false;//the kangaroo will not choose the path
                        break;
                    }
                //if the destination food is same with the current point and the kangaroo will obtain negative food in pouch and as the destination reached 
                if(path.getPointLink().getFood()==point.getFood()&&remainingFood<0&&food+remainingFood<0){
                    end=false;
                    break;
                }
                // the kangaroo will eat the food in the pouch
                else if(remainingFood<0&&food+remainingFood>0){
                    food+=remainingFood;
                    remainingFood=0;
                }
                //calculate the total food that obtain through the entire paths
                totalFood+=remainingFood;
            }
            if(end){
                // if the total food obtain is greater or equal with the current destination 
                //while having the shortest distance the option point will be the new destination 
                if(totalFood>maxFood||totalFood==maxFood&&distance<maxdistance){
                    maxdistance=distance;
                    maxFood=totalFood;
                    optimum=option;
                }
            }
        }
        return optimum;
    }
    
    //animation of movement
    public void movement(ArrayList<Path> path){
        map.mapdetails();// log details of each point
        for(int i=0;i<path.size();i++){
            if(colonised){
                logger.info("\nKangaroo is colonised at point "+point.getID());
                break;
            }
            logger.info("\nKangaroo is moving "+point.getID()+" to "+path.get(i).getPointLink().getID());
            Point destination=path.get(i).getPointLink();
             //calculate the total food required
            int food_required=path.get(i).getFoodRequired(food_available);
            if(destination.isColonised())
                 //increase food required as the gift of colonized
                food_required+=destination.getKangaroo().size();
            
            // if the kangaroo moves and the destination point add the kangaroo
            if(destination.addKangaroo(this)){
                move=true;
                point.removeKangaroo(this);
                point=destination;
                int destination_x=point.getX();
                int destination_y=point.getY()+150;

                int xchange=(destination_x-x)/60;
                int ychange=(destination_y-y)/60;
                left=x-point.getX()>0;
                while(true){
                    x+=xchange;
                    y+=ychange;

                    try{
                        Thread.sleep(80);
                    }catch(InterruptedException e){};

                    if((x-destination_x)<50&&(y-destination_y)<50)
                        break;
                }
                move=false;
                x=point.getX()+20+point.getKangaroo().size()*20;
                y=point.getY()+20+point.getKangaroo().size()*20;
                consumeFood(food_required);
                point.checkColonised(); //check is the point is colonized or not
            }
            try{
            Thread.sleep(400);
            }catch(InterruptedException e){};
            if(colonised)//the kangaroo will not moved if it is colonized
                break;
        }
        map.mapdetails();// log details of each point
    }
    
     // the kangaroo take the food from the point 
    public void takeFood(){
        if(food_available<food_limit){
            int get=food_limit-food_available; //the amount of food to fill the pouch till full
            if(point.getFood()>get){//if the food at point more than the food needed to get
                food_available+=get;
                point.setFood(point.getFood()-get);//remaining food at the point
            }
            else{
                food_available+=point.getFood(); //take all the food at that point to fill the pouch
                point.setFood(0);//no remaining food at the point
            }
        }
    }
    
    //display the image of kangaroo when move or stay at the point
    public void paint(Graphics g){
        if(move){
            if(left)
                g.drawImage(jumpLeft,x,height-y-75,null);
            else
                g.drawImage(jumpRight,x,height-y-75,null);
                
        }
        else{
            if(gender=='M')
                g.drawImage(stationaryM,x,height-y-75,null);
            else
                g.drawImage(stationaryF,x,height-y-75,null);
        }
    }
}
