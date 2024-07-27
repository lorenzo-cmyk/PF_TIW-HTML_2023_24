package it.polimi.tiw.frontend.filters;

/**
 * This enumeration is used to define the types of messages that can be displayed to the user.
 */
public enum MessageTypesEnumeration {
    /**
     * The message is the default message for the page
     */
    DEFAULT(0),
    /**
     * The message is a success message
     */
    SUCCESS(1),
    /**
     * The message is an error message
     */
    ERROR(2),
    /**
     * The message is a warning message
     */
    WARNING(3);

    /**
     * The integer value associated with the enumeration value
     */
    private final int value;

    /**
     * This constructor initializes the enumeration value with the integer value passed as a parameter.
     *
     * @param value the integer value associated with the enumeration value
     */
    MessageTypesEnumeration(int value) {
        this.value = value;
    }

    /**
     * This method returns the integer value associated with the enumeration value.
     *
     * @return the integer value associated with the enumeration value
     */
    public int getValue() {
        return value;
    }
}
