package org.example.prow.wikimedia;

public enum EventEntity {
    REVISION, PAGE, USER;



    public static EventEntity fromString(String str) throws Exception {
        switch (str) {
            case "revision":
                return REVISION;
            case "page":
                return PAGE;
            case "user":
                return USER;
            default:
                throw new Exception("UNKOWN ENTITY: " + str);
        }
    }

    @Override
    public String toString() {
        return "EventEntity{}";
    }
}
