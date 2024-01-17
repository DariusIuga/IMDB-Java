package org.example;

import org.jetbrains.annotations.NotNull;

public interface CommonInterface extends Comparable<CommonInterface>{
    public String getName();

    @Override
    int compareTo(@NotNull CommonInterface o);
}
