package sapphire.policy.transaction;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

public class DCAPTransactionWrapperTest {
    @Test
    public void test_wrap_rpc_in_tx(){
        String method = "foo";
        ArrayList<Object> params = new ArrayList<>();
        UUID transactionId = UUID.randomUUID();
        DCAPTransactionWrapper wrapper = new DCAPTransactionWrapper(transactionId, method, params);

        ArrayList<Object> wrappedMessage = wrapper.getRpcParams();
        assertEquals(wrappedMessage.get(0), transactionId);
        ArrayList<Object> innerRPCMessage = (ArrayList<Object>)wrappedMessage.get(1);
        assertEquals(innerRPCMessage.get(0), method);
        assertEquals(innerRPCMessage.get(1), params);
    }

    @Test
    public void test_extract_rpc_wrapper() {
        UUID id = UUID.randomUUID();
        ArrayList<Object> params = new ArrayList<>();

        ArrayList<Object> wrapped = new ArrayList<>(Arrays.asList(
                id,
                new ArrayList<Object>(Arrays.asList("bar", params))
        ));

        DCAPTransactionWrapper parser = new DCAPTransactionWrapper("tx_rpc", wrapped);

        assertEquals(parser.getTransaction(), id);
        assertEquals(parser.getInnerRPCMethod(), "bar");
        assertEquals(parser.getInnerRPCParams(), params);
    }
}
