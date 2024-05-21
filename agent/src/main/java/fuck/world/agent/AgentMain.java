package fuck.world.agent;

import fuck.world.rsa.Rsa;
import fuck.world.rsa.RsaDefaultImpl;

import java.lang.instrument.Instrumentation;

public class AgentMain {
    public static void premain(String args, Instrumentation instrumentation) {
        try {
            AgentArg agentArg = new AgentArg(args);
            Rsa rsa = new RsaDefaultImpl(agentArg.getPriKey());
          /*转换发生在 premain 函数执行之后，main 函数执行之前，这时每装载一个类，transform 方法就会执行一次，看看是否需要转换，
        所以，在 transform（Transformer 类中）方法中，程序用 className.equals("TransClass") 来判断当前的类是否需要转换。*/
            // 方式一：
            instrumentation.addTransformer(new JpClassFileTransformer(rsa, agentArg));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}