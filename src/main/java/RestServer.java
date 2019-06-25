public class RestServer {

    private final String protocol;
    private final String host;

    public RestServer(String protocol, String host) {
        this.protocol = protocol;
        this.host = host;
    }

    public String getHost() {
        return String.format(protocol + "://" + host);
    }

}