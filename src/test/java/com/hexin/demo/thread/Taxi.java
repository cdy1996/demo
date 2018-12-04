package com.hexin.demo.thread;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Taxi {
    private Point location,desnation;
    private final Dispathcer dispathcer;

    public Taxi(Dispathcer dispathcer) {
        this.dispathcer = dispathcer;
    }

    public synchronized Point getLocation(){
        return location;
    }

    public synchronized void setLocation(Point location) {
        boolean reachedDestination;
        synchronized (this) {
            this.location = location;
            reachedDestination = location.equals(desnation);
        }
        if (reachedDestination) {
            dispathcer.notifyAvailable(this);
        }
    }
}
class Dispathcer{
    private final Set<Taxi> taxis;
    private final Set<Taxi> availableTaix;

    public Dispathcer(Set<Taxi> taxis, Set<Taxi> availableTaix) {
        this.taxis = taxis;
        this.availableTaix = availableTaix;
    }

    public synchronized void notifyAvailable(Taxi taxi) {
        availableTaix.add(taxi);
    }

    public Image getImage(){
        Set<Taxi> copy;
        synchronized (this) {
            copy = new HashSet<Taxi>(taxis);
        }
        Image image = new Image();
        for (Taxi taxi : copy) {
            image.drawMarker(taxi.getLocation());
        }
        return image;
    }
}
class Image{
    public Image drawMarker(Point location) {
        return new Image();
    }
}