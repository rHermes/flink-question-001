package org.example.prow.wikimedia;

public enum EventType {
    ALTERBLOCKS, ALTERGROUPS, CREATE, DELETE, MOVE, RENAME, RESTORE, MERGE;


    public static EventType fromString(String str) throws Exception {
        switch (str) {
            case "create":
                return CREATE;
            case "merge":
                return MERGE;
            case "delete":
                return DELETE;
            case "move":
                return MOVE;
            case "rename":
                return RENAME;
            case "restore":
                return RESTORE;
            case "altergroups":
                return ALTERGROUPS;
            case "alterblocks":
                return ALTERBLOCKS;
            default:
                throw new Exception("UNKOWN TYPE: " + str);
        }
    }
}
