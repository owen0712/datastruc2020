/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpygrof;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author USER
 */
public class Path<E>{
    private PointNode pointLink;
    private Path pathLink;
    private E obstacle_height;
    private boolean back;
    private final Image low = new ImageIcon("Low.png").getImage();
    private final Image middle = new ImageIcon("Middle.png").getImage();
    private final Image high = new ImageIcon("High.png").getImage();
    private Path previous;

    public Path() {
        pointLink=null;
        pathLink=null;
        obstacle_height=null;
    }

    public Path(PointNode pointLink, E obstacle_height, Path pathLink,boolean back) {
        this.pointLink = pointLink;
        this.pathLink = pathLink;
        this.obstacle_height = obstacle_height;
        this.back=back;
    }

    public PointNode getPointLink() {return pointLink;}

    public void setPointLink(PointNode pointLink) {this.pointLink = pointLink;}

    public Path getPathLink() {return pathLink;}

    public void setPathLink(Path pathLink) {this.pathLink = pathLink;}

    public E getObstacle_height() {return obstacle_height;}

    public void setObstacle_height(E obstacle_height) {this.obstacle_height = obstacle_height;}
    
    public Image getObstacle_image(){
        if((Integer)obstacle_height<3)
            return low;
        else if((Integer)obstacle_height<6)
            return middle;
        else
            return high;
    }

    public boolean isBack() {return back;}

    public void setBack(boolean back) {this.back = back;}
    
    public int getFoodRequired(Kangaroo kangaroo){
        return (int)((Integer)obstacle_height+0.5*kangaroo.getFoodAvailable());
    }
    
    public int getRemainingFood(Kangaroo kangaroo){
        return pointLink.getFood()-getFoodRequired(kangaroo);
    }
}
