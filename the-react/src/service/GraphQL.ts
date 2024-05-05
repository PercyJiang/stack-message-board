import { gql } from "graphql-request";
import { Message } from "../types";

export const graphGetAll: string = gql`
  query graphGetAll {
    graphGetAll {
      id
      source
      content
      created
    }
  }
`;
export interface graphGetAllResponse {
  graphGetAll: Message[];
}

export const graphPost: string = gql`
  mutation graphPost($source: String!, $content: String!) {
    graphPost(source: $source, content: $content)
  }
`;
export interface GraphPostResponse {
  graphPost: number;
}
