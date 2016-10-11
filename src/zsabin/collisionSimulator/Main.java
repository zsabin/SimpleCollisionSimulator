package zsabin.collisionSimulator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @author zsabin
 */
public class Main
{
    private static final Random rand = new Random();

    public static void main(String[] args) throws InterruptedException
    {
        int gridWidth = 624;
        int gridHeight = 480;

        int particleCount = 12;
        List<Particle> particles = new ArrayList<>();

        final CountDownLatch latch = new CountDownLatch(1);

        /* Create initially motionless particles in middle of grid */
        particles.add(Particle.getNextParticle(new ImmutablePoint(276, 204), new Velocity(0, 0)));
        particles.add(Particle.getNextParticle(new ImmutablePoint(324, 240), new Velocity(0, 0)));
        particles.add(Particle.getNextParticle(new ImmutablePoint(324, 204), new Velocity(0, 0)));
        particles.add(Particle.getNextParticle(new ImmutablePoint(276, 240), new Velocity(0, 0)));

        /* Create moving particles that start at random locations throughout the grid */
        while (particles.size() < particleCount)
        {
            ImmutablePoint position;
            do {
                position = getRandomPosition(gridWidth, gridHeight);
            } while (positionIsOccupied(position, particles));
            Velocity velocity = getRandomVelocity();
            particles.add(Particle.getNextParticle(position, velocity));
        }

        /* Generate and display the grid panel */
        GridPanel gridPanel = new GridPanel(gridWidth, gridHeight, particles);
        GUIManager guiManager = new GUIManager(gridPanel);
        guiManager.generateWindow();

        /* Start the Particle Runnables */
        for (Particle particle : particles)
        {
            ParticleRunnable particleRunnable = new ParticleRunnable(particle, gridPanel, latch);
            new Thread(particleRunnable).start();
        }
        Thread.sleep(500);
        latch.countDown();
    }

    private static ImmutablePoint getRandomPosition(int xBound, int yBound)
    {
        int xPos = rand.nextInt(xBound / Particle.SIZE) * Particle.SIZE;
        int yPos = rand.nextInt(yBound / Particle.SIZE) * Particle.SIZE;
        return new ImmutablePoint(xPos, yPos);
    }

    private static boolean positionIsOccupied(ImmutablePoint position, List<Particle> particles)
    {
        for (Particle particle : particles)
        {
            if (particle.state.currentPosition.equals(position))
            {
                return true;
            }
        }
        return false;
    }

    private static Velocity getRandomVelocity()
    {
        return new Velocity(getRandomSign(), getRandomSign());
    }

    private static int getRandomSign()
    {
        return rand.nextInt(2) * 2 - 1;
    }
}
