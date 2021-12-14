package interpreter.value;

public class BooleanValue extends Value<Boolean> {

    private Boolean value;

    public BooleanValue(Boolean value) {
        this.value = value;
    }

    @Override
    public Boolean value() {
        return this.value;
    }

    @Override
    public boolean eval() {
        return this.value.booleanValue();
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof BooleanValue) {
            return this.value.booleanValue() == ((BooleanValue) obj).value.booleanValue();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}