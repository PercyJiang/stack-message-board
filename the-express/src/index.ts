import express, { Request, Response } from "express";
import amqp, { Channel } from "amqplib";
import { RabbitMessageBody } from "./type";
import { bytesToJson } from "./util";
import { RedisClientType } from "redis";
import { createRedisClient, printRedisKeys } from "./redis";
import { createRabbitChannel } from "./rabbit";

const app = express();
const port = 3000;

async function connect() {
  const redisClient: RedisClientType = await createRedisClient();

  const queue: string = "hello";
  const rabbitChannel: Channel = await createRabbitChannel(queue);
  rabbitChannel.consume(queue, (message) => {
    if (!message) throw new Error("Message failed");
    rabbitChannel.ack(message);

    const body: RabbitMessageBody = bytesToJson(message.content);
    console.log("percy: body: ", body);

    if (body.contentType === "application/json") {
      redisClient.set(body.username, JSON.stringify(body.content));
    }
    printRedisKeys(redisClient);
  });

  app.get("/", (req: Request, res: Response) => {
    res.send("Hello, Express with TypeScript!");
  });

  app.listen(port, () => {
    console.log(`Server is running on port ${port}`);
  });
}

connect().catch(console.error);
