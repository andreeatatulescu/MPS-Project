package io.mps.project.image;

public enum ThresholdType {
    OTSU(0),
    KITLER(1),
    LLOYD(2),
    SUNG(3),
    RIDLER(4),
    HUANG(5),
    RAMESH(6),
    LI1(7),
    LI2(8),
    BRINK(9),
    KAPUR(10),
    SAHOO(11),
    SHANBHAG(12),
    YEN(13),
    TSAI(14);

    private final int value;

    ThresholdType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static int count() {
        return values().length;
    }

    public static ThresholdType valueOf(int value) {
        return values()[value];
    }
}
