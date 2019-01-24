package org.monkey.cid.server.controller;

import java.util.ArrayList;
import java.util.List;

import org.monkey.cid.core.base.Response;
import org.monkey.cid.core.base.ResponseData;
import org.monkey.cid.server.dto.MachineDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class MachineController {

	@GetMapping("/machines")
	public ResponseData<List<MachineDto>> queryMachineList() {
		List<MachineDto> list = new ArrayList<MachineDto>();
		MachineDto d = new MachineDto();
		d.setId("1");
		d.setIntranetIp("192.168.0.222");
		list.add(d);
		d = new MachineDto();
		d.setId("2");
		d.setIntranetIp("192.168.0.232");
		list.add(d);
		return Response.ok(list);
	}
}
