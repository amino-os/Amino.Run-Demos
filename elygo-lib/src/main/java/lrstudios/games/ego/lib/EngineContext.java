package lrstudios.games.ego.lib;

import java.io.Serializable;

/**
 * Created by howell on 3/5/18.
 */

public class EngineContext implements Serializable {
    private static final long serialVersionUID = 20180224L;
    public long getMemoryLimit() {
        //max 920MB on test dev hw-Fire; 256MB seems reasonable value for Android
        // 4096MG seems fine on a decent PC
        return 1024 * 1024 * 512; // 512MB RSS
        //return 1024 * 1024 * 1024 * 4; // 4GB RSS
    }

    public String getDir() {
        return "/data/data/net.lrstudios.android.pachi/app_engines";
    }
}
