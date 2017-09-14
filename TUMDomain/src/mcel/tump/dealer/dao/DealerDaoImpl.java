package mcel.tump.dealer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import mcel.tump.dealer.domain.AbstractDealer;
import mcel.tump.dealer.domain.AccpacAccountable;
import mcel.tump.dealer.domain.IPAddress;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import mcel.tump.dealer.domain.DealerShop;
import mcel.tump.dealer.domain.ShopDailySaleInfo;
import mcel.tump.util.persistence.BaseDao;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

public class DealerDaoImpl extends BaseDao implements IDealerDao {

    public DealerDaoImpl() {
        super();
    }

    public List<AbstractDealer> findAllDealers() {
        return getHibernateTemplate().find("from mcel.tump.dealer.domain.AbstractDealer as dealer where dealer.deleted = false");
    }

    public AbstractDealer findDealerById(Long id) {
        return (AbstractDealer) getHibernateTemplate().get(AbstractDealer.class, id);
    }

    public AbstractDealer findDealerByIdForUpdate(Long id) {
        return (AbstractDealer) getHibernateTemplate().get(AbstractDealer.class, id, LockMode.UPGRADE);
    }

    public AbstractDealer findDealerByUsername(String username) {
        List<AbstractDealer> dealers = getHibernateTemplate().find("select user.owningDealer from mcel.tump.security.domain.User as user where user.username = ?", new Object[] {username});
        Iterator<AbstractDealer> it = dealers.iterator();
        if(it.hasNext()) return it.next();
        return null;
    }

    public AbstractDealer findDealerByDealerCode(String dealerCode) {
        List<AbstractDealer> dealers = getHibernateTemplate().find("from mcel.tump.dealer.domain.AbstractDealer as dealer where dealer.dealerCode = ?", dealerCode);
        Iterator<AbstractDealer> it = dealers.iterator();
        if(it.hasNext()) return it.next();
        return null;
    }

    public AccpacAccountable findDealerByAccpacCode(String accpacCode) {
        List<AccpacAccountable> dealers = getHibernateTemplate().find("from mcel.tump.dealer.domain.AccpacAccountable as shop where shop.accpacCode = ?", accpacCode);
        Iterator<AccpacAccountable> it = dealers.iterator();
        if(it.hasNext()) return it.next();
        return null;
    }
    
    public Long getNextRequestTransactionID() {
        return (Long)getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Long returnVal = null;
                Connection con = session.connection();
                PreparedStatement pstmt = con.prepareStatement("select seq_shop_tid.nextval from dual");
                ResultSet rs = pstmt.executeQuery();
                while(rs.next()) {
                    returnVal = rs.getLong(1);
                }
                rs.close();
                pstmt.close();
                return returnVal;
            }
        });
    }

    public List<IPAddress> findAllIPAddresses() {
         return getHibernateTemplate().find("from mcel.tump.dealer.domain.IPAddress as address order by address.owningShop.id");
    }
    
    public ShopDailySaleInfo findShopDailySaleInfo(final Date date, final Long dealerId) {
         List<ShopDailySaleInfo> saleInfos  = getHibernateTemplate().executeFind(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String strDate = format.format(date);
                SQLQuery query = session.createSQLQuery("select * from TUM_DEALER_BALLIMIT_CHECK_TBL" +
                        " where TUM_DEALER_ID = :dealerId and TO_CHAR(TIME_INFO, 'YYYY-MM-DD') = :date");
                query.addEntity(ShopDailySaleInfo.class);
                query.setParameter("dealerId", dealerId);
                query.setParameter("date", strDate);
                List<ShopDailySaleInfo> result = query.list();
                return result;
            }
        });
        Iterator<ShopDailySaleInfo> it = saleInfos.iterator();
        if(it.hasNext()) return it.next();
        return null;
    }
}
