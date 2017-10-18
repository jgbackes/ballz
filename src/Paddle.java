import collisionphysics.CollisionPhysics;
import collisionphysics.CollisionResponse;

import java.awt.*;
import java.util.Formatter;

/**
 * Created by jbackes on 10/2/17
 */
public class Paddle extends DefaultMovable implements Movable {

  private Color color;  // Paddle's color
  private static final Color DEFAULT_COLOR = Color.BLUE;

  /**
   * Constructor: For user friendliness, user specifies velocity in speed and
   * moveAngle in usual Cartesian coordinates. Need to convert to speedX and
   * speedY in Java graphics coordinates for ease of operation.
   */
  public Paddle(float x, float y, float radius, float speed, float angleInDegree, Color color) {
    this.x = x;
    this.y = y;
    // Convert (speed, angle) to (x, y), with y-axis inverted
    this.speedX = (float) (speed * Math.cos(Math.toRadians(angleInDegree)));
    this.speedY = (float) (-speed * (float) Math.sin(Math.toRadians(angleInDegree)));
    this.radius = radius;
    this.color = color;
  }

  /**
   * Constructor with the default color
   */
  public Paddle(float x, float y, float radius, float speed, float angleInDegree) {
    this(x, y, radius, speed, angleInDegree, DEFAULT_COLOR);
  }

  /**
   * Check if this paddle collides with the container in the coming time-step.
   *
   * @param another:    outer container.
   * @param timeLimit: upperbound of the time interval.
   */
  @Override
  public void intersect(Movable another, float timeLimit) {
    // Call movingPointIntersectsMovingPoint() with timeLimit.
    // Use thisResponse and anotherResponse, as the working copies, to store the
    // responses of this ball and another ball, respectively.
    // Check if this collision is the earliest collision, and update the ball's
    // earliestCollisionResponse accordingly.
    CollisionPhysics.pointIntersectsMovingPoint(
        this.x, this.y, this.speedX, this.speedY, this.radius,
        another.getX(), another.getY(), another.getSpeedX(), another.getSpeedY(), another.getRadius(),
        timeLimit, thisResponse, anotherResponse);
    if (anotherResponse.t < another.getEarliestCollisionResponse().t) {
      another.getEarliestCollisionResponse().copy(anotherResponse);
    }
    if (thisResponse.t < earliestCollisionResponse.t) {
      earliestCollisionResponse.copy(thisResponse);
    }
  }

  // Working copy for computing response in intersect(Paddle, timeLimit),
  // to avoid repeatedly allocating objects.
  private CollisionResponse thisResponse = new CollisionResponse();
  private CollisionResponse anotherResponse = new CollisionResponse();

  /**
   * Draw itself using the given graphics context.
   */
  public void draw(Graphics g) {
    g.setColor(color);
    g.fillOval((int) (x - radius), (int) (y - radius), (int) (2 * radius),
        (int) (2 * radius));
  }

  /**
   * Return the magnitude of speed.
   */
  public float getSpeed() {
    return (float) Math.sqrt(speedX * speedX + speedY * speedY);
  }

  /**
   * Return the direction of movement in degrees (counter-clockwise).
   */
  public float getMoveAngle() {
    return (float) Math.toDegrees(Math.atan2(-speedY, speedX));
  }

  /**
   * Return mass
   */
  public float getMass() {
    return radius * radius * radius / 1000f;
  }

  /**
   * Return the kinetic energy (0.5mv^2)
   */
  public float getKineticEnergy() {
    return 0.5f * getMass() * (speedX * speedX + speedY * speedY);
  }

  /**
   * Describe itself.
   */
  public String toString() {
    sb.delete(0, sb.length());
    formatter.format("@(%3.0f,%3.0f) r=%3.0f V=(%3.0f,%3.0f) " +
            "S=%4.1f \u0398=%4.0f KE=%3.0f",
        x, y, radius, speedX, speedY, getSpeed(), getMoveAngle(),
        getKineticEnergy());  // \u0398 is theta
    return sb.toString();
  }

  private StringBuilder sb = new StringBuilder();
  private Formatter formatter = new Formatter(sb);
}
