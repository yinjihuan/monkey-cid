package org.monkey.cid.server.service;

import java.util.List;

import org.monkey.cid.server.po.Machine;

public interface MachineService {
	/**
	 * 查询机器列表
	 * @param start
	 * @param limit
	 * @return
	 */
	List<Machine> queryMachineList(int start, int limit);
	
	/**
	 * 查询所有机器信息
	 * @return
	 */
	List<Machine> queryAllMachineList();
	
	/**
	 * 查询机器总数量
	 * @return
	 */
	long queryMachineCount();
	
	/**
	 * 新增机器
	 * @param machine
	 * @return
	 */
	Long addMachine(Machine machine);
	
	/**
	 * 修改机器
	 * @param machine
	 */
	void updateMachine(Machine machine);
	
	/**
	 * 获取机器信息
	 * @param id
	 * @return
	 */
	Machine get(Long id);
}
