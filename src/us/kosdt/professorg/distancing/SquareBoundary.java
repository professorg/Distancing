package us.kosdt.professorg.distancing;

import java.awt.*;

public class SquareBoundary implements Boundary2D {

    double radius;

    public SquareBoundary(double radius) {
        this.radius = radius;
    }

    @Override
    public void bound(Electron2D electron) {
        if (electron.getX() < -radius) {
            electron.setVelX(0.0);
            electron.setX(-radius);
        }

        if (electron.getY() < -radius) {
            electron.setVelY(0.0);
            electron.setY(-radius);
        }

        if (electron.getX() > radius) {
            electron.setVelX(0.0);
            electron.setX(radius);
        }

        if (electron.getY() > radius) {
            electron.setVelY(0.0);
            electron.setY(radius);
        }
    }

    @Override
    public void randomizePosition(Electron2D electron) {
        electron.setX(Math.random() * radius * 2 - radius);
        electron.setY(Math.random() * radius * 2 - radius);
    }

    @Override
    public void draw(Graphics g, int frameWidth, int frameHeight) {
        g.drawRect(frameWidth / 2 - (int) Math.round(frameHeight / 2 * Constants.SCALE_FACTOR),
                frameHeight / 2 - (int) Math.round(frameHeight / 2 * Constants.SCALE_FACTOR),
                (int) Math.round(frameHeight * Constants.SCALE_FACTOR),
                (int) Math.round(frameHeight * Constants.SCALE_FACTOR));
    }

    @Override
    public double getHeight() {
        return 2 * radius;
    }

}
