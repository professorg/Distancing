package us.kosdt.professorg.distancing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

public class ElectronFrame2D extends Frame {

    private boolean updating;
    private Boundary2D boundary;
    private List<Electron2D> electrons;

    private int bufferWidth;
    private int bufferHeight;
    private Image bufferImage;
    private Graphics bufferGraphics;

    public ElectronFrame2D(Boundary2D boundary, List<Electron2D> electrons) {
        super("Electron Plotter");

        this.updating = false;
        this.boundary = boundary;
        this.electrons = electrons;

        setSize(400, 300);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                repaint();
            }
        });

        startSimulation();
    }

    public void startSimulation() {
        SwingWorker<Object, Object> sw = new SwingWorker<Object, Object>() {
            @Override
            protected Object doInBackground() throws Exception {
                while (true) {
                    update();
                    Thread.sleep(1);
                }
            }
        };
        sw.execute();
    }

    public void update() {
        updating = true;
        // Calculate forces
        electrons.forEach(electron -> {
            electrons.forEach(other -> {
                if (electron == other)
                    return;

                electron.force(other);
            });
        });
        // Update position
        electrons.forEach(Electron2D::tick);
        // Bound
        electrons.forEach(boundary::bound);
        updating = false;
        // Repaint
        repaint();
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        if (updating)
            return;

        if (bufferWidth != getSize().width
                || bufferHeight != getSize().height
                || bufferImage == null
                || bufferGraphics == null)
            resetBuffer();


        if (bufferGraphics != null) {
            // Clear
            bufferGraphics.clearRect(0, 0, bufferWidth, bufferHeight);

            // Draw to buffer
            bufferGraphics.setColor(Color.BLACK);
            boundary.draw(bufferGraphics, bufferWidth, bufferHeight);
            electrons.forEach(electron -> electron.draw(bufferGraphics, bufferWidth, bufferHeight, boundary.getHeight()));

            // Draw to screen
            g.drawImage(bufferImage, 0, 0, this);
        }
    }

    private void resetBuffer() {
        bufferWidth = getSize().width;
        bufferHeight = getSize().height;

        if (bufferGraphics != null) {
            bufferGraphics.dispose();
            bufferGraphics = null;
        }
        if (bufferImage != null) {
            bufferImage.flush();
            bufferImage = null;
        }
        System.gc();

        bufferImage = createImage(bufferWidth, bufferHeight);
        bufferGraphics = bufferImage.getGraphics();
    }

}
