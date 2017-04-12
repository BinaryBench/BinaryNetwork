package me.binarynetwork.core.common;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * Created by Bench on 10/3/2016.
 */
public class NullableConsumer<T> implements Consumer<T> {
    private Consumer<T> consumer;

    public NullableConsumer(@Nullable Consumer<T> consumer)
    {
        this.consumer = consumer;
    }

    @Override
    public void accept(T t)
    {
        if (consumer != null)
            consumer.accept(t);
    }
}
