/**************************************************************************
 * alpha-Forms: self-editable formulars in form of an active document
 * (originally for the alpha-Flow project)
 * ==============================================
 * Copyright (C) 2009-2012 by 
 *   - Christoph P. Neumann (http://www.chr15t0ph.de)
 *   - Florian Wagner
 **************************************************************************
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 **************************************************************************
 * $Id$
 *************************************************************************/
package alpha.forms.designer.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import alpha.forms.designer.util.DraggedWidget;
import alpha.forms.designer.view.popup.FormPopupListener;
import alpha.forms.designer.view.popup.FormPopupMenu;
import alpha.forms.form.AlphaForm;
import alpha.forms.signal.model.PropertyUpdatedSignal;
import alpha.forms.signal.model.SelectionChangedSignal;
import alpha.forms.signal.model.Signal;
import alpha.forms.signal.model.SignalManager;
import alpha.forms.signal.model.Subscriber;
import alpha.forms.signal.model.WidgetTemplateSignal;
import alpha.forms.widget.model.FormWidget;
import alpha.forms.widget.model.container.AbstractContainerWidget;
import alpha.forms.widget.model.container.ContainerWidget;
import alpha.forms.widget.view.FormWidgetUI;

/**
 * The Class FormCanvas.
 */
public class FormCanvas extends JPanel implements MouseListener,
		MouseMotionListener, Subscriber, Scrollable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The parent. */
	private final FormDesignerPanel parent;

	/** The model. */
	private final AlphaForm model;

	/** The show grid. */
	private boolean showGrid = true;

	/** The grid size. */
	private int gridSize = 10;

	/** The selected element. */
	private FormWidget selectedElement = null;

	/** The selection. */
	SelectionState selection = null;

	/** The active container. */
	private ContainerWidget activeContainer = null;

	/** The active area. */
	private FocusArea activeArea = null;

	/** The is resize form. */
	boolean isResizeForm = false;

	/** The dragged widget. */
	DraggedWidget draggedWidget = null;

	/**
	 * Instantiates a new form canvas.
	 * 
	 * @param parent
	 *            the parent
	 * @param model
	 *            the model
	 */
	public FormCanvas(final FormDesignerPanel parent, final AlphaForm model) {
		this.parent = parent;
		this.model = model;
		this.setLayout(null);
		this.setAutoscrolls(true);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0),
				"deleteWidget");
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "deleteWidget");
		this.getActionMap().put("deleteWidget", new AbstractAction() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 6432735423738603274L;

			@Override
			public void actionPerformed(final ActionEvent ev) {
				if (FormCanvas.this.selection != null) {
					final FormWidget widget = FormCanvas.this.selection.subject;
					if (widget.getParent() != null) {
						final FormWidget parent = widget.getParent();
						((ContainerWidget) parent).removeChild(widget);
						parent.getUi().revalidate();
						parent.getUi().updateUI();
					} else {
						model.getWidgets().remove(widget);
					}
					FormCanvas.this.selection = null;
					FormCanvas.this.activeArea.updateUI();
					FormCanvas.this.repaint();
				}

			}

		});
		this.activeArea = new FocusArea(model, null, this);

		final FormPopupMenu formPopup = new FormPopupMenu(model, this);
		final FormPopupListener formPopupListener = new FormPopupListener(
				formPopup);
		this.addMouseListener(formPopupListener);

		SignalManager.getInstance().subscribeSink(this, "propertyEditor");
		SignalManager.getInstance().subscribeSink(this, "canvas");
	}

	/**
	 * Gets the parent panel.
	 * 
	 * @return the parent panel
	 */
	public FormDesignerPanel getParentPanel() {
		return this.parent;
	}

	/**
	 * Gets the selected items.
	 * 
	 * @return the selected items
	 */
	public List<FormWidget> getSelectedItems() {
		final List<FormWidget> selList = new ArrayList<FormWidget>();
		if (this.selection != null) {
			selList.add(this.selection.subject);
		}
		return selList;
	}

	/**
	 * Translate point.
	 * 
	 * @param pin
	 *            the pin
	 * @return the point
	 */
	public Point translatePoint(final Point pin) {
		if (this.showGrid) {
			final int x = ((pin.x + (this.gridSize / 2)) / this.gridSize)
					* this.gridSize;
			final int y = ((pin.y + (this.gridSize / 2)) / this.gridSize)
					* this.gridSize;
			return new Point(x, y);
		} else
			return new Point(pin);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintChildren(java.awt.Graphics)
	 */
	@Override
	protected void paintChildren(final Graphics g) {
		for (final FormWidget w : this.model.getWidgets()) {
			final FormWidgetUI ui = w.getUi();
			final Rectangle clipBounds = g.getClipBounds();
			final Rectangle cr = w.getBounds();
			SwingUtilities.computeIntersection(clipBounds.x, clipBounds.y,
					clipBounds.width, clipBounds.height, cr);
			final Graphics gComp = g.create(cr.x, cr.y, cr.width, cr.height);
			gComp.setFont(this.getFont());
			gComp.setColor(this.getForeground());
			ui.doLayout();
			ui.addNotify();
			ui.validate();
			ui.printAll(gComp);
			gComp.dispose();
		}
		if (this.activeContainer != null) {
			final Graphics2D g2d = (Graphics2D) g;
			g2d.setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 1.0f));
			final Color c = SystemColor.textHighlight;
			final Color o = new Color(c.getRed(), c.getGreen(), c.getBlue(),
					120);
			g2d.setPaint(o);
			g2d.fill(((FormWidget) this.activeContainer).getBounds());
		}
		if (this.selection != null) {
			this.selection.drawSelection((Graphics2D) g, this.activeArea);
		}
		if (this.draggedWidget != null) {
			this.draggedWidget.render((Graphics2D) g);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(final Graphics g) {
		// super.paintComponent(g);
		final Dimension d = this.getSize();
		g.setColor(SystemColor.control);
		g.fillRect(0, 0, d.width, d.height);
		if (this.showGrid) {
			g.setColor(this.getForeground());
			for (int x = 0; x <= d.width; x += this.gridSize) {
				for (int y = 0; y <= d.height; y += this.gridSize) {
					g.fillRect(x, y, 1, 1);
				}
			}
		}
		if (this.selection == null) {
			g.setColor(this.getForeground());
			g.drawLine(0, d.height - 1, d.width - 1, d.height - 1);
			g.drawLine(d.width - 1, 0, d.width - 1, d.height - 1);
			g.fillRect(d.width - 4, d.height - 4, 4, 4);
		}
	}

	/**
	 * Find widget.
	 * 
	 * @param p
	 *            the p
	 * @return the form widget
	 */
	public FormWidget findWidget(final Point p) {
		return this.findWidget(p, 0);
	}

	/**
	 * Find widget.
	 * 
	 * @param p
	 *            the p
	 * @param d
	 *            the d
	 * @return the form widget
	 */
	public FormWidget findWidget(final Point p, final int d) {
		for (final FormWidget w : this.model.getWidgets()) {
			final Rectangle b = w.getBounds();
			b.height += 2 * d;
			b.width += 2 * d;
			b.x -= d;
			b.y -= d;
			if (b.contains(p))
				return w;
		}
		return null;
	}

	/**
	 * Find widget deep.
	 * 
	 * @param p
	 *            the p
	 * @return the form widget
	 */
	public FormWidget findWidgetDeep(final Point p) {
		final FormWidget w = this.findWidget(p);
		if ((w != null) && (w instanceof ContainerWidget)) {
			final FormWidget cw = ((ContainerWidget) w).getWidgetByLocation(p);
			if (cw != null)
				return cw;
		}
		return w;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(final MouseEvent ev) {
		if (ev.getClickCount() == 2) {
			System.out.println("Double click");
			final FormWidget fw = this.findWidgetDeep(ev.getPoint());
			if ((this.selection != null) && (this.selection.subject == fw)
					&& (fw.getUi().getSubselection() != null)) {
				this.selection.subselect = fw.getUi().getSubselection();
				this.updateUI();
			}
		}
	}

	/**
	 * Gets the active area.
	 * 
	 * @return the active area
	 */
	public FocusArea getActiveArea() {
		return this.activeArea;
	}

	/**
	 * Drag over event.
	 * 
	 * @param p
	 *            the p
	 * @param w
	 *            the w
	 */
	public void dragOverEvent(final Point p, final FormWidget w) {
		final FormWidget container = this.findWidget(p);
		if (w.getParent() == container)
			return;
		if ((container != null) && (container instanceof ContainerWidget)
				&& ((ContainerWidget) container).doesAcceptWidget(w)) {
			this.activeContainer = (ContainerWidget) container;
		} else {
			this.activeContainer = null;
		}
		this.updateUI();
	}

	/**
	 * Stop container highlight.
	 */
	public void stopContainerHighlight() {
		this.activeContainer = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(final MouseEvent ev) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(final MouseEvent ev) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(final MouseEvent ev) {
		this.selectedElement = this.findWidget(ev.getPoint(), 3);
		AbstractContainerWidget container = null;
		if (this.selectedElement != null) {
			if (this.selectedElement instanceof ContainerWidget) {
				final FormWidget cw = ((ContainerWidget) this.selectedElement)
						.getWidgetByLocation(ev.getPoint());
				if (cw != null) {
					container = (AbstractContainerWidget) this.selectedElement;
					this.selectedElement = cw;
				}
			}
			if ((this.selection != null)
					&& (this.selection.subject == this.selectedElement))
				return;
			this.activeArea = new FocusArea(this.model, container, this);
			this.setSelection(Arrays.asList(this.selectedElement));
		} else {
			this.setSelection(null);
			this.activeContainer = null;
			this.activeArea = new FocusArea(this.model, null, this);
		}
		this.updateUI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(final MouseEvent ev) {
		this.isResizeForm = false;
		if (this.selection != null) {
			final FormWidget w = this.selection.subject;
			if (this.draggedWidget != null) {
				Point p = ev.getPoint();
				p.translate(-this.selection.mouseOffset.x,
						-this.selection.mouseOffset.y);
				p = this.translatePoint(p);
				final FormWidget container = this.findWidget(ev.getPoint());
				if ((container != null)
						&& (container instanceof ContainerWidget)
						&& (container != w)) {
					this.draggedWidget.finalize(p.x, p.y,
							(ContainerWidget) container);
					this.activeArea = new FocusArea(this.model,
							(AbstractContainerWidget) container, this);
				} else {
					this.draggedWidget.finalize(p.x, p.y, null);
					this.activeArea = new FocusArea(this.model, null, this);
				}
				this.draggedWidget = null;
			}
			if ((this.activeContainer != null)
					&& (this.activeContainer != w.getParent())) {
				this.stopContainerHighlight();
			}
			this.selection.isMoving = false;
			this.selection.isResizing = false;
			this.selection.resizeDirection = -1;
			this.activeContainer = null;
			this.setCursor(Cursor.getDefaultCursor());
			this.updateUI();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent
	 * )
	 */
	@Override
	public void mouseDragged(final MouseEvent ev) {
		System.out.println("mouseDragged: " + ev.getPoint());
		if (((this.model.getWidth() - ev.getPoint().x) <= 5)
				&& ((this.model.getHeight() - ev.getPoint().y) <= 5)) {
			this.isResizeForm = true;
		} else if ((this.selection != null) && !this.selection.isMoving
				&& !this.selection.isResizing) {
			final Point p = ev.getPoint();
			final int dir = this.selection.getResizingDirection(p);
			if (dir >= 0) {
				this.selection.isResizing = true;
				this.selection.resizeDirection = dir;
			} else {
				this.selection.isMoving = true;
				final Point mouse = this.activeArea.translatePoint(ev
						.getPoint());
				this.draggedWidget = new DraggedWidget(this.selection.subject,
						this.model);
				this.selection.mouseOffset = new Point(mouse.x
						- this.selection.subject.getX(), mouse.y
						- this.selection.subject.getY());
			}
		}
		if (this.isResizeForm) {
			final Point p = this.translatePoint(ev.getPoint());
			if ((this.model.getMinWidth() < p.x)
					&& (this.model.getMinHeight() < p.y)) {
				this.model.setWidth(p.x);
				this.model.setHeight(p.y);
			} else {
				this.model.setWidth(this.model.getMinWidth() + 10);
				this.model.setHeight(this.model.getMinHeight() + 10);
			}
			this.revalidate();
		}
		if ((this.selection != null) && this.selection.isMoving) {
			final Point mouse = ev.getPoint();
			final Point np = new Point(mouse.x - this.selection.mouseOffset.x,
					mouse.y - this.selection.mouseOffset.y);
			final Point p = this.translatePoint(np);
			if ((p.x >= 0)
					&& (p.x <= ((this.getWidth() - this.selection.subject
							.getWidth()) + 40))) {
				this.draggedWidget.setX(p.x);
			}
			if ((p.y >= 0)
					&& (p.y <= ((this.getHeight() - this.selection.subject
							.getHeight()) + 40))) {
				this.draggedWidget.setY(p.y);
			}
			this.model.recalculateDimensions(this.draggedWidget);
			this.dragOverEvent(mouse, this.selection.subject);
			this.updateUI();
		}
		if ((this.selection != null) && this.selection.isResizing) {
			final Point mouse = this.activeArea.translatePoint(this
					.translatePoint(ev.getPoint()));
			if (this.selection.subselect != null) {
				final Rectangle r = this.activeArea
						.translateRect(this.selection.subject.getUi()
								.getSubselection());
				if (this.selection.resizeDirection == SelectionState.NORTH) {
					final int delta = r.y - mouse.y;
					this.selection.subject.getUi().updateSubselectionSize(
							delta, this.selection.resizeDirection);
				} else if (this.selection.resizeDirection == SelectionState.EAST) {
					final int delta = mouse.x - r.x;
					this.selection.subject.getUi().updateSubselectionSize(
							delta, this.selection.resizeDirection);
				} else if (this.selection.resizeDirection == SelectionState.SOUTH) {
					final int delta = mouse.y - r.y;
					this.selection.subject.getUi().updateSubselectionSize(
							delta, this.selection.resizeDirection);
				} else if (this.selection.resizeDirection == SelectionState.WEST) {
					final int delta = r.x - mouse.x;
					this.selection.subject.getUi().updateSubselectionSize(
							delta, this.selection.resizeDirection);
				}
			} else if (this.selection.resizeDirection == SelectionState.NORTH) {
				final int delta = this.selection.subject.getY() - mouse.y;
				final int height = this.selection.subject.getHeight() + delta;
				if ((height > 0)
						&& ((this.selection.subject.getY() - delta) >= 0)) {
					this.selection.subject.setY(this.selection.subject.getY()
							- delta);
					this.selection.subject.setHeight(height);
				}
			} else if (this.selection.resizeDirection == SelectionState.EAST) {
				final int width = mouse.x - this.selection.subject.getX();
				if ((width > 0)
						&& ((this.selection.subject.getX() + width) <= this
								.getWidth())) {
					this.selection.subject.setWidth(width);
				}
			} else if (this.selection.resizeDirection == SelectionState.SOUTH) {
				final int height = mouse.y - this.selection.subject.getY();
				if ((height > 0)
						&& ((this.selection.subject.getY() + height) <= this
								.getHeight())) {
					this.selection.subject.setHeight(height);
				}
			} else if (this.selection.resizeDirection == SelectionState.WEST) {
				final int delta = this.selection.subject.getX() - mouse.x;
				final int width = this.selection.subject.getWidth() + delta;
				if ((width > 0)
						&& ((this.selection.subject.getX() - delta) >= 0)) {
					this.selection.subject.setX(this.selection.subject.getX()
							- delta);
					this.selection.subject.setWidth(width);
				}
			}
			this.model.recalculateDimensions(this.selection.subject);
			this.updateUI();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(final MouseEvent ev) {

		if (this.selection != null) {
			final int rdir = this.selection.getResizingDirection(ev.getPoint());
			if (rdir >= 0) {
				if (rdir == SelectionState.NORTH) {
					this.setCursor(Cursor
							.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
				} else if (rdir == SelectionState.EAST) {
					this.setCursor(Cursor
							.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
				} else if (rdir == SelectionState.SOUTH) {
					this.setCursor(Cursor
							.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
				} else if (rdir == SelectionState.WEST) {
					this.setCursor(Cursor
							.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
				}
			} else if (this.selection.subject == this.findWidget(ev.getPoint())) {
				this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			} else {
				this.setCursor(Cursor.getDefaultCursor());
			}
		} else {
			this.setCursor(Cursor.getDefaultCursor());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.signal.model.Subscriber#signalReceived(alpha.forms.signal
	 * .model.Signal)
	 */
	@Override
	public void signalReceived(final Signal s) {
		if (s instanceof PropertyUpdatedSignal) {
			this.updateUI();
		} else if (s instanceof WidgetTemplateSignal) {

		} else if (s instanceof SelectionChangedSignal) {
			this.setSelection(((SelectionChangedSignal) s).getSelection());
			this.updateUI();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(this.model.getWidth(), this.model.getHeight());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Scrollable#getPreferredScrollableViewportSize()
	 */
	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return this.getPreferredSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.Scrollable#getScrollableBlockIncrement(java.awt.Rectangle,
	 * int, int)
	 */
	@Override
	public int getScrollableBlockIncrement(final Rectangle view,
			final int orientation, final int direction) {
		if (orientation == SwingConstants.HORIZONTAL)
			return view.width - 20;
		else
			return view.height - 20;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Scrollable#getScrollableTracksViewportHeight()
	 */
	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Scrollable#getScrollableTracksViewportWidth()
	 */
	@Override
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.Scrollable#getScrollableUnitIncrement(java.awt.Rectangle,
	 * int, int)
	 */
	@Override
	public int getScrollableUnitIncrement(final Rectangle arg0, final int arg1,
			final int arg2) {
		return 10;
	}

	/**
	 * Checks if is show grid.
	 * 
	 * @return true, if is show grid
	 */
	public boolean isShowGrid() {
		return this.showGrid;
	}

	/**
	 * Sets the show grid.
	 * 
	 * @param showGrid
	 *            the new show grid
	 */
	public void setShowGrid(final boolean showGrid) {
		this.showGrid = showGrid;
	}

	/**
	 * Gets the grid size.
	 * 
	 * @return the grid size
	 */
	public int getGridSize() {
		return this.gridSize;
	}

	/**
	 * Sets the grid size.
	 * 
	 * @param gridSize
	 *            the new grid size
	 */
	public void setGridSize(final int gridSize) {
		this.gridSize = gridSize;
	}

	/**
	 * Set a new selection
	 * 
	 * @param newSelection
	 *            the elements going to be selected
	 */
	protected void setSelection(final List<FormWidget> newSelection) {
		final SelectionChangedSignal s = new SelectionChangedSignal();
		if ((newSelection == null) || (newSelection.size() == 0)) {
			this.selection = null;
			s.setSelection(null);
		} else {
			this.selection = new SelectionState(this);
			this.selection.subject = newSelection.get(0);

			s.setSelection(new ArrayList<FormWidget>());
			s.getSelection().add(this.selection.subject);
		}
		s.setSource(this);
		SignalManager.getInstance().sendSignal(s, "formCanvas");
	}

}