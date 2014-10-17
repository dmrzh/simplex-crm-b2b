package ru.dev_server.client.utils;

import ch.qos.logback.core.AppenderBase;

import java.awt.*;

/**
 * Do beep.
 */
public class BeepAppender extends AppenderBase {
    @Override
    protected void append(Object eventObject) {

        Toolkit.getDefaultToolkit().beep();
    }
}
