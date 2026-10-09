package pl.fuzjajadrowa.atomcraft;

public enum MachineKind {
    GENERATOR("generator"), CRUSHER("crusher"), ELECTRIC_FURNACE("electric_furnace");
    public final String id;
    MachineKind(String id) { this.id = id; }
}