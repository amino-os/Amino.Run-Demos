package kobi.chess;

/**
 * Created by mbssaiakhil on 27/1/18.
 */

import engine.SimpleEngine;
import sapphire.app.SapphireObject;

import static sapphire.runtime.Sapphire.new_;

public class NewChessManager implements SapphireObject {
    private SamplePrint samplePrint;

    private SimpleEngine simpleEngine;

    public NewChessManager() {

        samplePrint = (SamplePrint) new_(SamplePrint.class);
        simpleEngine = (SimpleEngine) new_(SimpleEngine.class);
    }

    public SamplePrint getSamplePrintManager() { return samplePrint; }

    public SimpleEngine getSimpleEngine() { return simpleEngine; }
}
