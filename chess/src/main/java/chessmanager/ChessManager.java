package chessmanager;

/**
 * Created by mbssaiakhil on 27/1/18.
 */

import amino.run.app.DMSpec;
import amino.run.app.Language;
import amino.run.app.MicroServiceSpec;
import amino.run.app.SapphireObject;
import amino.run.common.MicroServiceCreationException;
import amino.run.policy.mobility.explicitmigration.ExplicitMigrationPolicy;
import engine.SimpleEngine;

import static amino.run.runtime.Sapphire.new_;


public class ChessManager implements SapphireObject {

    private SimpleEngine simpleEngine;

    public ChessManager() {
        MicroServiceSpec simpleEngineSpec;
        simpleEngineSpec = MicroServiceSpec.newBuilder()
                .setLang(Language.java)
                .setJavaClassName(SimpleEngine.class.getName())
                .addDMSpec(
                        DMSpec.newBuilder()
                                .setName(ExplicitMigrationPolicy.class.getName())
                                .create())
                .create();
        try {
            simpleEngine = (SimpleEngine) new_(simpleEngineSpec);
        } catch (MicroServiceCreationException e) {
            e.printStackTrace();
        }
    }

    public SimpleEngine getSimpleEngine() { return simpleEngine; }
}
