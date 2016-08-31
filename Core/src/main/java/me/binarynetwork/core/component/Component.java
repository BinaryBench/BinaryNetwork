package me.binarynetwork.core.component;

/**
 * Created by Bench on 8/29/2016.
 */
public interface Component {

    /**
     * Enables the component
     *
     * @return false if the component was already enabled; true otherwise.
     */
    boolean enable();

    /**
     * Checks if the component is enabled.
     *
     * @return Whether the component is enabled
     */
    boolean isEnabled();

    /**
     * Disables the component
     *
     * @return false if the component was not enabled; true otherwise.
     */
    boolean disable();
}
