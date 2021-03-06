/*
 * Copyright (c) 2010 Preemptive Labs / Andreas Bielk (http://www.preemptivelabs.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.preemptive.redis;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import se.preemptive.redis.commands.RedisBaseCommands;
import se.preemptive.redis.commands.RedisStringCommands;

import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;

@Test
public class RedisBaseClientTest
{
  private RedisBaseCommands client = new RedisBaseCommands(new RedisProtocolClient());

  @BeforeTest
  public void initClient()
  {
    assert client.select(10).asBoolean();
    assert client.flushdb().asBoolean();
  }

  public void testConnect()
  {
    client.getProtocolClient().connect();
  }

  public void testPing()
  {
    assert "PONG".equals(client.ping().asString()) : "GOT NO PONG";
  }

  public void testExistsAndDel()
    throws TimeoutException
  {
    assert client.flushdb().asBoolean();

    new RedisStringCommands(client.getProtocolClient()).
      set("key", "value").withTimeout(1, SECONDS);

    assert client.exists("key").withTimeout(1, SECONDS).asBoolean();

    client.del("key");
    assert !client.exists("key").withTimeout(1, SECONDS).asBoolean();
  }

  //auth
  //select
  //flushdb
  //flushall
  //dbsize
  //type
  //keys
  //randomkey
  //rename
  //renamenx
  //move
  //expire
  //ttl

}