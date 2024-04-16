package top.pas.mcosu.osu.utils;

import java.nio.file.Path;

public record AudioObject(
        Path audioFile,
        int leadInTicks
) {}
