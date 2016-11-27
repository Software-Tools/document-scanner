/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package richtercloud.document.scanner.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import richtercloud.document.scanner.ifaces.OCRSelectPanel;
import richtercloud.document.scanner.ifaces.OCRSelectPanelSelectionListener;

/**
 * A panel which represents one single PDF or image page/scan. Will most likely
 * be contained in a {@link OCRSelectPanelPanel}.
 * @author richter
 */
/*
internal implementation notes:
- there's no sense in overwriting equals or hashCode since the only property
which can be used for distinction is image, but BufferedImage doesn't override
equals or hashCode
*/
public class DefaultOCRSelectPanel extends OCRSelectPanel implements MouseListener, MouseMotionListener {
    private static final long serialVersionUID = 1L;
    private Point dragStart;
    private Point dragEnd;
    private final BufferedImage image;
    private final Set<OCRSelectPanelSelectionListener> selectionListeners = new HashSet<>();
    private float zoomLevel = 1;

    public DefaultOCRSelectPanel(BufferedImage image) {
        this.image = image;
        updatePreferredSize();
        this.init0();
    }

    private void updatePreferredSize() {
        this.setPreferredSize(new Dimension((int)(image.getWidth()*zoomLevel),
                (int)(image.getHeight()*zoomLevel)));
    }

    private void init0() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    @Override
    public BufferedImage getImage() {
        return this.image;
    }

    @Override
    public void unselect() {
        this.dragStart = null;
        this.dragStart = null;
        this.repaint();
    }

    @Override
    public void addSelectionListener(OCRSelectPanelSelectionListener listener) {
        this.selectionListeners.add(listener);
    }

    public void removeSelectionListener(OCRSelectPanelSelectionListener listener) {
        this.selectionListeners.remove(listener);
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent evt) {
        this.setDragStart(evt.getPoint());
        for(OCRSelectPanelSelectionListener listener : this.selectionListeners) {
            listener.selectionChanged();
        }
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent evt) {
        this.setDragEnd(evt.getPoint());
        for(OCRSelectPanelSelectionListener listener : this.selectionListeners) {
            listener.selectionChanged();
        }
    }

    /**
     * one single click should remove the OCR frame
     * @param evt
     */
    @Override
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        this.setDragEnd(null);
        this.setDragStart(null);
        this.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(java.awt.event.MouseEvent evt) {
        this.setDragEnd(evt.getPoint());
        this.repaint();
        for(OCRSelectPanelSelectionListener listener : this.selectionListeners) {
            listener.selectionChanged();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int newImageWidth = (int)(imageWidth * zoomLevel);
        int newImageHeight = (int)(imageHeight * zoomLevel);
        g.drawImage(this.image, 0, 0, newImageWidth , newImageHeight ,
                null //imageObserver
        );
        if(this.getDragStart() != null && this.getDragEnd() != null) {
            g.drawRect(this.dragSelectionX(),
                    this.dragSelectionY(),
                    this.dragSelectionWidth(), //width
                    this.dragSeletionHeight() //height
            );
        }
    }

    @Override
    public int dragSelectionX() {
        return Math.min(this.getDragStart().x, this.getDragEnd().x);
    }

    @Override
    public int dragSelectionY() {
        return Math.min(this.getDragStart().y, this.getDragEnd().y);
    }

    @Override
    public int dragSelectionWidth() {
        return Math.max(this.getDragEnd().x, this.getDragStart().x)-Math.min(this.getDragEnd().x, this.getDragStart().x);
    }

    @Override
    public int dragSeletionHeight() {
        return Math.max(this.getDragEnd().y, this.getDragStart().y)-Math.min(this.getDragEnd().y, this.getDragStart().y);
    }

    /**
     * @return the dragStart
     */
    @Override
    public Point getDragStart() {
        return this.dragStart;
    }

    /**
     * @param dragStart the dragStart to set
     */
    protected void setDragStart(Point dragStart) {
        this.dragStart = dragStart;
    }

    /**
     * @return the dragEnd
     */
    @Override
    public Point getDragEnd() {
        return this.dragEnd;
    }

    /**
     * @param dragEnd the dragEnd to set
     */
    protected void setDragEnd(Point dragEnd) {
        this.dragEnd = dragEnd;
    }

    public float getZoomLevel() {
        return zoomLevel;
    }

    @Override
    public void setZoomLevel(float zoomLevel) {
        this.zoomLevel = zoomLevel;
        updatePreferredSize();
    }
}