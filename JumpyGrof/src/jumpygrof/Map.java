/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpygrof;

/**
 *
 * @author USER
 * @param <V>
 */
import java.util.*;

public class Map<V extends Comparable<V>,E>{
    private Point head;
    private Random r;

    public Map(){head=null;}
    
    public boolean isEmpty(){return head==null;}
    
    public int getSize(){
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
    
    public void addPoint(String ID, int food,int limit){
        Point newNode=new Point(ID,food,limit);
        if(head==null)
            head=newNode;
        else{
            Point currentNode=head;
            while(currentNode.getPointLink()!=null)
                currentNode=currentNode.getPointLink();
            currentNode.setPointLink(newNode);
        }
        checkOverlapped(newNode);
        newNode.setHead(head);
    }
    
    /*public boolean addPath(V from,V to,E obstacles_height,boolean back){
        r=new Random();
        if(hasPoint(from)==null||hasPoint(to)==null||isPath(to,from)){
            System.out.println("Invalid path");
            return false;
        }
        else{
            PointNode currentNode=head;
            while(currentNode!=null){
                if(from.compareTo((V)currentNode.getID())==0){
                    PointNode temp=hasPoint(to);
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
                        addPath(to,from,(E)((Integer)(Integer.parseInt(obstacles_height.toString())+r.nextInt(3))),true);
                    return true;
                }
                else
                    currentNode=currentNode.getPointLink();
            }
        }
        return false;
    }*/
    
    public boolean addPath(V from,V to,E obstacles_height){
        if(hasPoint(from)==null||hasPoint(to)==null){
            System.out.println("Invalid path");
            return false;
        }
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
                    if(pathNode==null)
                        currentNode.setPathLink(newNode);
                    else{
                        while(pathNode.getPathLink()!=null)
                            pathNode=pathNode.getPathLink();
                        pathNode.setPathLink(newNode);
                    }
                    return true;
                }
                else
                    currentNode=currentNode.getPointLink();
            }
        }
        return false;
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
    
    public int getColonyLimit(){return head.getColonyLimit();}
    
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
    
    
}
