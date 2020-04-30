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
            double newX = electron.getX() / distance * radius;
            double newY = electron.getY() / distance * radius;
            double tanX = newY;     // Bx
            double tanY = -newX;    // By
            double velX = electron.getVelX();   // Ax
            double velY = electron.getVelY();   // Ay
            double dotAB = tanX * velX + tanY * velY;
            double dotBB = tanX * tanX + tanY * tanY;
            double projX = dotAB / dotBB * tanX;
            double projY = dotAB / dotBB * tanY;
            electron.setVelX(projX);
            electron.setVelY(projY);
            electron.setX(newX);
            electron.setY(newY);
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
