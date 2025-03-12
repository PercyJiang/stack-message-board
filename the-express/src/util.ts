export function bytesToJson(bytes: Buffer<ArrayBufferLike>) {
  try {
    const decodedString = new TextDecoder().decode(bytes);
    const jsonObject = JSON.parse(decodedString);
    return jsonObject;
  } catch (error) {
    console.error("Error converting bytes to JSON:", error);
    return null;
  }
}
