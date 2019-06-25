public class Base {
    String protocol = "https";
    String hostName = "www.googleapis.com";
    String URIPathForBooksInformation = "/books/v1/volumes";

    RestServer server = new RestServer(protocol, hostName);
}
