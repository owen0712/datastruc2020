/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpygrof;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.*;

/**
 *
 * @author USER
 */
public class Kangaroo{
    Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
    int width=(int)screenSize.getWidth();
    int height =(int)screenSize.getHeight();
    private PointNode point;
    private int food_limit,food_available,x,y;
    private char gender;
    private boolean colonised,move,left;
    private Random r;
    private final Image stationaryF = new ImageIcon("Standing facing front with baby.png").getImage();
    private final Image stationaryM = new ImageIcon("Standing looking back.png").getImage();
    private final Image jumpLeft = new ImageIcon("Jumping left.gif").getImage();
    private final Image jumpRight = new ImageIcon("Jumping right.gif").getImage();
    private ArrayList<Path>path;

    public Kangaroo(PointNode point, char gender, int food_limit) {
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
        int food_required=0;
        PointNode destination=point;
        Path pathNode=point.getPathLink();
        while(pathNode!=null){
            if(!ableToMove(pathNode)){
                pathNode=pathNode.getPathLink();
                continue;
            }
            
            if(pathNode.getRemainingFood(this)>destination.getFood()){
                food_required=pathNode.getFoodRequired(this);
                destination=pathNode.getPointLink();
            }
            else if(pathNode.getRemainingFood(this)==destination.getFood()&&pathNode.getPointLink().getFemale()>destination.getFemale()){
                food_required=pathNode.getFoodRequired(this);
                destination=pathNode.getPointLink();
            }
            else{
                if(point.getFood()<=0&&pathNode.getRemainingFood(this)+food_available>0){
                    food_required=pathNode.getFoodRequired(this);
                    destination=pathNode.getPointLink();
                }
            }
            pathNode=pathNode.getPathLink();
        }
        if(destination!=point){
            movement(destination,food_required);
            return true;
        }
        else
            return false;
    }
    
    public boolean ableToMove(Path pathNode){
        if(pathNode.getPointLink().getFood()<=point.getFood()&&pathNode.getPointLink().getFemale()<=point.getFemale()||pathNode.getPointLink().isFull())
            return false;
        else if(pathNode.getPointLink().isColonised()){
            if(pathNode.getPointLink().getFood()+food_available<pathNode.getFoodRequired(this)+pathNode.getPointLink().getKangaroo().size())
                return false;
        }
        else if(pathNode.getPointLink().getFood()+food_available<pathNode.getFoodRequired(this))
            return false;
        return true;
    }
    
    public boolean moveByWholePoint(){
        PointNode destination=point;
        Path pathNode=point.getPathLink();
        while(pathNode!=null){
            if(!ableToMove(pathNode)){
                pathNode=pathNode.getPathLink();
                continue;
            }
            
            if(pathNode.getRemainingFood(this)>destination.getFood()){
                destination=pathNode.getPointLink();
            }
            else if(pathNode.getRemainingFood(this)==destination.getFood()&&pathNode.getPointLink().getFemale()>destination.getFemale()){
                destination=pathNode.getPointLink();
            }
            else{
                if(point.getFood()<=0&&pathNode.getRemainingFood(this)+food_available>0){
                    destination=pathNode.getPointLink();
                }
            }
            pathNode=pathNode.getPathLink();
        }
        PointNode currentNode=point.getHead();
        boolean isPath=false;
        while(currentNode!=null){
            if(currentNode!=point){
                pathNode=point.getPathLink();
                while(pathNode!=null){
                    isPath = pathNode.getPointLink()!=currentNode;
                    if(!isPath)
                        break;
                    pathNode=pathNode.getPathLink();
                }
                if(!isPath)
                    if(currentNode.getFood()>destination.getFood())
                        destination=currentNode;
                    else if(currentNode.getFood()>destination.getFood()&&currentNode.getFemale()>=destination.getFemale())
                        destination=currentNode;
            }
            currentNode=currentNode.getPointLink();
        }
        if(destination!=point){
            path=new ArrayList<>();
            computePath(point);
            getShortestPath(destination);
            if(!path.isEmpty())
                wholeMovement();
            return true;
        }
        else
            return false;
    }
    
    public void computePath(PointNode from){
        from.setMinDistance(0);
        Queue<PointNode>queue=new LinkedList<>();
        queue.add(from);
        while(!queue.isEmpty()){
            PointNode previous=queue.poll();
            Path pathNode=previous.getPathLink();
            while(pathNode!=null){
                PointNode next=pathNode.getPointLink();
                int height=(Integer)pathNode.getObstacle_height();
                int total_distance=(Integer)previous.getMinDistance()+height;
                if(total_distance<next.getMinDistance()){
                    queue.remove(pathNode.getPointLink());
                    next.setMinDistance(total_distance);
                    previous.setNextpath(pathNode);
                    queue.add(next);
                }
                pathNode=pathNode.getPathLink();
            }
        }
    }
    
    public void getShortestPath(PointNode to){
        ArrayList<Path>selected_path=new ArrayList();
        for(Path location=point.getNextpath();location!=null;location=location.getPointLink().getNextpath())
            selected_path.add(location);
        path=selected_path;
    }
    
    public void wholeMovement(){
        int g=10,u=-5;
        double t=0.5;
        for(int i=0;i<path.size();i++){
            PointNode destination=path.get(i).getPointLink();
            int food_required=path.get(i).getFoodRequired(this);
            if(destination.isColonised())
                food_required+=destination.getKangaroo().size();
            
            //if kangaroo can't take its point food,then like this
            while(destination.getFood()+food_available<food_required){System.out.println("Waiting");}
            
            if(destination.addKangaroo(this)){
                boolean jump=true;
                move=true;
                point.removeKangaroo(this);
                point=destination;
                int destination_x=point.getX()+20+point.getKangaroo().size()*20;
                int destination_y=point.getY()+20+point.getKangaroo().size()*20;

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
            if(colonised)
                break;
        }
    }
    
    public void movement(PointNode destination,int food_required){
        System.out.println("Moving from "+point.getID()+" to "+destination.getID());
        int g=10,u=-10;
        double t=0.5;
        if(destination.addKangaroo(this)){
            boolean jump=true;
            move=true;
            point.removeKangaroo(this);
            point=destination;
            int destination_x=point.getX()+20+point.getKangaroo().size()*20;
            int destination_y=point.getY()+20+point.getKangaroo().size()*20;
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
            x=destination_x;
            y=destination_y;
            consumeFood(food_required);
            point.checkColonised();
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
                g.drawImage(jumpLeft,x+100,height-y-75,null);
            else
                g.drawImage(jumpRight,x+100,height-y-75,null);
                
        }
        else{
            if(gender=='M')
                g.drawImage(stationaryM,x+100,height-y-75,null);
            else
                g.drawImage(stationaryF,x+100,height-y-75,null);
        }
    }
}