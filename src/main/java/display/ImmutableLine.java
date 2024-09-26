package display;

import lombok.Getter;

@Getter
public class ImmutableLine implements Line {
    private final String content;

    public ImmutableLine(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }
}
