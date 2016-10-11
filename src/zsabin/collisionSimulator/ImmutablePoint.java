package zsabin.collisionSimulator;

import java.util.Objects;

/**
 * @author zsabin
 */
public class ImmutablePoint
{
    public final int x;
    public final int y;

    public ImmutablePoint(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public static ImmutablePoint translate(ImmutablePoint point, int xDelta, int yDelta)
    {
        return new ImmutablePoint(point.getX() + xDelta, point.getY() + yDelta);
    }

    public boolean equals(Object o)
    {
        if (o instanceof ImmutablePoint)
        {
            ImmutablePoint other = (ImmutablePoint)o;
            return x == other.getX() && y == other.getY();
        }
        return false;
    }

    public int hashCode()
    {
        return Objects.hashCode(x) * Objects.hashCode(y);
    }

    public String toString()
    {
        return "x: " + x + " y: " + y;
    }
}
