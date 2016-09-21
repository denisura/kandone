package com.github.denisura.kandone.data.database;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;


public interface TaskColumns {

    @DataType(INTEGER)
    @AutoIncrement
    @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
    String _ID = "_id";

    @DataType(TEXT)
    @NotNull
    String TASK = "task";

    @DataType(TEXT)
    @NotNull
    String NOTES = "notes";

    @DataType(INTEGER)
    @NotNull
    String DUEDATE = "duedate";

    @DataType(INTEGER)
    @NotNull
    String PRIORITY = "priority";

    @DataType(INTEGER)
    String DONEDATE = "donedate";
}
