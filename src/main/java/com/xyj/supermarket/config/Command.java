package com.xyj.supermarket.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Data
public class Command {
    private int action;

    public static Command of(Action action) {
        return Command.of(action.getType());
    }

}
