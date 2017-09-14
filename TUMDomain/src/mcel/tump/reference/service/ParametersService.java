/*
 * ParametersService.java
 * 
 * Created on Aug 6, 2007, 6:26:11 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.reference.service;

import java.util.Collections;
import java.util.List;
import mcel.tump.account.dao.IAccountDao;
import mcel.tump.account.domain.Account;
import mcel.tump.dealer.domain.AbstractDealer;
import mcel.tump.dealer.domain.DealerShop;
import mcel.tump.dealer.service.IDealerService;
import mcel.tump.reference.dao.IParametersDao;
import mcel.tump.reference.domain.BlacklistItem;
import mcel.tump.reference.domain.City;
import mcel.tump.reference.domain.Parameters;
import mcel.tump.reference.domain.SubscriberBenefit;
import mcel.tump.util.value.TresholdValues;

/**
 *
 * @author db2admin
 */
public class ParametersService implements IParametersService {

    private IParametersDao paramsDao;
    private IAccountDao accountDao;
    private IDealerService dealerService;

    public IParametersDao getParamsDao() {
        return paramsDao;
    }

    public void setParamsDao(IParametersDao paramsDao) {
        this.paramsDao = paramsDao;
    }

    public IAccountDao getAccountDao() {
        return accountDao;
    }

    public void setAccountDao(IAccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public IDealerService getDealerService() {
        return dealerService;
    }

    public void setDealerService(IDealerService dealerService) {
        this.dealerService = dealerService;
    }
    
    public ParametersService() {
        super();
    }

    public List<City> getAllCities() {
        return getParamsDao().findAllCities();
    }

    public City getCity(Long id) {
        return getParamsDao().findCityById(id);
    }

    public Parameters getParameters() {
        return getParamsDao().findParameters();
    }

    public void saveParameters(Parameters params) {
        TresholdValues cumulative = getDealerService().getCumulativeTresholdValuesExcept(null);
        TresholdValues system = params.getTresholdValues();
        TresholdValues.validateSystemVsCumulative(system, cumulative);
        List<AbstractDealer> dealers = getDealerService().getAllDealers();
        for(AbstractDealer dealer : dealers) {
            TresholdValues.validateSystemVsAccount(system, dealer.getAccount().getTresholdValues());
        }
        getParamsDao().saveOrUpdate(params);
    }

    public List<SubscriberBenefit> getAllSubscriberBenefits() {
        List<SubscriberBenefit> subscribers = getParamsDao().findAllSubscriberBenefits();
        if(subscribers != null) Collections.sort(subscribers);
        return subscribers;
    }

    public SubscriberBenefit getSubscriberBenefit(Long id) {
        return getParamsDao().findSubscriberBenefitById(id);
    }

    public void saveSubscriberBenefit(SubscriberBenefit benefit) {
        benefit.validate();
        List<SubscriberBenefit> subs = getAllSubscriberBenefits();
        SubscriberBenefit last = new SubscriberBenefit();
        if(subs.size() > 0) last = subs.get(subs.size() - 1);
        if(benefit.getLowerLimit().getAmount().doubleValue() <= last.getUpperLimit().getAmount().doubleValue()) throw new RuntimeException("Lower and upper limit values of the entry should not be within bounds of the revious entry.");
        getParamsDao().saveOrUpdate(benefit);
    }
    
    public void updateSubscriberBenefit(SubscriberBenefit benefit) {
        getParamsDao().saveOrUpdate(benefit);
    }

    public void deleteSubscriberBenefit(SubscriberBenefit benefit) {
        getParamsDao().delete(benefit);
    }

    public void addToBlacklist(Long accountId, String subDealerId) {
        Account account = getAccountDao().findAccountById(accountId);
        BlacklistItem item = new BlacklistItem(account, subDealerId);
        getParamsDao().saveOrUpdate(item);
    }

    public void removeFromBlacklist(BlacklistItem item) {
        getParamsDao().delete(item);
        if(item.getSubDealerId() == null) {
            AbstractDealer dealer = getDealerService().getDealerByDealerCode(item.getDealerCode());
            getDealerService().activateDealer(dealer);
        } 
    }

    public List<BlacklistItem> getBlacklist() {
        return getParamsDao().findAllBlacklisted();
    }

    public void addToBlacklist(BlacklistItem item) {
        getParamsDao().save(item);
        if(item.getSubDealerId() == null) {
            AbstractDealer dealer = getDealerService().getDealerByDealerCode(item.getDealerCode());
            getDealerService().suspendDealer(dealer);
        }
    }
    
    public boolean isDealerInBlacklist(AbstractDealer dealer) {
        List<BlacklistItem> list = getBlacklist();
        for(BlacklistItem item : list) {
            if(item.getSubDealerId() == null) {
                if(item.getDealerCode().equals(dealer.getDealerCode())) return true;
            }
        }
        return false;
    }
}
