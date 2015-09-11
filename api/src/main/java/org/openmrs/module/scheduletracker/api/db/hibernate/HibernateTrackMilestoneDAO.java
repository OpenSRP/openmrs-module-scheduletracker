package org.openmrs.module.scheduletracker.api.db.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.scheduletracker.TrackMilestone;
import org.openmrs.module.scheduletracker.api.db.TrackMilestoneDAO;

public class HibernateTrackMilestoneDAO implements TrackMilestoneDAO{

	protected final Log log = LogFactory.getLog(getClass());
	private SessionFactory sessionFactory;
	private long totalRows;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}
	  
	public long LAST_QUERY_TOTAL_ROWS() {
		return totalRows;
	}

	@SuppressWarnings("unchecked")
	public List<TrackMilestone> getAll(boolean readonly, int firstResult, int maxResults, String[] mappingsToJoin) {
		Criteria cri = this.sessionFactory.getCurrentSession().createCriteria(TrackMilestone.class)
			      .setReadOnly(readonly);
	    if (mappingsToJoin != null)
	    {
	      String[] arrayOfString;
	      int j = (arrayOfString = mappingsToJoin).length;
	      for (int i = 0; i < j; i++)
	      {
	        String mapping = arrayOfString[i];
	        cri.setFetchMode(mapping, FetchMode.JOIN);
	      }
	    }
	    
	    totalRows = (Long)cri.setProjection(Projections.rowCount()).uniqueResult();
	    
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<TrackMilestone> list = cri.setFirstResult(firstResult).setMaxResults(maxResults).list();
	    return list;
	}

	@SuppressWarnings("unchecked")
	public TrackMilestone get(int id, boolean readonly) {
		Criteria cri = this.sessionFactory.getCurrentSession().createCriteria(TrackMilestone.class)
			      .setReadOnly(readonly);
	    
		cri.add(Restrictions.eq("trackMilestoneId", id));
		
	    totalRows = (Long)cri.setProjection(Projections.rowCount()).uniqueResult();
	    
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<TrackMilestone> list = cri.list();
	    return list.size()>0?list.get(0):null;
	}

	public Serializable save(TrackMilestone obj) {
		return sessionFactory.getCurrentSession().save(obj);
	}

	public void update(TrackMilestone obj) {
		sessionFactory.getCurrentSession().update(obj);
	}

	public void delete(TrackMilestone obj) {
		sessionFactory.getCurrentSession().delete(obj);
	}

	@SuppressWarnings("unchecked")
	public List<TrackMilestone> search(String schedule, Integer[] beneficiary,
			Integer[] recipient, Date from, Date to, String status,
			boolean readonly, int firstResult, int maxResults, String[] mappingsToJoin) {
		Criteria cri = this.sessionFactory.getCurrentSession().createCriteria(TrackMilestone.class)
			      .setReadOnly(readonly);
	    
		if(schedule != null){
			cri.createAlias("schedule", "sch").add(Restrictions.eq("sch.name", schedule));
		}

		if(beneficiary != null) {
			cri.createAlias("beneficiary", "b").add(Restrictions.in("b.personId", beneficiary));
		}

		if(recipient != null) {
			cri.createAlias("recipient", "r").add(Restrictions.in("r.personId", recipient));
		}

		if(from != null && to != null){
			cri.add(Restrictions.disjunction()
					.add(Restrictions.between("dateEnrolled", from, to))
					.add(Restrictions.between("earlyStartDate", from, to))
					.add(Restrictions.between("dueStartDate", from, to))
					.add(Restrictions.between("lateStartDate", from, to))
					.add(Restrictions.between("maxStartDate", from, to)));
		}
		if(status != null){
			cri.add(Restrictions.eq("status", status));
		}

		if (mappingsToJoin != null)
	    {
	      String[] arrayOfString;
	      int j = (arrayOfString = mappingsToJoin).length;
	      for (int i = 0; i < j; i++)
	      {
	        String mapping = arrayOfString[i];
	        cri.setFetchMode(mapping, FetchMode.JOIN);
	      }
	    }
		
	    totalRows = (Long)cri.setProjection(Projections.rowCount()).uniqueResult();
	    
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<TrackMilestone> list = cri.setFirstResult(firstResult).setMaxResults(maxResults).list();
	    return list;
	}

	@SuppressWarnings("unchecked")
	public List<TrackMilestone> getBySchedule(int schedule, boolean readonly,
			int firstResult, int maxResults, String[] mappingsToJoin) {
		Criteria cri = this.sessionFactory.getCurrentSession().createCriteria(TrackMilestone.class)
			      .setReadOnly(readonly);
	    
		cri.createAlias("track", "t").createAlias("t.schedule", "ts")
			.add(Restrictions.eq("ts.scheduleId", schedule));
		
	    totalRows = (Long)cri.setProjection(Projections.rowCount()).uniqueResult();
	    
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<TrackMilestone> list = cri.list();
	    return list;
	}

	@SuppressWarnings("unchecked")
	public List<TrackMilestone> getByTrack(int track, boolean readonly) {
		Criteria cri = this.sessionFactory.getCurrentSession().createCriteria(TrackMilestone.class)
			      .setReadOnly(readonly);
	    
		cri.createAlias("track", "t").add(Restrictions.eq("t.trackId", track));
		
	    totalRows = (Long)cri.setProjection(Projections.rowCount()).uniqueResult();
	    
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<TrackMilestone> list = cri.list();
	    return list;
	}

	@SuppressWarnings("unchecked")
	public List<TrackMilestone> getByMilestone(int milestone, boolean readonly,
			int firstResult, int maxResults, String[] mappingsToJoin) {
		Criteria cri = this.sessionFactory.getCurrentSession().createCriteria(TrackMilestone.class)
			      .setReadOnly(readonly);
	    
		cri.createAlias("milestone", "m").add(Restrictions.eq("m.milestoneId", milestone));
		
	    totalRows = (Long)cri.setProjection(Projections.rowCount()).uniqueResult();
	    
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<TrackMilestone> list = cri.list();
	    return list;
	}

	@SuppressWarnings("unchecked")
	public TrackMilestone getByUuid(String uuid, boolean readonly) {
		Criteria cri = this.sessionFactory.getCurrentSession().createCriteria(TrackMilestone.class)
			      .setReadOnly(readonly);
	    
		cri.add(Restrictions.eq("uuid", uuid));
		
	    totalRows = (Long)cri.setProjection(Projections.rowCount()).uniqueResult();
	    
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<TrackMilestone> list = cri.list();
	    return list.size()>0?list.get(0):null;
	}
}
