import React, { useEffect, useState } from "react";
import "./App.css";
import {
  Backdrop,
  Button,
  CircularProgress,
  IconButton,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  TextField,
} from "@mui/material";
import SendIcon from "@mui/icons-material/Send";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import CleaningServicesIcon from "@mui/icons-material/CleaningServices";
import { getAll, post } from "./service/Axios";
import { Message } from "./types";
import { ApiMode, GRAPHQL_PATH } from "./constants";
import { Variables, request } from "graphql-request";
import { graphGetAll, graphGetAllResponse, graphPost } from "./service/GraphQL";

function App() {
  const baseURL: string = "http://localhost:8080";
  const apiMode: ApiMode = ApiMode.GRAPHQL;

  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [messageList, setMessageList] = useState<Message[] | undefined>(
    undefined
  );
  const [source, setSource] = useState<string>("");
  const [content, setContent] = useState<string>("");

  async function fetchMessageList(): Promise<void> {
    try {
      if (apiMode === ApiMode.REST) {
        const response: Message[] = await getAll(baseURL);
        setMessageList(response);
      } else {
        const response: graphGetAllResponse = await request(
          baseURL + GRAPHQL_PATH,
          graphGetAll
        );
        setMessageList(response.graphGetAll);
      }
      setIsLoading(false);
    } catch (err: any) {
      console.error("percy: error: ", err);
      setIsLoading(false);
    }
  }

  async function sendMessage(): Promise<void> {
    setIsLoading(true);
    try {
      if (apiMode === ApiMode.REST) {
        await post(baseURL, {
          source: source,
          content: content,
        } as Message);
      } else {
        await request(baseURL + GRAPHQL_PATH, graphPost, {
          source: source,
          content: content,
        } as Variables);
      }
      fetchMessageList();
      setContent("");
    } catch (err: any) {
      console.error("percy: error: ", err);
      setIsLoading(false);
    }
  }

  useEffect(() => {
    fetchMessageList();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <div>
      <Backdrop
        sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}
        open={isLoading}
      >
        <CircularProgress />
      </Backdrop>
      <Table stickyHeader>
        <TableHead>
          <TableRow>
            <TableCell>SOURCE</TableCell>
            <TableCell>CONTENT</TableCell>
            <TableCell align="right">EDIT</TableCell>
            <TableCell align="right">DELETE</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {messageList?.map((message: Message) => (
            <TableRow key={message.id}>
              <TableCell>{message.source}</TableCell>
              <TableCell>{message.content}</TableCell>
              <TableCell align="right">
                <IconButton>
                  <EditIcon />
                </IconButton>
              </TableCell>
              <TableCell align="right">
                <IconButton>
                  <DeleteIcon />
                </IconButton>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
      <TextField
        id="outlined-basic"
        label="What is your name"
        variant="outlined"
        value={source}
        onChange={(e) => setSource(e.target.value)}
      />
      <TextField
        id="outlined-basic"
        label="Type your message here"
        variant="outlined"
        value={content}
        onChange={(e) => setContent(e.target.value)}
      />
      <Button
        variant="outlined"
        startIcon={<CleaningServicesIcon />}
        onClick={() => setContent("")}
      >
        CLEAR
      </Button>
      <Button
        variant="contained"
        endIcon={<SendIcon />}
        onClick={() => sendMessage()}
      >
        SEND
      </Button>
    </div>
  );
}

export default App;
