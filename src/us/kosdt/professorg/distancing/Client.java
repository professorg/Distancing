package us.kosdt.professorg.distancing;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Client {

    public static void main(String[] args) {
        // Construct a circle boundary
        Boundary2D boundary = new CircleBoundary(1.0);
        // Make a list of electrons
        List<Electron2D> electrons = Stream.generate(Electron2D::new).limit(Constants.NUM_PARTICLES).collect(Collectors.toList());
        // Randomize each electron position within the boundary
        electrons.forEach(e -> boundary.randomizePosition(e));
        // Make a frame
        SwingUtilities.invokeLater(() -> new ElectronFrame2D(boundary, electrons));
    }
}
