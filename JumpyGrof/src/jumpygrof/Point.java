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
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Point<V,E> implements ActionListener{
    Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
    int width=(int)screenSize.getWidth();
    int height =(int)screenSize.getHeight();
    private V ID;
    private int food,kangaroo_limit,x,y,female,colony_limit,colony_food,time,food_limit,path;
    private Point pointLink,head;
    private Path pathLink;
    private boolean depleted,colonised;
    private Random r;
    private LinkedList<Kangaroo>kangaroo;
    private final Image available_point=new ImageIcon("available_point.png").getImage();
    private final Image depleted_point=new ImageIcon("depleted_point.png").getImage();
    private javax.swing.Timer timer;

    public Point(V ID, int food,int kangaroo_limit,int path){
        this.ID=ID;
        this.food=food;
        this.food_limit=food;
        this.kangaroo_limit=kangaroo_limit;
        this.path=path;
        depleted=false;
        colonised=false;
        r=new Random();
        kangaroo=new LinkedList();
        female=0;
        colony_food=0;
        setCoordinate();
        timer=new javax.swing.Timer(5,this);
        timer.restart();
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

    public int getPath() {return path;}

    public void setPath(int path) {this.path = path;}
    
    public int getFemale() {return female;}

    public int getX() {return x;}

    public int getY() {return y;}

    public Path getPathLink() {return pathLink;}

    public void setPathLink(Path pathLink) {this.pathLink = pathLink;}

    public boolean isDepleted() {return depleted;}

    public void setDepleted(boolean depleted) {this.depleted = depleted;}

    public LinkedList<Kangaroo> getKangaroo() {return kangaroo;}

    public int getKangaroo_limit() {return kangaroo_limit;}

    public Point getPointLink() {return pointLink;}

    public void setPointLink(Point pointLink) {this.pointLink = pointLink;}    

    public void setColony_limit(int colony_limit) {this.colony_limit = colony_limit;}

    public boolean isColonised() {return colonised;}

    public Point getHead() {return head;}

    public void setHead(Point head) {this.head = head;}
    
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
    
    public void pointdetails(){
        System.out.println("***********************");
        System.out.println("Point : "+ID);
        System.out.println("Food : "+food);
        System.out.println("Number of kangaroo : "+kangaroo.size());
        for(int i=0;i<kangaroo.size();i++){
            System.out.println("    "+kangaroo.get(i).getGender()+" "+kangaroo.get(i).getFoodAvailable());
        }
        System.out.println("Colony : "+colonised);
        System.out.println("Path : ");
        while(pathLink!=null){
            System.out.println("    "+pathLink.getPointLink().getID()+" "+pathLink.getObstacle_height());
            pathLink=pathLink.getPathLink();
        }
        System.out.println("***********************");
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
        /*time++;
        if(time%100==0&&food<=food_limit)
            food++;
        if(food>0)
            depleted=false;*/
    }

}
