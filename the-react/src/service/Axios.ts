import axios, { AxiosInstance, AxiosResponse } from "axios";
import axiosRetry from "axios-retry";
import { Message } from "../types";
import { API_BASE_PATH } from "../constants";

export async function callAxios<RequestBodyType, ResponseBodyType>(
  baseURL: string,
  endpoint: string,
  requestType: string,
  requestData?: RequestBodyType
): Promise<AxiosResponse<ResponseBodyType>> {
  const timeoutThreshold: number = 60000;

  const axiosInstance: AxiosInstance = axios.create({
    baseURL: baseURL + API_BASE_PATH,
    timeout: timeoutThreshold,
    timeoutErrorMessage: "REQUEST_TIMEOUT",
    headers: {
      "Content-Type": "application/json",
    },
  });

  axiosRetry(axiosInstance, {
    retries: 0,
    shouldResetTimeout: true,
    retryDelay: (retryCount) => retryCount * 999,
    // wait one second longer per retry
  });

  switch (requestType) {
    case "GET":
      return await axiosInstance.get<ResponseBodyType>(endpoint);
    case "POST":
      return await axiosInstance.post<ResponseBodyType>(endpoint, requestData);
    case "PUT":
      return await axiosInstance.put<ResponseBodyType>(endpoint, requestData);
    case "DELETE":
      return await axiosInstance.delete<ResponseBodyType>(endpoint);
    default:
      throw new Error("Invalid HTTP verb/requestType");
  }
}

export async function getAll(baseURL: string): Promise<Message[]> {
  const response: AxiosResponse<Message[]> = await callAxios(
    baseURL,
    "/getAll",
    "GET"
  );
  return response.data;
}

export async function post(
  baseURL: string,
  message: Message
): Promise<number> {
  const response: AxiosResponse<number> = await callAxios(
    baseURL,
    "/post",
    "POST",
    message
  );
  return response.data;
}
