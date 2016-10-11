package zsabin.collisionSimulator;

/**
 * @author zsabin
 */
public class Velocity
{
    public final int x;
    public final int y;

    Velocity(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public String toString()
    {
        return "x:" + x + " y:" + y;
    }
}
