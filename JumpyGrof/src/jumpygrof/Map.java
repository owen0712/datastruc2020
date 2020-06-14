package jumpygrof;

import java.util.*;
import static jumpygrof.JumpyGrof.logger;

public class Map<V extends Comparable<V>,E>{
    private Point head;
    private Random r;

    public Map(){head=null;}
    
    public boolean isEmpty(){return head==null;}
    
    public int size(){
        int count=0;
        Point currentNode=head;
        while(currentNode!=null){
            currentNode=currentNode.getPointLink();
            count++;
        }
        return count;
    }
    
    public void clear(){head=null;}
    
    public Point hasPoint(V ID){
        Point currentNode=head;
        if(isEmpty())
            return null;
        while(currentNode!=null){
            if(ID.compareTo((V)currentNode.getID())==0)
                return currentNode;
            currentNode=currentNode.getPointLink();
        }
        return null;
    }
    
    public Point addPoint(String ID, int food,int limit,int path){
        Point newNode=new Point(ID,food,limit,path);
        if(head==null)
            head=newNode;
        else{
            Point currentNode=head;
            while(currentNode.getPointLink()!=null)
                currentNode=currentNode.getPointLink();
            currentNode.setPointLink(newNode);
        }
        checkOverlapped(newNode);
        return newNode;
    }
    
    //addPath method for basic feature
    public Path addPath(V from,V to,E obstacles_height,boolean back){
        r=new Random();
        if(hasPoint(from)==null||hasPoint(to)==null||!back&&isPath(to,from)){
            System.out.println("Invalid path");
            return null;
        }
        else{
            Point currentNode=head;
            while(currentNode!=null){
                if(from.compareTo((V)currentNode.getID())==0){
                    Point temp=hasPoint(to);
                    Path newNode=new Path(temp,obstacles_height,null,back);
                    Path pathNode=(Path)currentNode.getPathLink();
                    if(pathNode==null)
                        currentNode.setPathLink(newNode);
                    else{
                        while(pathNode.getPathLink()!=null)
                            pathNode=pathNode.getPathLink();
                        pathNode.setPathLink(newNode);
                    }
                    if(!back)
                        addPath(to,from,obstacles_height,true);
                    return newNode;
                }
                else
                    currentNode=currentNode.getPointLink();
            }
        }
        return null;
    }
    
    //addPath method for extra feature 2
    public Path addPath(V from,V to,E obstacles_height){
        if(hasPoint(from)==null||hasPoint(to)==null){
            System.out.println("Invalid path");
            return null;
        }
        else if(isPath(to,from))
            if(hasPath(to,from).getObstacle_height().equals(obstacles_height))
                System.out.println("Cannot be same height");
        else if(isPath(from,to)&&isPath(to,from))
            System.out.println("The path is full");
        else if(isPath(from,to))
            System.out.println("The path is existed");
        else{
            Point currentNode=head;
            while(currentNode!=null){
                if(from.compareTo((V)currentNode.getID())==0){
                    Point temp=hasPoint(to);
                    Path newNode=isPath(to,from)?new Path(temp,obstacles_height,null,true):new Path(temp,obstacles_height,null,false);
                    Path pathNode=(Path)currentNode.getPathLink();
                    if(pathNode==null){
                        currentNode.setPathLink(newNode);
                        newNode.setSource(currentNode);
                    }
                    else{
                        while(pathNode.getPathLink()!=null)
                            pathNode=pathNode.getPathLink();
                        pathNode.setPathLink(newNode);
                        newNode.setSource(currentNode);
                    }
                    return newNode;
                }
                else
                    currentNode=currentNode.getPointLink();
            }
        }
        return null;
    }
    
    public boolean isPath(V from,V to){
        if(hasPoint(from)==null||hasPoint(to)==null)
            return false;
        else{
            Point currentNode=head;
            while(currentNode!=null){
                if(from.compareTo((V)currentNode.getID())==0){
                    Point temp=hasPoint(to);
                    Path pathNode=currentNode.getPathLink();
                    if(pathNode==null)
                        return false;
                    else
                        while(pathNode!=null){
                            if(pathNode.getPointLink()==temp)
                                return true;
                            pathNode=pathNode.getPathLink();
                        }
                    break;
                }
                currentNode=currentNode.getPointLink();
            }
        }
        return false;
    }
    
    public Path hasPath(V from,V to){
        if(hasPoint(from)==null||hasPoint(to)==null)
            return null;
        else{
            Point currentNode=head;
            while(currentNode!=null){
                if(from.compareTo((V)currentNode.getID())==0){
                    Point temp=hasPoint(to);
                    Path pathNode=currentNode.getPathLink();
                    if(pathNode==null)
                        return null;
                    else
                        while(pathNode!=null){
                            if(pathNode.getPointLink()==temp)
                                return pathNode;
                            pathNode=pathNode.getPathLink();
                        }
                    break;
                }
                currentNode=currentNode.getPointLink();
            }
        }
        return null;
    }
    
    public void setThreshold(int threshold){
        Point currentNode=head;
        while(currentNode!=null){
            currentNode.setColony_limit(threshold);
            currentNode=currentNode.getPointLink();
        }
    }
    
    public int getColonies(){
        int colonies=0;
        Point currentNode=head;
        while(currentNode!=null){
            if(currentNode.isColonised())
                colonies++;
            currentNode=currentNode.getPointLink();
        }
        return colonies;
    }
    
    public Point get(int index){
        if(index==0)
            return head;
        else{
            Point currentNode=head;
            for(int i=1;i<=index;i++)
                currentNode=currentNode.getPointLink();
            return currentNode;
        }
    }
    
    public void checkOverlapped(Point checkNode){
        Point currentNode=head;
        boolean separated=true;
        while(currentNode!=null){
            if(currentNode!=checkNode){
                separated=((int)Math.sqrt(Math.pow(checkNode.getX()-currentNode.getX(),2)+Math.pow(checkNode.getY()-currentNode.getY(),2)))>300;
                if(!separated){
                    checkNode.setCoordinate();
                    currentNode=head;
                    continue;
                }
            }
            currentNode=currentNode.getPointLink();
        }
    }
    
    
    //log the details of each point
    public void mapdetails(){
        Point currentNode=head;
        while(currentNode!=null){
            LinkedList<Kangaroo>kangaroo=currentNode.getKangaroo();
            String detail="\n***********************"+"\nPoint : "+currentNode.getID()+"\nFood : "+currentNode.getFood()+"\nNumber of kangaroo : "+kangaroo.size();
            for(int i=0;i<kangaroo.size();i++)
                detail+="\n    "+kangaroo.get(i).getGender()+" "+kangaroo.get(i).getFoodAvailable();
            detail+="\nColony : "+currentNode.isColonised()+"\nPath : ";
            Path pathNode=currentNode.getPathLink();
            while(pathNode!=null){
                detail+="\n    "+pathNode.getPointLink().getID()+" "+pathNode.getObstacle_height();
                pathNode=pathNode.getPathLink();
        }
            detail+="\n***********************";
            logger.info(detail);
            currentNode=currentNode.getPointLink();
        }
    }
}
