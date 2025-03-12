import express, { Request, Response } from "express";
import amqp, { Channel, ChannelModel } from "amqplib";
import { RabbitMessageBody } from "./type";
import { bytesToJson } from "./util";
import { createClient, RedisClientType } from "redis";
import * as Minio from "minio";

const app = express();
const port = 3000;

async function connect() {
  // redis
  const redisClient: RedisClientType = createClient({
    username: "default",
    password: "UGZpEMCrRSUOk7KrfbVhq70gqHcB80n8",
    socket: {
      host: "redis-17506.c8.us-east-1-2.ec2.redns.redis-cloud.com",
      port: 17506,
    },
  });
  redisClient.on("error", (err: any) => console.log("Redis Client Error", err));
  await redisClient.connect();

  // minio
  const minioClient: Minio.Client = new Minio.Client({
    endPoint: "localhost",
    port: 9000,
    useSSL: false,
    accessKey: "minioadmin",
    secretKey: "minioadmin",
  });
  const bucket: string = "percy-minio-bucket";
  if (await minioClient.bucketExists(bucket)) {
    console.log(`Bucket ${bucket} already exists.`);
  } else {
    await minioClient.makeBucket(bucket);
    console.log(`Bucket ${bucket} created.`);
  }

  // rabbitmq
  const host: string = "amqp://localhost";
  const queue: string = "percy_rabbit_queue";
  const connection: ChannelModel = await amqp.connect(host);
  const rabbitChannel: Channel = await connection.createChannel();
  await rabbitChannel.assertQueue(queue, { durable: false });

  // main
  rabbitChannel.consume(queue, (message) => {
    if (!message) throw new Error("Message failed");
    rabbitChannel.ack(message);

    const body: RabbitMessageBody = bytesToJson(message.content);
    console.log("percy: body: ", body);

    if (body.contentType === "application/json") {
      redisClient.set(body.username, JSON.stringify(body.content));
    }

    if (body.contentType === "image/jpeg") {
      minioClient.putObject(
        bucket,
        body.username + ".jpg",
        Buffer.from(body.content, "base64")
      );
    }
  });

  app.get("/", (req: Request, res: Response) => {
    res.send("Hello, Express with TypeScript!");
  });

  app.listen(port, () => {
    console.log(`Server is running on port ${port}`);
  });
}

connect().catch(console.error);
