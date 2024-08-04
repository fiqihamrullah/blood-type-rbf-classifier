/*
 * Nama Kelompok    : Kelompok 3
 * Mata Kuliah      : Konsep Simulasi dan Permodelan

 */
package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 *
 * 
 */
public class PanelYellow extends JPanel {    

    public PanelYellow() {
        setOpaque(false); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gd = (Graphics2D) g.create();
        gd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);      
        gd.setPaint(new GradientPaint(0, 0, Color.black, 0, getHeight(), Color.yellow.darker()));
        gd.fillRoundRect(2, 2, getWidth() - 3, getHeight() - 3, 25, 25);
        gd.setStroke(new BasicStroke(3F));
        gd.setPaint(new GradientPaint(0, 0, Color.yellow.brighter(), 0, getHeight(), Color.yellow.darker().darker()));
        gd.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 25, 25);
        gd.dispose();
    }
}
