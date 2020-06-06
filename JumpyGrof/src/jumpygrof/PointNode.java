/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpygrof;

/**
 *
 * @author USER
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;

public class PointNode<V,E> implements ActionListener{
    Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
    int width=(int)screenSize.getWidth();
    int height =(int)screenSize.getHeight();
    private V ID;
    private int food,kangaroo_limit,x,y,female,colony_limit,colony_food,time,food_limit;
    private int minDistance;
    private PointNode pointLink,head;
    private Path pathLink,nextpath;
    private boolean depleted,colonised;
    private Random r;
    private LinkedList<Kangaroo>kangaroo;
    private final Image available_point=new ImageIcon("available_point.png").getImage();
    private final Image depleted_point=new ImageIcon("depleted_point.png").getImage();
    private javax.swing.Timer timer;

    public PointNode(V ID, int food,int kangaroo_limit){
        this.ID=ID;
        this.food=food;
        this.food_limit=food;
        this.kangaroo_limit=kangaroo_limit;
        depleted=false;
        colonised=false;
        r=new Random();
        kangaroo=new LinkedList();
        female=0;
        colony_food=0;
        setCoordinate();
        timer=new javax.swing.Timer(5,this);
        timer.restart();
        minDistance=Integer.MAX_VALUE;
        nextpath=null;
    }
    
    public void setCoordinate(){
        x=r.nextInt(width-225);
        y=r.nextInt(height-225);
    }

    public V getID() {return ID;}

    public void setID(V ID) {this.ID = ID;}

    public int getFood() {return food;}

    public void setFood(int food) {
        this.food = food;
        if(food<=0)
            depleted=true;
    }

    public int getFemale() {return female;}

    public int getX() {return x;}

    public int getY() {return y;}

    public Path getPathLink() {return pathLink;}

    public void setPathLink(Path pathLink) {this.pathLink = pathLink;}

    public boolean isDepleted() {return depleted;}

    public void setDepleted(boolean depleted) {this.depleted = depleted;}

    public LinkedList<Kangaroo> getKangaroo() {return kangaroo;}

    public PointNode getPointLink() {return pointLink;}

    public void setPointLink(PointNode pointLink) {this.pointLink = pointLink;}    

    public void setColony_limit(int colony_limit) {this.colony_limit = colony_limit;}

    public boolean isColonised() {return colonised;}

    public PointNode getHead() {return head;}

    public void setHead(PointNode head) {this.head = head;}

    public int getMinDistance() {return minDistance;}

    public void setMinDistance(int minDistance) {this.minDistance = minDistance;}

    public Path getNextpath() {return nextpath;}

    public void setNextpath(Path nextpath) {this.nextpath = nextpath;}
    
    public boolean addKangaroo(Kangaroo newKangaroo){
        if(kangaroo.size()<=kangaroo_limit){
            kangaroo.add(newKangaroo);
            if(newKangaroo.getGender()=='F')
                female++;
            return true;
        }
        return false;
    }
    
    public boolean isFull(){
        return kangaroo.size()>=kangaroo_limit;
    }
    
    public void removeKangaroo(Kangaroo leftKangaroo){kangaroo.remove(leftKangaroo);}

    public int getColonyLimit() {return colony_limit;}
    
    public E getObstacles(V to){
        Path pathNode=(Path)getPathLink();
        while(pathNode!=null){
            if(pathNode.getPointLink().getID()==to)
                return(E)pathNode.getObstacle_height();
            pathNode=pathNode.getPathLink();
        }
        return null;
    }
    
    public void checkColonised(){
        if(kangaroo.size()>=colony_limit){
            for(int i=0;i<kangaroo.size();i++){
                kangaroo.get(i).setColonised(true);
                colony_food+=kangaroo.get(i).getFoodAvailable();
                kangaroo.get(i).setFoodAvailable(0);
            }
            colonised=true;
        }
    }
    
    public void paint(Graphics g){
        g.setFont(new Font("TimesRoman",Font.PLAIN,36));
        Path pathNode=pathLink;
        while(pathNode!=null){
            int middle_x=(x+pathNode.getPointLink().getX())/2;
            int middle_y=(y+pathNode.getPointLink().getY())/2;
            if(!pathNode.isBack()){
                g.drawLine(x+150, height-y-70, pathNode.getPointLink().getX()+150, height-pathNode.getPointLink().getY()-70);
                g.drawImage(pathNode.getObstacle_image(),middle_x+125,height-middle_y-85,null);
                g.drawString(pathNode.getObstacle_height().toString(),middle_x+125,height-middle_y-85);
            }
            else{
                g.drawLine(x+200, height-y-80, pathNode.getPointLink().getX()+200, height-pathNode.getPointLink().getY()-80);
                g.drawImage(pathNode.getObstacle_image(),middle_x+175,height-middle_y-95,null);
                g.drawString(pathNode.getObstacle_height().toString(),middle_x+175,height-middle_y-95);
            }
            pathNode=pathNode.getPathLink();
        }
        
        if(!depleted)
            g.drawImage(available_point,x+100,height-y-150,null);
        else
            g.drawImage(depleted_point,x+100,height-y-150,null);
        
        g.setFont(new Font("TimesRoman",Font.PLAIN,72));
        g.drawString(ID.toString(), x+150, height-y-50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        time++;
        if(time%100==0&&food<=food_limit)
            food++;
        if(food>0)
            depleted=false;
    }
}
