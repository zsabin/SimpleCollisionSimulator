package zsabin.collisionSimulator; /**
 * @author zsabin
 */

import javax.swing.*;
import java.awt.*;


public class GUIManager
{
    private final GridPanel gridPanel;

    public GUIManager(GridPanel gridPanel)
    {
        this.gridPanel = gridPanel;
    }

    public void generateWindow()
    {
        JFrame frame = new JFrame("Collision Simulator");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        gridPanel.setOpaque(true);
        gridPanel.setBackground(new Color(237, 235, 232));

        frame.add(gridPanel);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
}
