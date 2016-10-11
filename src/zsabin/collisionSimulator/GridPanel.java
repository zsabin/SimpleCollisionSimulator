package zsabin.collisionSimulator;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

/**
 * @author zsabin
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
