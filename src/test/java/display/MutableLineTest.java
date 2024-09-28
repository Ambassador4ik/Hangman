package display;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MutableLineTest {

    @Test
    public void testConstructorEmpty() {
        MutableLine<String> line = new MutableLine<>();
        assertNotNull(line);
        assertTrue(line.content().isEmpty());
        assertEquals(" ", line.sep());
    }

    @Test
    public void testConstructorWithSeparator() {
        String separator = ",";
        MutableLine<String> line = new MutableLine<>(separator);
        assertNotNull(line);
        assertTrue(line.content().isEmpty());
        assertEquals(separator, line.sep());
    }

    @Test
    public void testConstructorWithInitialContent() {
        List<String> initialContent = Arrays.asList("Hello", "World");
        MutableLine<String> line = new MutableLine<>(initialContent);
        assertNotNull(line);
        assertEquals(initialContent, line.content());
        assertEquals(" ", line.sep());
    }

    @Test
    public void testConstructorWithInitialContentAndSeparator() {
        List<String> initialContent = Arrays.asList("Hello", "World");
        String separator = ",";
        MutableLine<String> line = new MutableLine<>(initialContent, separator);
        assertNotNull(line);
        assertEquals(initialContent, line.content());
        assertEquals(separator, line.sep());
    }

    @Test
    public void testSetContent() {
        MutableLine<String> line = new MutableLine<>();
        List<String> newContent = Arrays.asList("A", "B", "C");
        line.setContent(newContent);
        assertEquals(newContent, line.content());
    }

    @Test
    public void testAddContent() {
        MutableLine<String> line = new MutableLine<>();
        line.addContent("Test");
        assertEquals(List.of("Test"), line.content());
    }

    @Test
    public void testAddContentAtIndex() {
        MutableLine<String> line = new MutableLine<>();
        line.addContent("A");
        line.addContent("C");
        line.addContent(1, "B");
        assertEquals(Arrays.asList("A", "B", "C"), line.content());
    }

    @Test
    public void testAddContentAtIndexOutOfBounds() {
        MutableLine<String> line = new MutableLine<>();
        assertThrows(IndexOutOfBoundsException.class, () -> {
            line.addContent(1, "Test");
        });
    }

    @Test
    public void testRemoveContentByItem() {
        MutableLine<String> line = new MutableLine<>(Arrays.asList("A", "B", "C"));
        boolean removed = line.removeContent("B");
        assertTrue(removed);
        assertEquals(Arrays.asList("A", "C"), line.content());
    }

    @Test
    public void testRemoveContentByItemNotFound() {
        MutableLine<String> line = new MutableLine<>(Arrays.asList("A", "B", "C"));
        boolean removed = line.removeContent("D");
        assertFalse(removed);
        assertEquals(Arrays.asList("A", "B", "C"), line.content());
    }

    @Test
    public void testRemoveContentByIndex() {
        MutableLine<String> line = new MutableLine<>(Arrays.asList("A", "B", "C"));
        String removedItem = line.removeContent(1);
        assertEquals("B", removedItem);
        assertEquals(Arrays.asList("A", "C"), line.content());
    }

    @Test
    public void testRemoveContentByIndexOutOfBounds() {
        MutableLine<String> line = new MutableLine<>(Arrays.asList("A", "B", "C"));
        assertThrows(IndexOutOfBoundsException.class, () -> {
            line.removeContent(3);
        });
    }

    @Test
    public void testSetContentAtIndex() {
        MutableLine<String> line = new MutableLine<>(Arrays.asList("A", "B", "C"));
        line.setContent(1, "D");
        assertEquals(Arrays.asList("A", "D", "C"), line.content());
    }

    @Test
    public void testSetContentAtIndexOutOfBounds() {
        MutableLine<String> line = new MutableLine<>(Arrays.asList("A", "B", "C"));
        assertThrows(IndexOutOfBoundsException.class, () -> {
            line.setContent(3, "D");
        });
    }

    @Test
    public void testReplaceContent() {
        MutableLine<String> line = new MutableLine<>(Arrays.asList("A", "B", "A", "C"));
        int count = line.replaceContent("A", "D");
        assertEquals(2, count);
        assertEquals(Arrays.asList("D", "B", "D", "C"), line.content());
    }

    @Test
    public void testReplaceContentNoMatch() {
        MutableLine<String> line = new MutableLine<>(Arrays.asList("A", "B", "C"));
        int count = line.replaceContent("D", "E");
        assertEquals(0, count);
        assertEquals(Arrays.asList("A", "B", "C"), line.content());
    }

    @Test
    public void testClearContent() {
        MutableLine<String> line = new MutableLine<>(Arrays.asList("A", "B", "C"));
        line.clearContent();
        assertTrue(line.content().isEmpty());
    }

    @Test
    public void testGetContentString() {
        List<String> initialContent = Arrays.asList("Hello", "World");
        MutableLine<String> line = new MutableLine<>(initialContent);
        String expected = "Hello World";
        assertEquals(expected, line.getContent());
    }

    @Test
    public void testGetContentStringWithSeparator() {
        List<String> initialContent = Arrays.asList("Hello", "World");
        String separator = ",";
        MutableLine<String> line = new MutableLine<>(initialContent, separator);
        String expected = "Hello,World";
        assertEquals(expected, line.getContent());
    }

    @Test
    public void testGetContentStringEmpty() {
        MutableLine<String> line = new MutableLine<>();
        String expected = "[Empty Mutable Line]";
        assertEquals(expected, line.getContent());
    }

    @Test
    public void testListeners() {
        MutableLine<String> line = new MutableLine<>();
        TestLineListener listener = new TestLineListener();
        line.addListener(listener);

        line.addContent("Test");
        assertTrue(listener.notified());

        listener.reset();
        line.removeListener(listener);
        line.addContent("Another Test");
        assertFalse(listener.notified());
    }

    // Inner class to test listeners
    @Getter
    private static class TestLineListener implements LineListener {
        private boolean notified = false;

        @Override
        public void onLineChanged(Line line) {
            notified = true;
        }

        public void reset() {
            notified = false;
        }
    }
}
