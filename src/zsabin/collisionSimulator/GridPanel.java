package zsabin.collisionSimulator;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

/**
 * @author zsabin
 *
 * Particles exist in a grid-like structure where each square of the grid is equal to the diameter of the
 * particle. Particles move one square of the grid at a time and are treated one-dimensionally in terms of 
 * collisons with other particles (see note about how collisions are detected below).
 */
public class GridPanel extends JPanel
{
    public final int width;
    public final int height;
    public final List<Particle> particles;

    GridPanel(int width, int height, List<Particle> particles)
    {
        if (width % Particle.SIZE != 0 || height % Particle.SIZE != 0)
        {
            throw new IllegalArgumentException("Width and Height of grid must be a multiple of Particle.SIZE");
        }

        this.width = width;
        this.height = height;
        this.particles = Collections.unmodifiableList(particles);
    }

    /* A collision is only detected if particles are competing for the same square in the grid. This means that, 
     * in terms of collisions, particles are treated one-dimensionally. That is, they can only collide if their
     * centers collide; we do not consider it a collision when only the edges of particles come in contact.
     * Consequently, when particles visually "brush up" against eachother as they pass by diagonally in adjacent 
     * squares of the grid, it will appear that the particles went through eachother, but we ignore this for the 
     * purposes of this simple simulator.
     */
    public Particle detectCollision(int id, ImmutablePoint nextPosition)
    {
        for (Particle particle : particles)
        {
            if (particle.id != id)
            {
                ParticleState state = particle.state;
                if (nextPosition.equals(state.currentPosition) || nextPosition.equals(state.nextPosition))
                {
                    return particle;
                }
            }
        }
        return null;
    }

    public void animateParticleMove(ImmutablePoint initialPosition, ImmutablePoint finalPosition)
    {
        repaint(initialPosition.getX(), initialPosition.getY(), Particle.SIZE, Particle.SIZE);
        repaint(finalPosition.getX(), finalPosition.getY(), Particle.SIZE, Particle.SIZE);
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(width, height);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        for (Particle particle : particles)
        {
            ParticleState state = particle.state;
            ImmutablePoint position = state.currentPosition;
            g.setColor(state.color);
            g.fillOval(position.getX(), position.getY(), Particle.SIZE, Particle.SIZE);
        }
    }
}
