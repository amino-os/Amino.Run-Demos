package net.lrstudios.android.pachi;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import lrstudios.games.ego.lib.EngineContext;
import lrstudios.games.ego.lib.ExternalGtpEngine;
import lrstudios.games.ego.lib.Utils;
import lrstudios.util.android.AndroidUtils;
import sapphire.app.SapphireObject;

import java.io.*;
import java.util.Hashtable;
import java.util.Properties;


public class PachiEngine extends ExternalGtpEngine implements SapphireObject {
    private static final String TAG = "PachiEngine";

    /**
     * Increment this counter each time you update the pachi executable
     * (this will force the app to extract it again).
     */
    private static final int EXE_VERSION = 5;

    private static final String ENGINE_NAME = "Pachi";
    private static final String ENGINE_VERSION = "10.00";

    private static final String PREF_KEY_VERSION = "pachi_exe_version";

    private int _time = 600;
    private int _maxTreeSize = 256;


    public PachiEngine(EngineContext context) {
        super(context);

        long totalRam = context.getMemoryLimit();

        // The amount of RAM used by pachi (adjustable with max_tree_size) should not
        // be too high compared to the total RAM available, because Android can kill a
        // process at any time if it uses too much memory.
        if (totalRam > 0)
            _maxTreeSize = Math.max(256, (int) Math.round(totalRam / 1024.0 / 1024.0 * 0.5));
    }

    @Override
    public boolean init(Hashtable<String, String> properties) {
        int level = Utils.tryParseInt(properties.get("level"), 5);
        int boardsize = Utils.tryParseInt(properties.get("boardsize"), 9);

        _time = (int) Math.round((boardsize * 1.5) * (0.5 + level / 10.0));
        return super.init(properties);
    }

    @Override
    protected String[] getProcessArgs() {
        return new String[]{"-t", "" + _time, "max_tree_size=" + _maxTreeSize};
    }


    @Override
    protected File getEngineFile() {
        File dir = new File(_context.getDir());

        File file = new File(dir, "pachi");
// todo: enable the engine file update
/*
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_context);
        int version = prefs.getInt(PREF_KEY_VERSION, 0);
        if (version < EXE_VERSION) {
            if (file.exists())
                file.delete();
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                outputStream = new BufferedOutputStream(new FileOutputStream(file), 4096);
                inputStream = new BufferedInputStream(_context.getResources().openRawResource(R.raw.pachi), 4096);
                Utils.copyStream(inputStream, outputStream, 4096);

                extractRawResToFile(R.raw.libcaffe, dir,"libcaffe.so");
                extractRawResToFile(R.raw.golast19, dir,"golast19.prototxt");
                extractRawResToFile(R.raw.golast, dir, "golast.trained");

                try {
                    //file.setExecutable(true); TODO test this instead of chmod
                    new ProcessBuilder("chmod", "744", file.getAbsolutePath()).start().waitFor();
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt(PREF_KEY_VERSION, EXE_VERSION);
                    editor.commit();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            catch (IOException e) { // TODO handle file extracting errors
                e.printStackTrace();
            }
            finally {
                Utils.closeObject(inputStream);
                Utils.closeObject(outputStream);
            }
        }
*/
        return file;
    }
/*
    private void extractRawResToFile(int resID, File dir, String fileName) throws IOException {
        File f = new File(dir, fileName);
        OutputStream o = new BufferedOutputStream(new FileOutputStream(f), 4096);
        InputStream i = new BufferedInputStream(_context.getResources().openRawResource(resID), 4096);
        Utils.copyStream(i, o, 4096);
        i.close();
        o.close();
    }
*/
    @Override
    public String getName() {
        return ENGINE_NAME;
    }

    @Override
    public String getVersion() {
        return ENGINE_VERSION;
    }
}
