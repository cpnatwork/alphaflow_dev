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
package alpha.forms.designer.dnd;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JList;

import alpha.forms.designer.view.FormCanvas;
import alpha.forms.designer.view.FormDesignerPanel;
import alpha.forms.form.naming.WidgetNameGenerator;
import alpha.forms.template.model.WidgetTemplate;
import alpha.forms.widget.model.FormWidget;

/**
 * The Class ComponentDragDrop.
 */
public class ComponentDragDrop implements DragGestureListener,
		DragSourceListener, DropTargetListener, Transferable {

	/** The Constant supportedFlavors. */
	static final DataFlavor[] supportedFlavors = { null };

	static {
		try {
			ComponentDragDrop.supportedFlavors[0] = new DataFlavor(
					DataFlavor.javaJVMLocalObjectMimeType);
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}

	/** The object. */
	Object object;

	/** The offset. */
	Point offset;

	/** The drag image. */
	Image dragImage;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer
	 * .DataFlavor)
	 */
	@Override
	public Object getTransferData(final DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (flavor.isMimeTypeEqual(DataFlavor.javaJVMLocalObjectMimeType))
			return this.object;
		else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return ComponentDragDrop.supportedFlavors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.
	 * datatransfer.DataFlavor)
	 */
	@Override
	public boolean isDataFlavorSupported(final DataFlavor flavor) {
		return flavor.isMimeTypeEqual(DataFlavor.javaJVMLocalObjectMimeType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent
	 * )
	 */
	@Override
	public void dragEnter(final DropTargetDragEvent ev) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	@Override
	public void dragExit(final DropTargetEvent ev) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent
	 * )
	 */
	@Override
	public void dragOver(final DropTargetDragEvent ev) {
		if (this.object != null) {
			final FormCanvas cv = (FormCanvas) ev.getDropTargetContext()
					.getComponent();
			FormWidget w = null;
			if (this.object instanceof WidgetTemplate) {
				w = ((WidgetTemplate) this.object).createWidgetFromTemplate();
			} else if (this.object instanceof FormWidget) {
				w = (FormWidget) this.object;
			}
			cv.dragOverEvent(ev.getLocation(), w);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	@Override
	public void drop(final DropTargetDropEvent ev) {
		ev.acceptDrop(ev.getDropAction());
		try {
			final Object source = ev.getTransferable().getTransferData(
					ComponentDragDrop.supportedFlavors[0]);
			FormWidget cmp = null;
			final FormCanvas canvas = (FormCanvas) ev.getDropTargetContext()
					.getComponent();
			final FormDesignerPanel container = canvas.getParentPanel();
			final Point loc = new Point(ev.getLocation().x + this.offset.x,
					ev.getLocation().y + this.offset.y);
			if (this.object instanceof WidgetTemplate) {
				cmp = ((WidgetTemplate) this.object).createWidgetFromTemplate();
				cmp.setName(WidgetNameGenerator
						.getName((WidgetTemplate) this.object));
			} else {
				final FormWidget component = (FormWidget) source;
				try {
					final Constructor<? extends FormWidget> c = component
							.getClass().getConstructor(String.class);
					cmp = c.newInstance(WidgetNameGenerator.getName(component));
				} catch (final SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			container.addWidget(cmp, loc);
			container.repaint();
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
		ev.dropComplete(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.
	 * DropTargetDragEvent)
	 */
	@Override
	public void dropActionChanged(final DropTargetDragEvent ev) {
		// dropTargetDrag(ev);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.dnd.DragSourceListener#dragDropEnd(java.awt.dnd.DragSourceDropEvent
	 * )
	 */
	@Override
	public void dragDropEnd(final DragSourceDropEvent ev) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.dnd.DragSourceListener#dragEnter(java.awt.dnd.DragSourceDragEvent
	 * )
	 */
	@Override
	public void dragEnter(final DragSourceDragEvent ev) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.dnd.DragSourceListener#dragExit(java.awt.dnd.DragSourceEvent)
	 */
	@Override
	public void dragExit(final DragSourceEvent ev) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.dnd.DragSourceListener#dragOver(java.awt.dnd.DragSourceDragEvent
	 * )
	 */
	@Override
	public void dragOver(final DragSourceDragEvent ev) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.dnd.DragSourceListener#dropActionChanged(java.awt.dnd.
	 * DragSourceDragEvent)
	 */
	@Override
	public void dropActionChanged(final DragSourceDragEvent arg0) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.dnd.DragGestureListener#dragGestureRecognized(java.awt.dnd.
	 * DragGestureEvent)
	 */
	@Override
	public void dragGestureRecognized(final DragGestureEvent ev) {
		if (ev.getComponent() instanceof JList) {
			final JList list = (JList) ev.getComponent();
			if (!list.isSelectionEmpty()) {
				this.object = list.getSelectedValue();
				FormWidget w = null;
				if (this.object instanceof WidgetTemplate) {
					w = ((WidgetTemplate) this.object)
							.createWidgetFromTemplate();
				} else {
					w = (FormWidget) this.object;
				}
				final Image di = w.getUi().getAsImage();
				final int idx = list.getSelectedIndex();
				final Rectangle cell = list.getCellBounds(idx, idx);
				final Point po = new Point(-ev.getDragOrigin().x + cell.x,
						-ev.getDragOrigin().y + cell.y);
				this.offset = po;
				this.dragImage = di;
				ev.startDrag(null, di, po, this, this);

			}
		}

	}

}
