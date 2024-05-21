package fuck.world.agent;

public class AgentArg {
    private String priKey;
    private String[] pkgs;

    public String getPriKey() {
        return priKey;
    }

    public String[] getPkgs() {
        return pkgs;
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
                case "pkgs":
                    this.pkgs = pkgCollec(arg[1].trim());
                    break;
            }
        }
    }

    private String[] pkgCollec(String arg) {
        return arg.split(",");
    }
}
