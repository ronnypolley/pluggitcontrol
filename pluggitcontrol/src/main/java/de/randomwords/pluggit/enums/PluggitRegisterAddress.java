package de.randomwords.pluggit.enums;

/**
 * Enum representing the modbus adresses of the different parts.
 * <p>
 * Created by Ronny Polley on 19.12.2016.
 *
 * @author Ronny Polley
 */
public enum PluggitRegisterAddress {
    IPAddress(40029),
    Firmware(40025),
    CurrentDateTime(40109),
    Worktime(40625),
    OperationMode(40473),
    OutdoorTemp(40133),
    ExtractTemp(40137),
    ExhaustTemp(40139),
    SupplyTemp(40135),
    FilterRemainingTime(40555),
    ActiveAlarm(40517),
    Fan1Speed(40101),
    Fan2Speed(40103);


    private final int pmr;

    PluggitRegisterAddress(int pmr) {
        this.pmr = pmr;
    }

    public int getPmr() {
        return pmr;
    }
}
