package org.example;

import org.hamcrest.Factory;
import org.jetbrains.annotations.NotNull;

public interface Favorite extends Comparable<Favorite>{
    public String getName();

    @Override
    int compareTo(@NotNull Favorite o);
}
