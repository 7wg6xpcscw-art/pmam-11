package org.example.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class UIUtils {
    public static final Color ACCENT_COLOR = new Color(18, 60, 120);

    public static class CustomScrollBarUI extends BasicScrollBarUI {
        private final Color thumbColor;
        public CustomScrollBarUI(Color thumbColor) { this.thumbColor = thumbColor; }
        @Override protected void configureScrollBarColors() {
            this.thumbDarkShadowColor = thumbColor.darker();
            this.thumbLightShadowColor = thumbColor.brighter();
            this.thumbHighlightColor = thumbColor.brighter();
            this.trackColor = new Color(240, 240, 240);
        }
        @Override protected void installDefaults() { super.installDefaults(); UIManager.put("ScrollBar.width", 12); }
        @Override protected JButton createDecreaseButton(int orientation) { return createZeroButton(); }
        @Override protected JButton createIncreaseButton(int orientation) { return createZeroButton(); }
        private JButton createZeroButton() { return new JButton() { @Override public Dimension getPreferredSize() { return new Dimension(0, 0); } }; }
        @Override protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, thumbBounds.width - 4, thumbBounds.height - 4, 10, 10);
            g2.dispose();
        }
    }

    public static class CustomComboBoxUI extends BasicComboBoxUI {
        @Override public void installUI(JComponent c) { super.installUI(c); c.setBorder(new EmptyBorder(2, 4, 2, 4)); }
        @Override protected JButton createArrowButton() {
            JButton button = new BasicArrowButton(BasicArrowButton.SOUTH, Color.WHITE, Color.WHITE, ACCENT_COLOR, Color.WHITE);
            button.setBorder(BorderFactory.createEmptyBorder());
            return button;
        }
        @Override public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            g.setColor(comboBox.getBackground()); g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
        @Override public void paint(Graphics g, JComponent c) { paintCurrentValue(g, rectangleForCurrentValue(), hasFocus); }
    }

    public static void customizeScrollPane(JScrollPane scrollPane, Color color) {
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI(color));
        scrollPane.getHorizontalScrollBar().setUI(new CustomScrollBarUI(color));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
    }
}