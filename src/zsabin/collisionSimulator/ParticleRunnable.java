package zsabin.collisionSimulator;

import java.util.concurrent.CountDownLatch;

/**
 * @author zsabin
 */
public class ParticleRunnable implements Runnable
{
    private final Particle particle;
    private final GridPanel gridPanel;
    private final CountDownLatch latch;

    public ParticleRunnable(Particle particle, GridPanel gridPanel, CountDownLatch latch)
    {
        this.particle = particle;
        this.gridPanel = gridPanel;
        this.latch = latch;
    }

    @Override
    public void run()
    {
        try
        {
            latch.await();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
            return;
        }

        for(;;)
        {
            try
            {
                Thread.sleep(70);
            } catch (InterruptedException ignored) {}
            ImmutablePoint initialPosition = particle.state.currentPosition;
            moveParticle();
            gridPanel.animateParticleMove(initialPosition, particle.state.currentPosition);
        }
    }

    private void moveParticle()
    {
        Particle obstacle;
        particle.lock.lock();
        try
        {
            for (;;)
            {
                handleBoundaryCollision();
                if ((obstacle = gridPanel.detectCollision(particle.id, particle.state.nextPosition)) != null)
                {
                    /* Utilize resource-order locking based on particle IDs */
                    if (particle.id < obstacle.id)
                    {
                        obstacle.lock.lock();
                    } else
                    {
                        particle.lock.unlock();
                        obstacle.lock.lock();
                        particle.lock.lock();
                    }
                    try
                    {
                        if (inPath(obstacle))
                        {
                            ParticleState preCollisionState = particle.state;
                            particle.collide(obstacle.state);
                            obstacle.collide(preCollisionState);
                        }
                    } finally
                    {
                        obstacle.lock.unlock();
                    }
                } else
                {
                    particle.move();
                    return;
                }
            }
        } finally
        {
            particle.lock.unlock();
        }
    }

    /** This method is only called while we hold our own lock **/
    private void handleBoundaryCollision()
    {
        ParticleState state = particle.state;
        ImmutablePoint nextPosition = state.nextPosition;
        Velocity velocity = state.velocity;
        if (!inRange(nextPosition.x, 0, gridPanel.width))
        {
            velocity = new Velocity(velocity.x * -1, velocity.y);
        }
        if (!inRange(nextPosition.y, 0, gridPanel.height))
        {
            velocity = new Velocity(velocity.x, velocity.y * -1);
        }
        particle.state = new ParticleState(state.currentPosition, velocity, Particle.COLOR);
    }

    private boolean inRange(int value, int lowerBound, int upperBound)
    {
        return value >= lowerBound && value < upperBound;
    }

    /** This method is only called while we hold our own lock and the lock of the obstructing particle **/
    private boolean inPath(Particle obstacle)
    {
        ImmutablePoint nextPosition = particle.state.nextPosition;
        ParticleState obstacleState = obstacle.state;
        return nextPosition.equals(obstacleState.currentPosition) || nextPosition.equals(obstacleState.nextPosition);
    }
}
