package mcel.tump.dealer.service;

import java.util.List;
import mcel.tump.dealer.domain.AbstractDealer;
import mcel.tump.dealer.domain.AccpacAccountable;
import mcel.tump.dealer.domain.IPAddress;
import mcel.tump.util.service.IBaseService;
import mcel.tump.util.value.TresholdValues;

public interface IDealerService extends IBaseService {

    public List<AbstractDealer> getAllDealers();
    public AbstractDealer getDealer(Long id);
    public AbstractDealer getDealerByDealerCode(String dealerCode);
    public AccpacAccountable getDealerByAccpacCode(String accpacCode);
    public AbstractDealer getDealerByUserName(String username);
    public void saveDealer(AbstractDealer dealer, Long newParentId);
    public void deleteDealer(AbstractDealer dealer);
    public void createDealer(AbstractDealer dealer, Long parentId);
    public TresholdValues getCumulativeTresholdValuesExcept(AbstractDealer dealer);
    public void activateDealer(AbstractDealer dealer);
    public void suspendDealer(AbstractDealer dealer);
    public Long fetchNextRequestTransactionID();
    public List<IPAddress> getAllIPAddresses();
    public void saveIPAddress(IPAddress address, Long shopId);
    public void deleteIPAddress(IPAddress address);
    public boolean checkIPValidity(String ipAddress, AbstractDealer dealer);
    public void sendDailySalesInfoEmails();
    public void validateTresholds(TresholdValues cumulativeTresholds, AbstractDealer dealer);
}
