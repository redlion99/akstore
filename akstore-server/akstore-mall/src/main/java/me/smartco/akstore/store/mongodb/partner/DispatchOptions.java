package me.smartco.akstore.store.mongodb.partner;

/**
 * Created by libin on 14-12-26.
 */
public class DispatchOptions {

    private boolean dispatchMorning;
    private boolean dispatchNoon;
    private boolean dispatchAfternoon;

    public DispatchOptions() {
    }

    public DispatchOptions(boolean dispatchMorning, boolean dispatchNoon, boolean dispatchAfternoon) {
        this.dispatchMorning = dispatchMorning;
        this.dispatchNoon = dispatchNoon;
        this.dispatchAfternoon = dispatchAfternoon;
    }

    public boolean isDispatchMorning() {
        return dispatchMorning;
    }

    public void setDispatchMorning(boolean dispatchMorning) {
        this.dispatchMorning = dispatchMorning;
    }

    public boolean isDispatchNoon() {
        return dispatchNoon;
    }

    public void setDispatchNoon(boolean dispatchNoon) {
        this.dispatchNoon = dispatchNoon;
    }

    public boolean isDispatchAfternoon() {
        return dispatchAfternoon;
    }

    public void setDispatchAfternoon(boolean dispatchAfternoon) {
        this.dispatchAfternoon = dispatchAfternoon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DispatchOptions that = (DispatchOptions) o;

        if (dispatchAfternoon != that.dispatchAfternoon) return false;
        if (dispatchMorning != that.dispatchMorning) return false;
        if (dispatchNoon != that.dispatchNoon) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (dispatchMorning ? 1 : 0);
        result = 31 * result + (dispatchNoon ? 1 : 0);
        result = 31 * result + (dispatchAfternoon ? 1 : 0);
        return result;
    }
}
