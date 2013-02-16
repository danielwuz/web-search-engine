package edu.nyu.cs.cs2580;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class SearchEngine {
  // @CS2580: please use a port number 258XX, where XX corresponds
  // to your group number.
  public static void main(String[] args) throws IOException {
    // Create the server.
    if (args.length < 2){
      System.out.println("arguments for this program are: [PORT] [PATH-TO-CORPUS]");
      return;
    }
    int port = Integer.parseInt(args[0]);
    String index_path = args[1];
    InetSocketAddress addr = new InetSocketAddress(port);
    HttpServer server = HttpServer.create(addr, -1);

    Ranker ranker = new Ranker(index_path);
    
    // Attach specific paths to their handlers.
    server.createContext("/", new QueryHandler(ranker));
    server.setExecutor(Executors.newCachedThreadPool());
    server.start();
    System.out.println("Listening on port: " + Integer.toString(port));
  }
}
