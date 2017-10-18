import collisionphysics.CollisionPhysics;
import collisionphysics.CollisionResponse;

/**
 * Created by jbackes on 10/2/17
 */
abstract public class DefaultMovable implements Movable {
  float x, y;           // Paddle's center x and y (package access)
  float speedX, speedY; // Paddle's speed per step in x and y (package access)
  float radius;         // Paddle's radius (package access)

  /**
   * Check if this Movable collides with the given another ball in the interval
   * (0, timeLimit].
   *
   * @param another:   another ball.
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

  /**
   * Check if this paddle collides with the container in the coming time-step.
   *
   * @param circle:    outer container.
   * @param timeLimit: upperbound of the time interval.
   */
  public void intersect(ContainerCircle circle, float timeLimit) {
    // Call movingPointIntersectsRectangleOuter, which returns the
    // earliest collision to one of the 4 borders, if collision detected.
    CollisionPhysics.pointIntersectsCircleOuter(x, y, speedX, speedY, radius,
        circle.centerX, circle.centerY, circle.radius, timeLimit, tempResponse);
    if (tempResponse.t < earliestCollisionResponse.t) {
      earliestCollisionResponse.copy(tempResponse);
    }
  }

  /**
   * Update the states of this ball for the given time.
   *
   * @param time: the earliest collision time detected in the system.
   *              If this ball's earliestCollisionResponse.time equals to time, this
   *              ball is the one that collided; otherwise, there is a collision elsewhere.
   */
  public void update(float time) {
    // Check if this ball is responsible for the first collision?
    if (earliestCollisionResponse.t <= time) { // FIXME: threshold?
      // This ball collided, get the new position and speed
      this.x = earliestCollisionResponse.getNewX(this.x, this.speedX);
      this.y = earliestCollisionResponse.getNewY(this.y, this.speedY);
      this.speedX = earliestCollisionResponse.newSpeedX;
      this.speedY = earliestCollisionResponse.newSpeedY;
    } else {
      // This ball does not involve in a collision. Move straight.
      this.x += this.speedX * time;
      this.y += this.speedY * time;
    }
    // Clear for the next collision detection
    earliestCollisionResponse.reset();
  }

  // For collision detection and response
  // Maintain the response of the earliest collision detected
  //  by this ball instance. Only the first collision matters! (package access)
  protected CollisionResponse earliestCollisionResponse = new CollisionResponse();
  public CollisionResponse getEarliestCollisionResponse() {
    return earliestCollisionResponse;
  };

  // Working copy for computing response in intersect(),
  // to avoid repeatedly allocating objects.
  protected CollisionResponse tempResponse = new CollisionResponse();

  // Working copy for computing response in intersect(Ball, timeLimit),
  // to avoid repeatedly allocating objects.
  protected CollisionResponse thisResponse = new CollisionResponse();
  protected CollisionResponse anotherResponse = new CollisionResponse();

  @Override
  public float getX() {
    return x;
  }

  @Override
  public float getY() {
    return y;
  }

  @Override
  public float getSpeedX() {
    return speedX;
  }

  @Override
  public float getSpeedY() {
    return speedY;
  }

  @Override
  public float getRadius() {
    return radius;
  }
}
