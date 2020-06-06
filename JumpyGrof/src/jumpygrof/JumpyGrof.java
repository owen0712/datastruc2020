/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpygrof;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.Scanner;
import java.util.LinkedList;

/**
 *
 * @author USER
 */
public class JumpyGrof extends JPanel implements ActionListener{

    /**
     * @param args the command line arguments
     */
    static Scanner s=new Scanner(System.in);
    static Point<String,Integer> point=new Point();
    static LinkedList<Kangaroo>kangaroo=new LinkedList();
    Image background=new ImageIcon("background.jpg").getImage();
    static boolean run=false;
            
    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here
        String ans="";
        do{
        System.out.println("You want to input (enter i) or random generated (enter r) : ");
        ans=s.nextLine();
        if(ans.equalsIgnoreCase("i"))
            input();
        else if(ans.equalsIgnoreCase("r"))
            random();
        else
            System.out.println("Wrong option. Please enter again");
        }while(!ans.equalsIgnoreCase("i")&&!ans.equalsIgnoreCase("r"));
        
        new JumpyGrof();
        
        int colonized=0;
        while(true){
            colonized=0;
            int kangaroos=kangaroo.size();
            for(int i=0;i<kangaroo.size();i++){
                if(kangaroo.get(i).isColonised()){
                    colonized++;
                    kangaroos--;
                }
                else if(kangaroo.get(i).getGender()=='F')
                    kangaroos--;
                else
                    if(!kangaroo.get(i).moveByWholePoint())
                        kangaroos--;
            }
            if(kangaroos==0)
                break;
        }
        System.out.println("Number of colonies:"+point.getColonies());
        System.out.println("Number of remaining kangaroos:"+(kangaroo.size()-colonized));
        for(int i=0;i<kangaroo.size();i++)
            if(!kangaroo.get(i).isColonised())
                System.out.println(kangaroo.get(i).toString());
    }
    
    public static void input(){
        LinkedList<String>source=new LinkedList();
        LinkedList<String>destination=new LinkedList();
        LinkedList<Integer>obstacles=new LinkedList();
        
        System.out.print("Enter number of point : ");
        int loop=s.nextInt();
        for(int i=0;i<loop;i++){
            System.out.print("Enter ID of point : ");
            String ID=s.nextInt()+"";
            System.out.print("Enter food in point "+ID+" : ");
            int food=s.nextInt();
            System.out.print("Enter number of kangaroo limit in point "+ID+" : ");
            int kangaroo_limit=s.nextInt();
            point.addPoint(ID,food,kangaroo_limit);
            System.out.print("Enter number of path connect to point "+ID+" : ");
            int path=s.nextInt();
            for(int j=0;j<path;j++){
                source.add(ID);
                System.out.print("Enter the destination point ID : ");
                destination.add(s.nextInt()+"");
                System.out.print("Enter the height of obstacles : ");
                obstacles.add(s.nextInt());
            }
        }
        
        for(int i=0;i<source.size();i++){
            point.addPath(source.get(i), destination.get(i), obstacles.get(i));
        }
        
        System.out.print("Enter number of kangaroo : ");
        loop=s.nextInt();
        for(int i=0;i<loop;i++){
            System.out.print("Enter location of kangaroo : ");
            String location=s.nextInt()+"";
            System.out.print("Enter gender of kangaroo : ");
            char gender=s.next().charAt(0);
            System.out.print("Enter food limit in pouch of kangaroo : ");
            int food=s.nextInt();
            kangaroo.add(new Kangaroo(point.hasPoint(location),gender,food));
        }
        
        System.out.print("Enter the colony limit of point : ");
        int colony_limit=s.nextInt();
        point.setThreshold(colony_limit);
    }
    
    public static void random(){
        Random r=new Random();
        LinkedList<String>pointlist=new LinkedList();
        
        int loop=r.nextInt(20);
        for(int i=0;i<loop;i++){
            String ID=(r.nextInt(20)+1)+"";
            pointlist.add(ID);
            int food=r.nextInt(10)+10;
            int kangaroo_limit=r.nextInt(25);
            point.addPoint(ID,food,kangaroo_limit);
        }
        
        for(String ID:pointlist){
            int path=r.nextInt(pointlist.size());
            for(int j=0;j<path;j++)
                point.addPath(ID, pointlist.get(r.nextInt(pointlist.size())), r.nextInt(15)+1);
        }
        
        loop=r.nextInt(pointlist.size()*5);
        for(int i=0;i<loop;i++){
            String location=pointlist.get(r.nextInt(pointlist.size()));
            char gender=r.nextInt(2)>0?'M':'F';
            int food=r.nextInt(13);
            kangaroo.add(new Kangaroo(point.hasPoint(location),gender,food));
        }
        int colony_limit=r.nextInt(10);
        point.setThreshold(colony_limit);
    }
    
    public JumpyGrof(){
        Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
        int width=(int)screenSize.getWidth();
        int height =(int)screenSize.getHeight();
        JFrame frame=new JFrame();
        frame.setSize(width,height);
        frame.setTitle("JumpyGrof");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JLabel(setBackground(background)));
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setTitle("JumpyGrof");
        Timer t=new Timer(30,this);
        t.restart();
        frame.add(this);
        frame.setVisible(true);
    }
    
    @Override
    public void paint(Graphics g){
        super.paintComponent(g);
        g.drawImage(background,0,0,getWidth(),getHeight(),this);
        for(int i=0;i<point.getSize();i++)
            point.get(i).paint(g);
        for(int i=0;i<kangaroo.size();i++)
            kangaroo.get(i).paint(g);
    }

    public ImageIcon setBackground(Image img){
        Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
        int width=(int)screenSize.getWidth();
        int height = (int)screenSize.getHeight();
        ImageIcon ic = new ImageIcon(background);
        Image image = ic.getImage();
        Image newImage = image.getScaledInstance(width,height, java.awt.Image.SCALE_SMOOTH);
        ic = new ImageIcon(newImage);
        return ic;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
