package autorepair.state.edge;

import org.openqa.selenium.*;

import java.io.*;
import java.util.*;

public class Event implements Serializable {

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    private EventType eventType;
    private static final long serialVersionUID = 123411166663488L;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getSourceVertexId() {
        return sourceVertexId;
    }

    public void setSourceVertexId(int sourceVertexId) {
        this.sourceVertexId = sourceVertexId;
    }

    public int getTargetVertexId() {
        return targetVertexId;
    }

    public void setTargetVertexId(int targetVertexId) {
        this.targetVertexId = targetVertexId;
    }

    public Identification getIdentification() {
        return identification;
    }

    public void setIdentification(Identification identification) {
        this.identification = identification;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCodeLine() {
        return codeLine;
    }

    public void setCodeLine(String codeLine) {
        this.codeLine = codeLine;
    }

    private int eventId;
    private int sourceVertexId;
    private int targetVertexId;
    private Identification identification;
    private String method;
    private String codeLine;

    public String getAbsoluteXpath() {
        return absoluteXpath;
    }

    public void setAbsoluteXpath(String absoluteXpath) {
        this.absoluteXpath = absoluteXpath;
    }

    public int getElementId() {
        return elementId;
    }

    public void setElementId(int elementId) {
        this.elementId = elementId;
    }

    private String absoluteXpath;
    private int elementId;

    public List<Object> getArguments() {
        return arguments;
    }

    public void setArguments(List<Object> arguments) {
        this.arguments = arguments;
    }

    private List<Object> arguments;

    public Event(
            int sourceVertexId,
            int targetVertexId,
            Identification identification,
            String method,
            String codeLine,
            String absoluteXpath,
            int elementId,
            EventType eventType,
            List<Object> arguments
    ) {
        this.sourceVertexId = sourceVertexId;
        this.targetVertexId = targetVertexId;
        this.identification = identification;
        this.method = method;
        this.codeLine = codeLine;
        this.absoluteXpath = absoluteXpath;
        this.elementId = elementId;
        this.eventType = eventType;
        this.arguments = arguments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        return sourceVertexId == event.sourceVertexId &&
               targetVertexId == event.targetVertexId &&
               Objects.equals(identification, event.identification) &&
               Objects.equals(method, event.method) &&
               Objects.equals(codeLine, event.codeLine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceVertexId, targetVertexId, identification, method, codeLine);
    }

    @Override
    public String toString() {
        return "Event{" +
               "eventType=" +
               eventType +
               ", eventId=" +
               eventId +
               ", sourceVertexId=" +
               sourceVertexId +
               ", targetVertexId=" +
               targetVertexId +
               ", identification=" +
               identification +
               ", method='" +
               method +
               "'" +
               ", arguments=" +
               arguments +
               '}';
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        List<Object> serializableArguments = new ArrayList<>();

        for (Object argument : arguments) {
            if (argument instanceof By) {
                By by = (By) argument;
                String serialized = serializeBy(by);
                serializableArguments.add(serialized);
            } else {
                serializableArguments.add(argument);
            }
        }

        this.arguments = serializableArguments;
        out.defaultWriteObject();
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        List<Object> deserializedArguments = new ArrayList<>();
        for (Object argument : arguments) {
            if (argument instanceof String && ((String) argument).startsWith("BY::")) {
                deserializedArguments.add(deserializeBy((String) argument));
            } else {
                deserializedArguments.add(argument);
            }
        }

        this.arguments = deserializedArguments;
    }

    private String serializeBy(By by) {
        String value = by.toString(); // e.g.: "By.className: my-class"
        if (value.startsWith("By.")) {
            return "BY::" + value.substring(3); // → "BY::className: my-class"
        }
        return null;
    }

    private By deserializeBy(String serialized) {
        if (!serialized.startsWith("BY::")) {
            return null;
        }

        String content = serialized.substring(4); // "className: my-class"
        String[] parts = content.split(":", 2);
        String type = parts[0].trim();
        String value = parts.length > 1 ? parts[1].trim() : "";

        return switch (type) {
            case "id" -> By.id(value);
            case "name" -> By.name(value);
            case "className" -> By.className(value);
            case "cssSelector" -> By.cssSelector(value);
            case "linkText" -> By.linkText(value);
            case "partialLinkText" -> By.partialLinkText(value);
            case "tagName" -> By.tagName(value);
            case "xpath" -> By.xpath(value);
            default -> throw new IllegalArgumentException("Unknown By type: " + type);
        };
    }
}
