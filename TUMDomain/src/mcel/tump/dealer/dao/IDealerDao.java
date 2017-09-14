package mcel.tump.dealer.dao;

import java.util.Date;
import java.util.List;
import mcel.tump.dealer.domain.AbstractDealer;
import mcel.tump.dealer.domain.AccpacAccountable;
import mcel.tump.dealer.domain.DealerShop;
import mcel.tump.dealer.domain.IPAddress;
import mcel.tump.dealer.domain.ShopDailySaleInfo;
import mcel.tump.util.persistence.IBaseDao;

public interface IDealerDao extends IBaseDao {

    public List<AbstractDealer> findAllDealers();

    public AbstractDealer findDealerById(Long id);
    
    public AbstractDealer findDealerByDealerCode(String dealerCode);
    
    public AccpacAccountable findDealerByAccpacCode(String accpacCode);
    
    public AbstractDealer findDealerByUsername(String username);

    public AbstractDealer findDealerByIdForUpdate(Long id);
    
    public Long getNextRequestTransactionID();
    
    public List<IPAddress> findAllIPAddresses();
    
    public ShopDailySaleInfo findShopDailySaleInfo(Date date, Long dealerId);
}
