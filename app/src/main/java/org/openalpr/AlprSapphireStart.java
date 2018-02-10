package org.openalpr;

import sapphire.app.AppEntryPoint;
import sapphire.app.AppObjectNotCreatedException;
import sapphire.common.AppObjectStub;

import static sapphire.runtime.Sapphire.new_;

/**
 * Open ALPR Sapphire wrapper.
 */
public class AlprSapphireStart implements AppEntryPoint {

    @Override
    public AppObjectStub start() throws AppObjectNotCreatedException {
        return (AppObjectStub) new_(AlprSapphire.class);
    }
}