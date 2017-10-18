import java.awt.Color;
import java.awt.Graphics;
/**
 * A rectangular container box, containing the bouncing ball.  
 * 
 * @author Hock-Chuan Chua
 * @version October 2010
 */
public class ContainerCircle {
   int centerX, centerY, radius;
   private Color colorFilled;   // Box's filled color (background)
   private Color colorBorder;   // Box's border color
   private static final Color DEFAULT_COLOR_FILLED = Color.BLACK;
   private static final Color DEFAULT_COLOR_BORDER = Color.YELLOW;
   
   /** Constructors */
   public ContainerCircle(int centerX, int centerY, int radius, Color colorFilled, Color colorBorder) {
      this.centerX = centerX;
      this.centerY = centerY;
      this.radius = radius;
      this.colorFilled = colorFilled;
      this.colorBorder = colorBorder;
   }
   /** Constructor with the default color */
   public ContainerCircle(int centerX, int centerY, int radius) {
      this(centerX, centerY, radius, DEFAULT_COLOR_FILLED, DEFAULT_COLOR_BORDER);
   }
   /** Set or reset the container (to match the screen) */
   public void set(int centerX, int centerY, int radius) {
      this.centerX = centerX;
      this.centerY = centerY;
      this.radius = radius;
   }

   /** Draw itself using the given graphic context. */
   public void draw(Graphics g) {
      g.setColor(colorFilled);
      g.fillOval(centerX - radius, centerY - radius, 2 * radius + 1, 2 * radius + 1);
      g.setColor(colorBorder);
      g.drawOval(centerX - radius, centerY - radius, 2 * radius + 1, 2 * radius + 1);
   }
}
