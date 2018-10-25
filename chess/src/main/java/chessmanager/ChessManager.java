package chessmanager;

/**
 * Created by mbssaiakhil on 27/1/18.
 */

import engine.SimpleEngine;
import sapphire.app.DMSpec;
import sapphire.app.Language;
import sapphire.app.SapphireObject;

import static sapphire.runtime.Sapphire.new_;

import sapphire.app.SapphireObjectSpec;
import sapphire.policy.mobility.explicitmigration.ExplicitMigrationPolicy;

public class ChessManager implements SapphireObject {

    private SimpleEngine simpleEngine;

    public ChessManager() {
        SapphireObjectSpec simpleEngineSpec;
        simpleEngineSpec = SapphireObjectSpec.newBuilder()
                .setLang(Language.java)
                .setJavaClassName(SimpleEngine.class.getName())
                .addDMSpec(
                        DMSpec.newBuilder()
                                .setName(ExplicitMigrationPolicy.class.getName())
                                .create())
                .create();
        simpleEngine = (SimpleEngine) new_(simpleEngineSpec);
    }

    public SimpleEngine getSimpleEngine() { return simpleEngine; }
}
