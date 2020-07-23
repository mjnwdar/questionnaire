package com.zzq.graduationproject;

import com.zzq.graduationproject.service.DataFlowService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zzq
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DataFlowTest {

  @Autowired
  private DataFlowService dataFlowService;

  @Test
  public void updateStatus() {
    Integer dataFlowId = 1;
    String status = "Success";

    dataFlowService.updateDataFlowStatus(dataFlowId, status);
  }


}
