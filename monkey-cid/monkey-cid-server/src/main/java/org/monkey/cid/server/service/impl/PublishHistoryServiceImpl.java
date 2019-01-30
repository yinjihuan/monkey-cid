package org.monkey.cid.server.service.impl;

import org.monkey.cid.server.po.PublishHistory;
import org.monkey.cid.server.service.PublishHistoryService;
import org.springframework.stereotype.Service;

import com.cxytiandi.jdbc.EntityService;
import com.cxytiandi.jdbc.Orders;
import com.cxytiandi.jdbc.Orders.OrderyType;

@Service
public class PublishHistoryServiceImpl extends EntityService<PublishHistory> implements PublishHistoryService {

	@Override
	public void save(PublishHistory history) {
		super.save(history);
	}

	@Override
	public PublishHistory getLatelyPublishHistory(Long projectId) {
		return super.getByParams(new String[] { "project_id" }, 
				new Object[] { projectId } ,
				new Orders[] { new Orders("id", OrderyType.DESC) });
	}

}
