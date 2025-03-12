import { createClient, RedisClientType } from "redis";

export async function createRedisClient(): Promise<RedisClientType> {
  const client: RedisClientType = createClient({
    username: "1",
    password: "1",
    socket: {
      host: "1",
      port: 1,
    },
  });

  client.on("error", (err: any) => console.log("Redis Client Error", err));
  await client.connect();

  return client;
}

export async function printRedisKeys(client: RedisClientType): Promise<void> {
  const keys: string[] = await client.keys("*");
  for (const key of keys) {
    const value = await client.get(key);
    console.log("percy: key: ", key, " value: ", value);
  }
}
