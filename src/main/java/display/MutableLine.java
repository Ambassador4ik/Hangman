package display;

import lombok.Getter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Represents a mutable line with content that can be modified.
 *
 * @param <T> The type of elements in the content.
 */
@Getter
public class MutableLine<T> implements Line {
    private final String sep;
    private final List<T> content;
    private final List<LineListener> listeners = new CopyOnWriteArrayList<>();

    /**
     * Constructs an empty MutableLine.
     */
    public MutableLine() {
        this.content = new ArrayList<>();
        this.sep = " ";
    }

    public MutableLine(String separator) {
        this.content = new ArrayList<>();
        this.sep = separator;
    }

    /**
     * Constructs a MutableLine with initial content.
     *
     * @param initialContent The initial elements of the line.
     */
    public MutableLine(Collection<T> initialContent) {
        this.content = new ArrayList<>(initialContent);
        this.sep = " ";
    }

    public MutableLine(Collection<T> initialContent, String separator) {
        this.content = new ArrayList<>(initialContent);
        this.sep = separator;
    }

    @Override
    public String getContent() {
        if (content.isEmpty()) {
            return "[Empty Mutable Line]";
        }
        return content.stream()
                      .map(Object::toString)
                      .collect(Collectors.joining(sep));
    }

    // Listener registration methods

    /**
     * Adds a listener to be notified of content changes.
     *
     * @param listener The listener to add.
     */
    public void addListener(LineListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener from notifications.
     *
     * @param listener The listener to remove.
     */
    public void removeListener(LineListener listener) {
        listeners.remove(listener);
    }

    // Notify listeners of changes

    /**
     * Notifies all registered listeners about a content change.
     */
    private void notifyListeners() {
        for (LineListener listener : listeners) {
            listener.onLineChanged(this);
        }
    }

    // Content modification methods

    /**
     * Adds an item to the end of the content.
     *
     * @param item The item to add.
     */
    public void addContent(T item) {
        content.add(item);
        notifyListeners();
    }

    /**
     * Adds an item at a specific index in the content.
     *
     * @param index The position to insert the item.
     * @param item  The item to add.
     * @throws IndexOutOfBoundsException If the index is out of range.
     */
    public void addContent(int index, T item) {
        if (index < 0 || index > content.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        content.add(index, item);
        notifyListeners();
    }

    /**
     * Removes the first occurrence of the specified item from the content.
     *
     * @param item The item to remove.
     * @return true if the item was removed, false otherwise.
     */
    public boolean removeContent(T item) {
        boolean removed = content.remove(item);
        if (removed) {
            notifyListeners();
        }
        return removed;
    }

    /**
     * Removes the item at the specified index from the content.
     *
     * @param index The index of the item to remove.
     * @return The removed item.
     * @throws IndexOutOfBoundsException If the index is out of range.
     */
    public T removeContent(int index) {
        if (index < 0 || index >= content.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        T removedItem = content.remove(index);
        notifyListeners();
        return removedItem;
    }

    /**
     * Sets the item at the specified index to a new value.
     *
     * @param index   The index of the item to set.
     * @param newItem The new item to replace the old one.
     * @throws IndexOutOfBoundsException If the index is out of range.
     */
    public void setContent(int index, T newItem) {
        if (index < 0 || index >= content.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        content.set(index, newItem);
        notifyListeners();
    }

    /**
     * Replaces all items that match the specified oldItem with newItem.
     *
     * @param oldItem The item to be replaced.
     * @param newItem The item to replace with.
     * @return The number of items replaced.
     */
    public int replaceContent(T oldItem, T newItem) {
        int count = 0;
        for (int i = 0; i < content.size(); i++) {
            if (content.get(i).equals(oldItem)) {
                content.set(i, newItem);
                count++;
            }
        }
        if (count > 0) {
            notifyListeners();
        }
        return count;
    }

    /**
     * Clears all content from the line.
     */
    public void clearContent() {
        if (!content.isEmpty()) {
            content.clear();
            notifyListeners();
        }
    }

    /**
     * Sets the entire content to a new collection of items.
     *
     * @param newContent The new collection of items.
     */
    public void setContent(Collection<T> newContent) {
        content.clear();
        content.addAll(newContent);
        notifyListeners();
    }
}
