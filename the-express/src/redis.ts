import { createClient, RedisClientType } from "redis";

export async function createRedisClient(): Promise<RedisClientType> {
  const client: RedisClientType = createClient({
    username: "default",
    password: "UGZpEMCrRSUOk7KrfbVhq70gqHcB80n8",
    socket: {
      host: "redis-17506.c8.us-east-1-2.ec2.redns.redis-cloud.com",
      port: 17506,
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
