package jumpygrof;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Path<E>{
    private Point source;
    private Point pointLink;
    private Path pathLink;
    private E obstacle_height;
    private boolean back;
    private final Image low = new ImageIcon("image\\Low.png").getImage();
    private final Image middle = new ImageIcon("image\\Middle.png").getImage();
    private final Image high = new ImageIcon("image\\High.png").getImage();

    public Path(Point pointLink, E obstacle_height, Path pathLink,boolean back) {
        this.pointLink = pointLink;
        this.pathLink = pathLink;
        this.obstacle_height = obstacle_height;
        this.back=back;
    }

    public String getSource() {return (String)source.getID();}

    public void setSource(Point source) {this.source = source;}    

    public Point getPointLink() {return pointLink;}

    public void setPointLink(Point pointLink) {this.pointLink = pointLink;}

    public Path getPathLink() {return pathLink;}

    public void setPathLink(Path pathLink) {this.pathLink = pathLink;}

    public E getObstacle_height() {return obstacle_height;}
    
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
    
    // calculation of food consuming when going through one point to the other
    public int getFoodRequired(int foodInPouch){return (int)((Integer)obstacle_height+0.5*foodInPouch);}
    
    //food remaining in point
    public int getRemainingFood(int foodInPouch){return pointLink.getFood()-getFoodRequired(foodInPouch);}
    
    //get the destination ID used for GUI purpose
    public String getDestination(){return (String)pointLink.getID();}
}
