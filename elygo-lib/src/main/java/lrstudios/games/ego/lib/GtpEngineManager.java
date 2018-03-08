package lrstudios.games.ego.lib;

import android.content.Context;

import java.lang.reflect.InvocationTargetException;

import sapphire.runtime.Sapphire;

/**
 * Created by howell on 2/22/18.
 */

public class GtpEngineManager {
    public GtpEngine getEngine(Class<?> botClass, Context context)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException{
        return (GtpEngine) Sapphire.new_(botClass, context);
    }
}
