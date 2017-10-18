import collisionphysics.CollisionResponse;

/**
 * Created by jbackes on 10/2/17
 */
public interface Movable {
  float getX();
  float getY();
  float getSpeedX();
  float getSpeedY();         // Movable's speed per step in x and y (package access)
  float getRadius();         // Movable's radius (package access)

  void intersect(Movable another, float timeLimit);
  void intersect(ContainerCircle circle, float timeLimit);
  void update(float time);

  CollisionResponse getEarliestCollisionResponse();
}
