package us.kosdt.professorg.distancing;

import java.awt.*;

public class CircleBoundary implements Boundary2D {

    private double radius;

    public CircleBoundary(double radius) {
        this.radius = radius;
    }

    @Override
    public void bound(Electron2D electron) {
        double distance = electron.distanceTo(0.0, 0.0);
        if (distance > radius) {
            electron.setVelX(0.0);
            electron.setVelY(0.0);
            electron.setX(electron.getX() / distance * radius);
            electron.setY(electron.getY() / distance * radius);
        }
    }

    @Override
    public void randomizePosition(Electron2D electron) {
        do {
            electron.setX(Math.random() * radius * 2 - radius);
            electron.setY(Math.random() * radius * 2 - radius);
        } while (electron.distanceTo(0.0, 0.0) > radius);
    }

    @Override
    public void draw(Graphics g, int frameWidth, int frameHeight) {
        g.drawOval(frameWidth / 2 - (int) Math.round(frameHeight / 2 * Constants.SCALE_FACTOR),
                frameHeight / 2 - (int) Math.round(frameHeight / 2 * Constants.SCALE_FACTOR),
                (int) Math.round(frameHeight * Constants.SCALE_FACTOR),
                (int) Math.round(frameHeight * Constants.SCALE_FACTOR));
    }

    @Override
    public double getHeight() {
        return 2 * radius;
    }
}
