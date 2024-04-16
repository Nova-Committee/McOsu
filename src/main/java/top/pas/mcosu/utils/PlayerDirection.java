package top.pas.mcosu.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;

public enum PlayerDirection {
    PositiveX,
    NegativeX,
    PositiveZ,
    NegativeZ;

    public static PlayerDirection fromYaw(@Range(from = -180, to = 180) float yaw) {
        yaw = yaw % 180;
        if (yaw <= 45 && yaw > -135)
            if (yaw > -45)
                return PositiveZ;
            else
                return PositiveX;
        else
            if (yaw <= 135)
                return NegativeX;
            else
                return NegativeZ;
    }

    @Contract(pure = true)
    public PlayerDirection getReverse() {
        PlayerDirection result = this;
        switch (this) {
            case PositiveX -> result = NegativeX;
            case PositiveZ -> result = NegativeZ;
            case NegativeX -> result = PositiveX;
            case NegativeZ -> result = PositiveZ;
        }
        return result;
    }
}
