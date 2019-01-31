package org.monkey.cid.server.service.impl;

import java.util.List;

import org.monkey.cid.server.po.Machine;
import org.monkey.cid.server.service.MachineService;
import org.springframework.stereotype.Service;

import com.cxytiandi.jdbc.EntityService;

@Service
public class MachineServiceImpl extends EntityService<Machine> implements MachineService {

	@Override
	public List<Machine> queryMachineList(int start, int limit) {
		return super.listForPage(start, limit);
	}

	@Override
	public long queryMachineCount() {
		return super.count();
	}

	@Override
	public Long addMachine(Machine machine) {
		return (Long) super.save(machine);
	}

	@Override
	public void updateMachine(Machine machine) {
		super.updateByContainsFields(machine, "id", 
				new String[] { "intranet_ip", "extranet_ip", "client_url", "publish_catalog", "update_time", "environment" });
	}

	@Override
	public List<Machine> queryAllMachineList() {
		return super.list();
	}

	@Override
	public Machine get(Long id) {
		return super.getById("id", id);
	}

}
