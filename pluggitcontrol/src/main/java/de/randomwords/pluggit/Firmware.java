package de.randomwords.pluggit;

/**
 * We hold the firmware version of the Pluggit in this class.
 * <p>
 * Created by Ronny Polley on 19.12.2016.
 *
 * @author Ronny Polley
 */
public class Firmware {

    private final int majorVersion, minorVersion;

    public Firmware(int majorVersion, int minorVersion) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    @Override
    public String toString() {
        return majorVersion + "." + minorVersion;
    }
}
