package fuck.world.agent;

public class AgentArg {
    private String priKey;

    public String getPriKey() {
        return priKey;
    }


    public AgentArg(String args) {
        if (args == null || args.trim().isEmpty()) {
            throw new IllegalArgumentException("arg empty");
        }

        String[] split = args.split("::");
        for (int i = split.length - 1; i >= 0; i--) {
            String[] arg = split[i].split("=");
            switch (arg[0].trim()) {
                case "priKey":
                    this.priKey = arg[1].trim();
                    break;
            }
        }
    }

    private String[] pkgArr(String arg) {
        return arg.split(",");
    }
}
