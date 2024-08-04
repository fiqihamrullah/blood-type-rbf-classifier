package ui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;

public class ButtonGlass extends JButton {

    private Color glassColor;
    private boolean over;
    private boolean press;
    private int roundRect;

    public ButtonGlass() {
        setGlassColor(null);
        setOver(false);
        setPress(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setRoundRect(23);
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                setOver(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                setOver(false);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                setPress(true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                setPress(false);
            }
        });
    }

    private boolean isPress() {
        return this.press;
    }

    private void setPress(boolean press) {
        this.press = press;
        repaint();
    }

    private boolean isOver() {
        return this.over;
    }

    private void setOver(boolean over) {
        this.over = over;
        repaint();
    }

    public Color getGlassColor() {
        return this.glassColor;
    }

    public void setGlassColor(Color glassColor) {
        try {
            if (glassColor == null) {
                this.glassColor = new Color(255, 255, 255, 125);
            } else {
                this.glassColor = glassColor;
            }
        } finally {
            repaint();
        }
    }

    public void setRoundRect(int roundRect) {
        this.roundRect = roundRect;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        try {
            Graphics2D g2;
            Shape s;
            Color light;
            Color dark;
            Paint p;
            if (isEnabled()) {
                g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                s = new RoundRectangle2D.Double(0.0D, 0.0D, getWidth(), getHeight(), roundRect, roundRect);


                light = new Color(getGlassColor().getRed(), getGlassColor().getGreen(), getGlassColor().getBlue(), 125);
                dark = new Color(getGlassColor().getRed(), getGlassColor().getGreen(), getGlassColor().getBlue(), 0);

                p = null;

                if (isOver()) {
                    if (isPress()) {
                        p = new GradientPaint(0.0F, getHeight(), light, 0.0F, getHeight() - (getHeight() / 2), light);
                    } else {
                        p = new GradientPaint(0.0F, getHeight(), light, 0.0F, 0.0F, dark);
                    }
                } else {
                    p = new GradientPaint(0.0F, 0.0F, light, 0.0F, getHeight(), dark);
                }

                g2.setPaint(p);
                g2.fill(s);
                g2.dispose();
            } else {
                g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                s = null;

                light = new Color(getGlassColor().getRed(), getGlassColor().getGreen(), getGlassColor().getBlue(), 111);
                dark = new Color(getGlassColor().getRed(), getGlassColor().getGreen(), getGlassColor().getBlue(), 0);

                p = new GradientPaint(0.0F, getHeight(), light, 0.0F, getHeight() - (getHeight() / 2), light);

                g2.setPaint(p);
                g2.fill(s);
                g2.dispose();
            }
        } catch (NullPointerException ex) {
        }

        super.paintComponent(g);
    }
}
