import React, { useState } from "react";
import "./App.css";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import InboxIcon from "@mui/icons-material/Inbox";
import Divider from "@mui/material/Divider";

function App() {
  const [messageList, setMessageList] = useState([
    { source: "bob", content: "hello world" },
    { source: "ann", content: "bye world" },
  ]);
  const [message, setMessage] = useState(undefined);

  return (
    <div>
      <List>
        {messageList.map((message) => (
          <>
            <ListItem>
              <ListItemIcon>
                <InboxIcon />
              </ListItemIcon>
              <ListItemText primary={"source: " + message.source} />
              <ListItemText primary={"content: " + message.content} />
            </ListItem>
            <Divider />
          </>
        ))}
      </List>
    </div>
  );
}

export default App;
