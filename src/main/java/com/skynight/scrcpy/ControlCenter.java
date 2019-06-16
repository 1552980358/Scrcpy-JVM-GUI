package com.skynight.scrcpy;

public class ControlCenter {

    private static ControlCenter instance;
    public static synchronized ControlCenter getInstance() {
        if (instance == null) {
            instance = new ControlCenter();
        }
        return instance;
    }

    private ControlCenter() {

    }

    public ControlListener controlListener;
    public synchronized void setControlListener(ControlListener controlListener) {
        this.controlListener = controlListener;
    }
    public ControlListener getControlListener() {
        return controlListener;
    }

    private volatile boolean wiredMethod = true;
    public synchronized void setWiredMethod(boolean wiredMethod) {
        this.wiredMethod = wiredMethod;
    }
    public boolean isWiredMethod() {
        return wiredMethod;
    }
}
