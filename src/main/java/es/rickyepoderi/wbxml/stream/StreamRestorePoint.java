/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *    
 * Linking this library statically or dynamically with other modules 
 * is making a combined work based on this library. Thus, the terms and
 * conditions of the GNU General Public License cover the whole
 * combination.
 *    
 * As a special exception, the copyright holders of this library give 
 * you permission to link this library with independent modules to 
 * produce an executable, regardless of the license terms of these 
 * independent modules, and to copy and distribute the resulting 
 * executable under terms of your choice, provided that you also meet, 
 * for each linked independent module, the terms and conditions of the 
 * license of that module.  An independent module is a module which 
 * is not derived from or based on this library.  If you modify this 
 * library, you may extend this exception to your version of the 
 * library, but you are not obligated to do so.  If you do not wish 
 * to do so, delete this exception statement from your version.
 *
 * Project: github.com/rickyepoderi/wbxml-stream
 * 
 */
package es.rickyepoderi.wbxml.stream;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * <p>Class that represents a backup of the status in a WbXmlStreamReader.
 * With an object of this class you can revert the status of a stream reader
 * to the point when this point was saved. This is useful to cover implications
 * for filters in the StAX definition.</p>
 * 
 * <p>Mainly the data saved in a backup object are the following:</p>
 * 
 * <ul>
 * <li>The current event in the stream reader.</li>
 * <li>The current element index that the reader was in that moment. Remember
 * that the index element is the WbXmlElemnt being processing in that point
 * plus two indexes (current attr being processed and current content).</li>
 * <li>The parents element index. A content of a WbXmlElement can be another
 * element, so the parents are stored in a queue to continue its processing
 * when this elemenent is over.</li>
 * </ul>
 * 
 * @author ricky
 */
public class StreamRestorePoint {
    
    /**
     * The event the reader is right now.
     */
    private int event;
    
    /**
     * Queue of elements to iterate over all children.
     */
    private Deque<WbXmlStreamReader.ElementIndex> parents;
    
    /**
     * Current element index (current element and index of the content).
     */
    private WbXmlStreamReader.ElementIndex elementIndex;
    
    /**
     * Constructor using the properties. The properties should be cloned.
     * @param event The current event of the stream reader
     * @param parents The current parent of the stream reader
     * @param elementIndex The current element index of the stream reader
     */
    public StreamRestorePoint(int event, Deque<WbXmlStreamReader.ElementIndex> parents,
            WbXmlStreamReader.ElementIndex elementIndex) {
        this.event = event;
        this.elementIndex = new WbXmlStreamReader.ElementIndex(elementIndex);
        this.parents = new ArrayDeque<WbXmlStreamReader.ElementIndex>();
        for (WbXmlStreamReader.ElementIndex e: parents) {
            this.parents.add(new WbXmlStreamReader.ElementIndex(e));
        }
    }

    /**
     * Getter for the event.
     * @return The event saved
     */
    public int getEvent() {
        return event;
    }

    /**
     * Getter for the parents.
     * @return The parents saved
     */
    public Deque<WbXmlStreamReader.ElementIndex> getParents() {
        return parents;
    }

    /**
     * Getter for the element index.
     * @return The element index saved
     */
    public WbXmlStreamReader.ElementIndex getElementIndex() {
        return elementIndex;
    }
    
    @Override
    public String toString() {
        return new StringBuilder()
                .append(this.getClass().getSimpleName())
                .append(": ")
                .append(event)
                .append(" ")
                .append(elementIndex)
                .append(" ")
                .append(parents)
                .toString();
    }
    
}
