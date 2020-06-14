package jumpygrof;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import static jumpygrof.JumpyGrof.logger;

public class Kangaroo{
    Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
    int width=(int)screenSize.getWidth();
    int height =(int)screenSize.getHeight();
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
            point.setFood(point.getFood()-food_limit);
        }
        else{
            food_available=point.getFood();
            point.setFood(0);
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
    
    public void consumeFood(int food_required){
        if(point.getFood()<food_required){
            food_required-=point.getFood();
            point.setFood(0);
            food_available-=food_required;
        }
        else{
            point.setFood(point.getFood()-food_required);
            takeFood();
        }
    }
    
    public boolean moveByPoint(){
        Point destination=point;
        ArrayList<Path>path=new ArrayList<>();
        Path pathNode=point.getPathLink();
            
        while(pathNode!=null){
            if(ableToMove(pathNode)){
                if(pathNode.getPointLink().getFood()>destination.getFood()){
                    path.clear();
                    path.add(pathNode);
                    destination=pathNode.getPointLink();
                }
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
    
    public boolean ableToMove(Path pathNode){
        if(pathNode.getPointLink().isFull())
            return false;
        else if(pathNode.getPointLink().isColonised()){
            if(pathNode.getPointLink().getFood()+food_available<pathNode.getFoodRequired(food_available)+pathNode.getPointLink().getKangaroo().size())
                return false;
        }
        else if(pathNode.getRemainingFood(food_available)+food_available<0)
            return false;
        return true;
    }
    
    //extra feature 3
    public boolean moveThroughMap(){
        ArrayList<Point>option=new ArrayList<>();
        for(int i=0;i<map.size();i++){
            if(map.get(i).getFood()>point.getFood())
                option.add(map.get(i));
        }
        sort(option);
        ArrayList<Path>path=new ArrayList<>();
        for(int i=0;i<option.size();i++){
            path=shortestPath(point,option.get(i));
            if(!path.isEmpty())
                break;
        }
        if(!path.isEmpty()){
            movement(path);
            return true;
        }
        else
            return false;
    }
    
    public void sort(ArrayList<Point>option){
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
        Collections.reverse(option);
    }
    
    ArrayList<ArrayList<Path>> pathList; 
    
    public ArrayList<Path> shortestPath(Point from,Point to){
        ArrayList<Point> visited = new ArrayList<>(); 
        ArrayList<Path> travel=new ArrayList<>();
        pathList = new ArrayList<>();
          
        shortestPath(from, to, null, visited,travel); 
        return optimumPath();
    }
    
    public void shortestPath(Point from,Point to, Path pathNode, ArrayList<Point> visited,ArrayList<Path>travel) { 
        
        visited.add(from); 
          
        if (from==to){ 
            pathList.add(new ArrayList<>());
            for(int i=0;i<travel.size();i++)
                pathList.get(pathList.size()-1).add(travel.get(i));
            visited.remove(from); 
            return ; 
        } 
        
        pathNode=from.getPathLink();
        while(pathNode!=null){ 
            if (!visited.contains(pathNode.getPointLink())) { 
                travel.add(pathNode); 
                shortestPath(pathNode.getPointLink(), to, pathNode, visited, travel); 
                
                travel.remove(pathNode); 
            } 
            pathNode=pathNode.getPathLink();
        } 
        
        visited.remove(from); 
    } 
    
    public ArrayList<Path> optimumPath(){
        int food,totalFood,distance;
        int maxFood=-1,maxdistance=Integer.MIN_VALUE;
        boolean end=false;
        ArrayList<Path>optimum=new ArrayList<>();
        for(ArrayList<Path>option:pathList){
            distance=option.size();
            totalFood=0;
            end=true;
            food=food_available;
            for(Path path:option){
                int remainingFood=path.getRemainingFood(food);
                
                if(path.getPointLink().getFood()==point.getFood()&&remainingFood<0&&food+remainingFood<0){
                    end=false;
                    break;
                }
                else if(remainingFood<0&&food+remainingFood>0){
                    food+=remainingFood;
                    remainingFood=0;
                }
                
                totalFood+=remainingFood;
            }
            if(end){
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
        for(int i=0;i<path.size();i++){
            map.mapdetails();
            if(colonised){
                logger.info("\nKangaroo is colonised at point "+point.getID());
                break;
            }
            logger.info("\nKangaroo is moving "+point.getID()+" to "+path.get(i).getPointLink().getID());
            Point destination=path.get(i).getPointLink();
            int food_required=path.get(i).getFoodRequired(food_available);
            if(destination.isColonised())
                food_required+=destination.getKangaroo().size();
            
            if(destination.addKangaroo(this)){
                move=true;
                point.removeKangaroo(this);
                point=destination;
                int destination_x=point.getX();
                int destination_y=point.getY()+150;

                int xchange=(Math.abs(destination_x-x)>100)?(destination_x-x)/60:(destination_x-x)/80;
                int ychange=(Math.abs(destination_y-y)>90)?(destination_y-y)/60:(destination_y-y)/80;
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
                point.checkColonised();
            }
            try{
            Thread.sleep(200);
            }catch(InterruptedException e){};
            if(colonised)
                break;
        }
    }
    
    public void takeFood(){
        if(food_available<food_limit){
            int get=food_limit-food_available;
            if(point.getFood()>get){
                food_available+=get;
                point.setFood(point.getFood()-get);
            }
            else{
                food_available+=point.getFood();
                point.setFood(0);
            }
        }
    }
    
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