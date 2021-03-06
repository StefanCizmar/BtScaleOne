package com.kiwi.btscaleone.dao;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

import com.kiwi.btscaleone.Times;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig timesDaoConfig;

    private final TimesDao timesDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        timesDaoConfig = daoConfigMap.get(TimesDao.class).clone();
        timesDaoConfig.initIdentityScope(type);

        timesDao = new TimesDao(timesDaoConfig, this);

        registerDao(Times.class, timesDao);
    }
    
    public void clear() {
        timesDaoConfig.clearIdentityScope();
    }

    public TimesDao getTimesDao() {
        return timesDao;
    }

}
