
package com.asset.ccat.simulators_service.operations;


import com.asset.ccat.simulators_service.database.dao.SubscriberDao;
import com.asset.ccat.simulators_service.exceptions.SimulatorsException;
import com.asset.ccat.simulators_service.models.AccountDetailsModel;
import com.asset.ccat.simulators_service.models.UCIPModel;
import com.asset.ccat.simulators_service.utils.FileUtils;

import java.io.IOException;
import java.util.List;


public class UpdateBalanceAndDateClassOperation implements Operation {


  private final SubscriberDao dao;

  public UpdateBalanceAndDateClassOperation(SubscriberDao dao) {
    this.dao = dao;
  }

  @Override
  public String operate(UCIPModel model) throws SimulatorsException {

    List<AccountDetailsModel> accountDetails = dao.getAccountDetails(model.getMsisdn());

    Integer totalBalance = 0;
    if(!accountDetails.isEmpty()){
      String originalBalance = accountDetails.get(0).getAdjustmentAmount();
      totalBalance += Integer.parseInt(originalBalance);
      totalBalance += Integer.parseInt(model.getAdjustmentAmount());
    }



    int result = dao.updateBalance(model.getMsisdn(),totalBalance.toString());
    String filePath = "";
    if (result>0) {
      filePath = model.getPath() + model.getMethod();
    } else {
      filePath = model.getPath() + model.getMethod() + "NotFound";
    }
    String returnBody = "";
    try {
      returnBody = FileUtils.readFileAsString(filePath, model);
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    return returnBody;
  }

}
