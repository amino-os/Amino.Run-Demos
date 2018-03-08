package lrstudios.games.ego.lib;

import java.io.Serializable;

/**
 * Created by howell on 3/5/18.
 */

public class EngineContext implements Serializable {
    private static final long serialVersionUID = 20180224L;
    public long getMemoryLimit() {
        //920MB on test dev hw-Fire
        return 1024 * 1024 * 512; // 512MB RSS
    }

    public String getDir() {
        return "/data/data/net.lrstudios.android.pachi/app_engines";
    }
}
