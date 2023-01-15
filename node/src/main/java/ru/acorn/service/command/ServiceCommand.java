package ru.acorn.service.command;

public enum ServiceCommand {
    HELP("/help"),
    START("/start"),
    CANCEL("/cancel");

    private String command;

    ServiceCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return command;
    }
}
