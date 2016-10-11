package zsabin.collisionSimulator;

import java.awt.*;

/**
 * @author zsabin
 *
 *  We include the nextPosition in the state so we can ensure that two particles will not move into the same location
 *  simultaneously. If A and B both wish to move to position X then A and B must update their next position to be X
 *  prior to checking if the path to X is clear. This means that at least one of A or B will see that the other particle
 *  is attempting to move to position X.
 */
public class ParticleState
{
    public final ImmutablePoint currentPosition;
    public final ImmutablePoint nextPosition;
    public final Velocity velocity;
    public final Color color;

    ParticleState(ImmutablePoint currentPosition, Velocity velocity, Color color)
    {
        this.currentPosition = currentPosition;
        this.nextPosition = calculateNextPosition(currentPosition, velocity);
        this.velocity = velocity;
        this.color = color;
    }

    private ImmutablePoint calculateNextPosition(ImmutablePoint currentPosition, Velocity velocity)
    {
        return ImmutablePoint.translate(currentPosition, velocity.x * Particle.SIZE, velocity.y * Particle.SIZE );
    }
}
