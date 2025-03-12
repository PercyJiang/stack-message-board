package com.msg.api;

import com.msg.dao.RabbitDao;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("unused")
@RestController
@RequestMapping(path = "tests")
@CrossOrigin
@Tag(name = "Tests", description = "Test API")
public class TestController {

  private final RabbitDao rabbitDao;

  @Autowired
  public TestController(RabbitDao rabbitDao) {
    this.rabbitDao = rabbitDao;
  }

  @RequestMapping("/send")
  public String testSend() {
    rabbitDao.testSend();
    return "Pong!";
  }

  @RequestMapping("/receive")
  public String testReceive() {
    rabbitDao.testReceive();
    return "Pong!";
  }
}
