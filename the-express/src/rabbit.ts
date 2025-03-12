import amqp, { Channel, ChannelModel } from "amqplib";

export async function createRabbitChannel(queue: string): Promise<Channel> {
  const connection: ChannelModel = await amqp.connect("amqp://localhost");
  const channel: Channel = await connection.createChannel();
  await channel.assertQueue(queue, { durable: false });
  return channel;
}
