package ru.acorn.service.command;

public enum ServiceCommand {
    HELP("/help"),
    START("/start"),
    CANCEL("/cancel"),
    REGISTRATION("/registration");

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

    public boolean equalCommand(String command){
        return this.command.equals(command);
    }
}
