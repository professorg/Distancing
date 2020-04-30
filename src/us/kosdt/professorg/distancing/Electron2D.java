package us.kosdt.professorg.distancing;

import java.awt.*;

public class Electron2D {

    public static final int DRAW_SIZE = 20;
    public static final double MIN_DISTANCE = 1E-6;
    public static final double SPEED_OF_LIGHT = Double.POSITIVE_INFINITY;

    double charge;
    private double x, y;
    private double velX, velY;
    private double forceX, forceY;

    public Electron2D() {
        this(0.0, 0.0, 1.0);
        if (Constants.DO_CHARGES && Math.random() < 0.50)
            this.charge = -1.0;
    }

    public Electron2D(double x, double y, double charge) {
        this.x = x;
        this.y = y;
        this.charge = charge;
        this.velX = 0.0;
        this.velY = 0.0;
        this.forceX = 0.0;
        this.forceY = 0.0;
    }

    public Electron2D(Electron2D other) {
        this.x = other.x;
        this.y = other.y;
        this.charge = other.charge;
        this.velX = other.velX;
        this.velY = other.velY;
        this.forceX = other.forceX;
        this.forceY = other.forceY;
    }

    public double distanceSquaredTo(double x, double y) {
        double dx = this.x - x;
        double dy = this.y - y;
        return dx*dx + dy*dy;
    }

    public double distanceTo(double x, double y) {
        return Math.sqrt(this.distanceSquaredTo(x, y));
    }

    public double distanceSquaredTo(Electron2D other) {
        return distanceSquaredTo(other.x, other.y);
    }

    public double distanceTo(Electron2D other) {
        return Math.sqrt(this.distanceSquaredTo(other));
    }

    public void force(Electron2D source) {
        double dx = this.x - source.x;
        double dy = this.y - source.y;
        double distanceSquared = Math.pow(this.distanceTo(source), 1.0);
        if (distanceSquared == 0.0)
            return;

        if (Constants.DO_NEUTRALIZE
                && Math.signum(this.charge) + Math.signum(source.charge) < 1E-6
                && Math.abs(distanceSquared) < MIN_DISTANCE) {
            this.charge = 0.0;
            source.charge = 0.0;
        }

        else
            this.addForce(
                    (Constants.DO_GRAVITY ? -1.0 : 1.0) * charge * source.charge * dx / distanceSquared,
                    (Constants.DO_GRAVITY ? -1.0 : 1.0) * charge * source.charge * dy / distanceSquared
            );
    }

    public void addForce(double x, double y) {
        this.forceX += x;
        this.forceY += y;
    }

    public void tick() {
        this.velX += Constants.DELTA * this.forceX;
        this.velY += Constants.DELTA * this.forceY;
        if (Math.pow(this.velX, 2) + Math.pow(this.velY, 2) > SPEED_OF_LIGHT) {
            this.velX = 0.0;
            this.velY = 0.0;
        }
        this.x += Constants.DELTA * this.velX;
        this.y += Constants.DELTA * this.velY;
        this.forceX = 0.0;
        this.forceY = 0.0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVelX() {
        return velX;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public void draw(Graphics g, int frameWidth, int frameHeight, double boundaryHeight) {
        if (charge < 0)
            g.setColor(Color.BLUE);
        else if (charge > 0)
            g.setColor(Color.RED);
        else
            g.setColor(Color.GRAY);
        g.fillOval(
                toScreenX(this.x, frameWidth, frameHeight, boundaryHeight) - DRAW_SIZE,
                toScreenY(this.y, frameWidth, frameHeight, boundaryHeight) - DRAW_SIZE,
                DRAW_SIZE * 2, DRAW_SIZE * 2);
    }

    private int toScreenX(double x, int frameWidth, int frameHeight, double boundaryHeight) {
        // 0 => frame.width / 2
        // boundaryHeight => frame.width / 2 + frame.height / 2
        return (int) Math.round(frameWidth / 2 + x * frameHeight / boundaryHeight * Constants.SCALE_FACTOR);
    }

    private int toScreenY(double y, int frameWidth, int frameHeight, double boundaryHeight) {
        // 0 => frame.height / 2
        // boundaryHeight => 0
        return (int) Math.round(frameHeight / 2 - y * frameHeight / boundaryHeight * Constants.SCALE_FACTOR);
    }

    @Override
    public String toString() {
        String sb = String.format("Position: (%.2f, %.2f)%n", this.x, this.y) +
                String.format("Force: (%.2f, %.2f)", this.forceX, this.forceY);
        return sb;
    }

}
