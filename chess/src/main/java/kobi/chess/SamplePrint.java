package kobi.chess;

import sapphire.app.SapphireObject;
import sapphire.policy.ShiftPolicy;

/**
 * Created by mbssaiakhil on 24/1/18.
 */

public class SamplePrint implements SapphireObject<ShiftPolicy> {
    private int cnt = 1;

    public void printSampleLine() {
        System.out.println("This is sample line print from Sapphire Object with " + cnt++);
    }

    public void printSampleLine2() {
        System.out.println("This is not a sample line print from Sapphire Object with " + cnt++);
    }

    public void printSampleLine3() {
        System.out.println("This is a 3rd not a sample line print from Sapphire Object with " + cnt++);
    }
}
