package org.monkey.cid.server.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.monkey.cid.core.base.Response;
import org.monkey.cid.core.base.ResponseData;
import org.monkey.cid.server.dto.MachineDto;
import org.monkey.cid.server.param.AddMachineParam;
import org.monkey.cid.server.po.Machine;
import org.monkey.cid.server.service.MachineService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cxytiandi.jdbc.util.PageBean;

@RestController
@RequestMapping("/rest")
public class MachineController {

	@Autowired
	private MachineService machineService;
	
	/**
	 * 查询所有机器信息
	 * @return
	 */
	@GetMapping("/machines")
	public ResponseData<List<MachineDto>> queryMachineList() {
		List<MachineDto> list = new ArrayList<MachineDto>();
		List<Machine> machines = machineService.queryAllMachineList();
		for (Machine machine : machines) {
			MachineDto dto = new MachineDto();
			dto.setId(machine.getId().toString());
			dto.setIntranetIp(machine.getIntranetIp());
			list.add(dto);
		}
		return Response.ok(list);
	}
	
	/**
	 * 分页查询机器列表
	 * @param page
	 * @return
	 */
	@GetMapping("/machine/list")
	public ResponseData<PageBean<MachineDto>> queryMachineList(int page) {
		List<MachineDto> machineDtoList = new ArrayList<MachineDto>();
		int limit = 10;
		int start = PageBean.page2Start(page, limit);
		long total = machineService.queryMachineCount();
		List<Machine> machineList = machineService.queryMachineList(start, limit);
		for (Machine machine : machineList) {
			MachineDto dto = new MachineDto();
			BeanUtils.copyProperties(machine, dto);
			dto.setId(machine.getId().toString());
			machineDtoList.add(dto);
		}
		PageBean<MachineDto> data = new PageBean<MachineDto>(start, limit, machineDtoList, total);
		return Response.ok(data);
	}
	
	@PostMapping("/machine/add")
	public ResponseData<String> addProject(@RequestBody AddMachineParam param) {
		Machine machine = new Machine();
		BeanUtils.copyProperties(param, machine);
		Date addTime = new Date();
		machine.setAddTime(addTime);
		machine.setUpdateTime(addTime);
		Long id = machineService.addMachine(machine);
		return Response.ok(id.toString());
	}
	
	@PostMapping("/machine/edit")
	public ResponseData<String> editProject(@RequestBody AddMachineParam param) {
		Machine machine = new Machine();
		BeanUtils.copyProperties(param, machine);
		machine.setId(Long.valueOf(param.getId()));
		Date updateTime = new Date();
		machine.setUpdateTime(updateTime);
		machineService.updateMachine(machine);
		return Response.ok();
	}
}
