package us.kosdt.professorg.distancing;

import java.awt.*;

public interface Boundary2D {

    public void bound(Electron2D electron);
    public void randomizePosition(Electron2D electron);
    public void draw(Graphics g, int frameWidth, int frameHeight);
    public double getHeight();

}
