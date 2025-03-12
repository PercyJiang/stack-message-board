import express, { Request, Response } from "express";
import amqp from "amqplib";
import { RabbitMessageBody } from "./type";
import { bytesToJson } from "./util";

const app = express();
const port = 3000;

async function connect() {
  const connection = await amqp.connect("amqp://localhost");
  const channel = await connection.createChannel();
  const queue = "hello";
  await channel.assertQueue(queue, { durable: false });

  channel.consume(queue, (message) => {
    if (!message) throw new Error("Message failed");
    channel.ack(message);

    const body: RabbitMessageBody = bytesToJson(message.content);
    console.log("percy: body: ", body);
  });

  app.get("/", (req: Request, res: Response) => {
    res.send("Hello, Express with TypeScript!");
  });

  app.listen(port, () => {
    console.log(`Server is running on port ${port}`);
  });
}

connect().catch(console.error);
