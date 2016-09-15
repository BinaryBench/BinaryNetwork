package me.binarynetwork.core.database.old;

import me.binarynetwork.core.database.old.DatabaseCall;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Bench on 9/9/2016.
 */
public class DatabaseCallData {
    private DatabaseCall call;
    private Runnable onFail;

    public DatabaseCallData(@Nonnull DatabaseCall call, @Nullable Runnable onFail)
    {
        this.call = call;
        this.onFail = onFail;
    }

    public void execute(Connection connection) throws SQLException
    {
        if (call != null)
            call.execute(connection);
    }

    public void onFail()
    {
        if (onFail != null)
            onFail.run();
    }
}
