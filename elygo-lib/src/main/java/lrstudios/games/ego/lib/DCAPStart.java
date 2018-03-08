package lrstudios.games.ego.lib;

import sapphire.app.AppEntryPoint;
import sapphire.app.AppObjectNotCreatedException;
import sapphire.common.AppObjectStub;
import sapphire.runtime.Sapphire;

/**
 * Created by howell on 3/4/18.
 */

public class DCAPStart implements AppEntryPoint {
    @Override
    public AppObjectStub start() throws AppObjectNotCreatedException {
        return (AppObjectStub) Sapphire.new_(GtpEngineManager.class);
    }
}
