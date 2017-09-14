package mcel.tump.dealer.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import mcel.tump.dealer.dao.IDealerDao;
import mcel.tump.dealer.domain.AbstractDealer;
import mcel.tump.dealer.domain.AccpacAccountable;
import mcel.tump.dealer.domain.DealerShop;
import mcel.tump.dealer.domain.IPAddress;
import mcel.tump.dealer.domain.Notifiable;
import mcel.tump.dealer.domain.ShopDailySaleInfo;
import mcel.tump.notification.service.INotificationService;
import mcel.tump.reference.dao.IParametersDao;
import mcel.tump.reference.service.IParametersService;
import mcel.tump.security.domain.User;
import mcel.tump.security.domain.UserStatus;
import mcel.tump.security.service.ISecurityService;
import mcel.tump.util.value.TresholdValues;

public class DealerService implements IDealerService {

    private IDealerDao dealerDao;
    private IParametersDao paramsDao;
    private ISecurityService securityService;
    private IParametersService parametersService;
    private INotificationService notificationService;
    
    public IParametersDao getParamsDao() {
        return paramsDao;
    }

    public void setParamsDao(IParametersDao paramsDao) {
        this.paramsDao = paramsDao;
    }

    public IDealerDao getDealerDao() {
        return dealerDao;
    }

    public void setDealerDao(IDealerDao dealerDao) {
        this.dealerDao = dealerDao;
    }

    public ISecurityService getSecurityService() {
        return securityService;
    }

    public void setSecurityService(ISecurityService securityService) {
        this.securityService = securityService;
    }

    public IParametersService getParametersService() {
        return parametersService;
    }

    public void setParametersService(IParametersService parametersService) {
        this.parametersService = parametersService;
    }

    public INotificationService getNotificationService() {
        return notificationService;
    }

    public void setNotificationService(INotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    @Override
    public void deleteDealer(AbstractDealer dealer) {
        getDealerDao().update(dealer);
        dealer.setDeleted(true);
        dealer.deactivateAllUsers();
    }

    @Override
    public List<AbstractDealer> getAllDealers() {
        return getDealerDao().findAllDealers();
    }

    @Override
    public AbstractDealer getDealer(Long id) {
        return getDealerDao().findDealerById(id);
    }

    @Override
    public void saveDealer(AbstractDealer dealer, Long newParentId) {
        dealer = (AbstractDealer)getDealerDao().merge(dealer);
        if(newParentId != 0) {
            AbstractDealer parent = getDealer(newParentId);
            dealer.setNewParent(parent);
        } else {
            dealer.setNewParent(null);
        }
        TresholdValues cumulativeTresholds = getCumulativeTresholdValuesExcept(dealer);
        validateTresholds(cumulativeTresholds, dealer);
    }
    
    public void validateTresholds(TresholdValues cumulativeTresholds, AbstractDealer dealer) {
        TresholdValues systemTresholds = getParametersService().getParameters().getTresholdValues();
        dealer.getAccount().validateTresholds(cumulativeTresholds, systemTresholds);
    }
    
    public void createDealer(AbstractDealer dealer, Long parentId) {
        TresholdValues cumulativeTresholds = getCumulativeTresholdValuesExcept(null);
        validateTresholds(cumulativeTresholds, dealer);
        if(parentId != 0) {
            AbstractDealer parentDealer = getDealer(parentId);
            parentDealer.addDealer(dealer);
            dealer.getAccount().setDealer(dealer);
            getDealerDao().save(parentDealer);
        } else {
            dealer.getAccount().setDealer(dealer);
            getDealerDao().save(dealer);
        }
        if(dealer instanceof Notifiable)
            getNotificationService().sendDealerCreatedInformationMail((Notifiable)dealer, getSecurityService().getApplicationAdminUser());
        //getNotificationService().sendDealerCreatedInformationSMS(shop);
    }

    public AbstractDealer getDealerByUserName(String username) {
        return getDealerDao().findDealerByUsername(username);
    }

    public TresholdValues getCumulativeTresholdValuesExcept(AbstractDealer dealer) {
        List<AbstractDealer> dealers = getAllDealers();
        Iterator<AbstractDealer> it = dealers.iterator();
        TresholdValues returnVal = new TresholdValues();
        while(it.hasNext()) {
            AbstractDealer current = it.next();
            if((dealer != null) && (!dealer.equals(current)))
                returnVal = returnVal.add(current.getAccount().getTresholdValues());
        }
        return returnVal;
    }

    public void activateDealer(AbstractDealer dealer) {
        getDealerDao().update(dealer);
        boolean inBlacklist = getParametersService().isDealerInBlacklist(dealer);
        if(inBlacklist) throw new RuntimeException("The Dealer is in blacklist and cannot be activated.");
        dealer.setDealerStatus(UserStatus.Active);
    }

    public void suspendDealer(AbstractDealer dealer) {
        getDealerDao().update(dealer);
        dealer.setDealerStatus(UserStatus.Suspended);
    }

    public AbstractDealer getDealerByDealerCode(String dealerCode) {
        return getDealerDao().findDealerByDealerCode(dealerCode);
    }

    public AccpacAccountable getDealerByAccpacCode(String accpacCode) {
        return getDealerDao().findDealerByAccpacCode(accpacCode);
    }
    
    public Long fetchNextRequestTransactionID() {
        return getDealerDao().getNextRequestTransactionID();
    }
    
    public void addIPAddress(IPAddress address, DealerShop shop) {
        getDealerDao().update(shop);
        shop.addIPAddress(address);
    }
    
    public void deleteIPAddress(IPAddress address) {
        getDealerDao().delete(address);
    }

    public List<IPAddress> getAllIPAddresses() {
        return getDealerDao().findAllIPAddresses();
    }

    public void saveIPAddress(IPAddress address, Long shopId) {
        if(address.getOwningShop() != null) {
            address = (IPAddress)getDealerDao().merge(address);
            address.getOwningShop().removeIPAddress(address);
        }
        AbstractDealer dealer = getDealer(shopId);
        dealer.addIPAddress(address);
        getDealerDao().saveOrUpdate(dealer);
    }

    public boolean checkIPValidity(String ipAddress, AbstractDealer dealer) {
        getDealerDao().attach(dealer);
        return dealer.hasIPAddress(ipAddress);
    }
    
    public void sendDailySalesInfoEmails() {
        List<User> users = getSecurityService().getAllShopSupervisorUsers();
        for(User user : users) {
            AbstractDealer dealer = user.getOwningDealer();
            ShopDailySaleInfo info = getDealerDao().findShopDailySaleInfo(new Date(), dealer.getId());
            if(info == null) return;
            getNotificationService().sendDealerDailySaleInfoMail(info, user);
        }
    }
    
    @Override
	public void attach(Object o) {
		getDealerDao().attach(o);		
	}
    
}
