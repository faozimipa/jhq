package id.zitech.base;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import redis.embedded.RedisServer;

public class RedisCacheTestResource implements QuarkusTestResourceLifecycleManager {
  RedisServer server;

  @Override
  public Map<String, String> start() {
    try {
      server = new RedisServer();
      server.start();

      return Collections.emptyMap();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void stop() {
    server.stop();
  }
}
