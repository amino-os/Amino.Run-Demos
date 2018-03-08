package lrstudios.games.ego.lib;

import android.content.Context;

import java.lang.reflect.InvocationTargetException;

import sapphire.app.SapphireObject;
import sapphire.runtime.Sapphire;

/**
 * Created by howell on 3/3/18.
 */

public class GtpEngineManager implements SapphireObject{
    public GtpEngine getEngine(Class<?> botClass, EngineContext context) {
        try {
            return (GtpEngine) Sapphire.new_(botClass, context);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
