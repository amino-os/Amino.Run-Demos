package chessmanager;

/**
 * Created by mbssaiakhil on 27/1/18.
 */

import amino.run.app.DMSpec;
import amino.run.app.Language;
import amino.run.app.MicroService;
import amino.run.app.MicroServiceSpec;
import amino.run.common.MicroServiceCreationException;
import amino.run.policy.mobility.explicitmigration.ExplicitMigrationPolicy;
import engine.SimpleEngine;

import static amino.run.runtime.MicroService.new_;


public class ChessManager implements MicroService {

    private SimpleEngine simpleEngine;

    public ChessManager() throws MicroServiceCreationException {
        MicroServiceSpec simpleEngineSpec;
        simpleEngineSpec = MicroServiceSpec.newBuilder()
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
