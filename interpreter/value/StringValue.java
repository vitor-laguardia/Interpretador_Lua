package interpreter.value;

public class StringValue extends Value<String> {

    private String value;

    public StringValue(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return this.value;
    }

    @Override
    public boolean eval() {
        return true;
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof StringValue) {
            return this.value.equals(((StringValue) obj).value);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}