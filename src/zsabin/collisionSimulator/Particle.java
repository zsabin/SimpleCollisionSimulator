package zsabin.collisionSimulator;

import java.awt.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zsabin
 */
public class Particle
{
    public static final int SIZE = 12;
    public static final Color COLOR = Color.BLUE.darker().darker();
    public static final Color COLLISION_COLOR = Color.RED;

    public static int nextId = 0;

    public final int id;
    public volatile ParticleState state;

    public final Lock lock = new ReentrantLock();

    // A countdown to determine for how many more moves a particle will be painted in the collision color after a collision
    private int collisionColorCountdown = 0;

    public static Particle getNextParticle(ImmutablePoint position, Velocity velocity)
    {
        return new Particle(nextId++, new ParticleState(position, velocity, COLOR));
    }

    private Particle(int id, ParticleState state)
    {
        this.id = id;
        this.state = state;
    }

    public void move()
    {
        Color color = collisionColorCountdown > 0 ? COLLISION_COLOR : COLOR;
        this.state = new ParticleState(state.nextPosition, state.velocity, color);
        if (collisionColorCountdown > 0)
        {
            collisionColorCountdown--;
        }
    }

    public void collide(ParticleState colliderState)
    {
        Velocity finalVelocity = calculatePostCollisionVelocity(state.velocity, colliderState.velocity);
        state = new ParticleState(state.currentPosition, finalVelocity, COLLISION_COLOR);
        collisionColorCountdown = 3;
    }

    public Velocity calculatePostCollisionVelocity(Velocity v1, Velocity v2)
    {
        int xSpeed = v1.x == 0 ? v2.x : v1.x * -1;
        int ySpeed = v1.y == 0 ? v2.y : v1.y * -1;
        return new Velocity(xSpeed, ySpeed);
    }
}
