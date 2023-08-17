package com.samyx.controllers;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.samyx.service.interfaces.IFEBitacoraService;

@Controller
public class HomeController {
	@Autowired
	private IFEBitacoraService _bitacoraService;

	@GetMapping({ "/" })
	public String Home(Model model, HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			String emisorIdentificacion = session.getAttribute("SESSION_IDENTIFICACION_EMPRESA").toString();
			int tfe = this._bitacoraService.countFe(emisorIdentificacion);
			int tnd = this._bitacoraService.countNd(emisorIdentificacion);
			int tnc = this._bitacoraService.countNc(emisorIdentificacion);
			int tte = this._bitacoraService.countTe(emisorIdentificacion);
			int fec = this._bitacoraService.countFec(emisorIdentificacion);
			int fee = this._bitacoraService.countFee(emisorIdentificacion);
			int totalDocs = tfe + tnd + tnc + tte + fec + fee;
			try {
				model.addAttribute("pfe", Integer.valueOf(tfe * 100 / totalDocs));
				model.addAttribute("pnd", Integer.valueOf(tnd * 100 / totalDocs));
				model.addAttribute("pnc", Integer.valueOf(tnc * 100 / totalDocs));
				model.addAttribute("pte", Integer.valueOf(tte * 100 / totalDocs));
				model.addAttribute("pfec", Integer.valueOf(fec * 100 / totalDocs));
				model.addAttribute("pfee", Integer.valueOf(fee * 100 / totalDocs));
			} catch (Exception e) {
				model.addAttribute("pfe", Integer.valueOf(0));
				model.addAttribute("pnd", Integer.valueOf(0));
				model.addAttribute("pnc", Integer.valueOf(0));
				model.addAttribute("pte", Integer.valueOf(0));
				model.addAttribute("pfec", Integer.valueOf(0));
				model.addAttribute("pfee", Integer.valueOf(0));
			}
			model.addAttribute("tfe", Integer.valueOf(tfe));
			model.addAttribute("tnd", Integer.valueOf(tnd));
			model.addAttribute("tnc", Integer.valueOf(tnc));
			model.addAttribute("tte", Integer.valueOf(tte));
			model.addAttribute("tfec", Integer.valueOf(fec));
			model.addAttribute("tfee", Integer.valueOf(fee));
			return "/home/home";
		}
		return "redirect:/emisor/";
	}
}
