package jumpygrof;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Point<V,E> implements ActionListener{
    private Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
    private int width=(int)screenSize.getWidth();
    private int height =(int)screenSize.getHeight();
    private V ID;
    private final int kangaroo_limit,food_limit;
    private int food,x,y,female,colony_food,colony_limit,time,path;
    private Point pointLink;
    private Path pathLink;
    private boolean depleted,colonised;
    private Random r;
    private LinkedList<Kangaroo>kangaroo;
    private final Image available_point=new ImageIcon("image\\available_point.png").getImage();
    private final Image depleted_point=new ImageIcon("image\\depleted_point.png").getImage();
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

    public LinkedList<Kangaroo> getKangaroo() {return kangaroo;}

    public Point getPointLink() {return pointLink;}

    public void setPointLink(Point pointLink) {this.pointLink = pointLink;}    

    public void setColony_limit(int colony_limit) {this.colony_limit = colony_limit;}

    public boolean isColonised() {return colonised;}
    
    public boolean addKangaroo(Kangaroo newKangaroo){
        if(kangaroo.size()<=kangaroo_limit){
            kangaroo.add(newKangaroo);
            if(newKangaroo.getGender()=='F')
                female++;
            return true;
        }
        return false;
    }
    
    public boolean isFull(){return kangaroo.size()>=kangaroo_limit;}
    
    public void removeKangaroo(Kangaroo leftKangaroo){kangaroo.remove(leftKangaroo);}
    
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
    
    //change to this paint method to this for extra feature 2
    /*public void paint(Graphics g){
        g.setFont(new Font("TimesRoman",Font.PLAIN,36));
        Path pathNode=pathLink;
        while(pathNode!=null){
            int middle_x=(x+pathNode.getPointLink().getX())/2;
            int middle_y=(y+pathNode.getPointLink().getY())/2;
            if(!pathNode.isBack()){
                g.drawLine(x+50, height-y-70, pathNode.getPointLink().getX()+50, height-pathNode.getPointLink().getY()-70);
                g.drawImage(pathNode.getObstacle_image(),middle_x+25,height-middle_y-85,null);
                g.drawString(pathNode.getObstacle_height().toString(),middle_x+25,height-middle_y-85);
            }
            else{
                g.drawLine(x+100, height-y-80, pathNode.getPointLink().getX()+100, height-pathNode.getPointLink().getY()-80);
                g.drawImage(pathNode.getObstacle_image(),middle_x+75,height-middle_y-95,null);
                g.drawString(pathNode.getObstacle_height().toString(),middle_x+75,height-middle_y-95);
            }
            pathNode=pathNode.getPathLink();
        }
        
        if(!depleted)
            g.drawImage(available_point,x,height-y-150,null);
        else
            g.drawImage(depleted_point,x,height-y-150,null);
        
        g.setFont(new Font("TimesRoman",Font.PLAIN,72));
        g.drawString(ID.toString(), x+50, height-y-50);
    }*/
    
    //change to this paint method for basic feature
    public void paint(Graphics g){
        g.setFont(new Font("TimesRoman",Font.PLAIN,36));
        Path pathNode=pathLink;
        while(pathNode!=null){
            int middle_x=(x+pathNode.getPointLink().getX())/2;
            int middle_y=(y+pathNode.getPointLink().getY())/2;
            if(!pathNode.isBack()){
                g.drawLine(x+50, height-y-70, pathNode.getPointLink().getX()+50, height-pathNode.getPointLink().getY()-70);
                g.drawImage(pathNode.getObstacle_image(),middle_x,height-middle_y-85,null);
                g.drawString(pathNode.getObstacle_height().toString(),middle_x,height-middle_y-85);
            }
            pathNode=pathNode.getPathLink();
        }
        
        if(!depleted)
            g.drawImage(available_point,x,height-y-150,null);
        else
            g.drawImage(depleted_point,x,height-y-150,null);
        
        g.setFont(new Font("TimesRoman",Font.PLAIN,72));
        g.drawString(ID.toString(), x+50, height-y-50);
    }

    //Uncomment the code in below method for extra feature 4
    @Override
    public void actionPerformed(ActionEvent e) {
        /*time++;
        if(time%100==0&&food<=food_limit)
            food++;
        if(food>0)
            depleted=false;*/
    }

}
