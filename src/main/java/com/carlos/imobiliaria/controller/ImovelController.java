package com.carlos.imobiliaria.controller;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.carlos.imobiliaria.filter.FilterDesc;
import com.carlos.imobiliaria.model.Bairro;
import com.carlos.imobiliaria.model.Categoria;
import com.carlos.imobiliaria.model.Estado;
import com.carlos.imobiliaria.model.Imovel;
import com.carlos.imobiliaria.model.Municipio;
import com.carlos.imobiliaria.model.Negocio;
import com.carlos.imobiliaria.repository.BairroRepository;
import com.carlos.imobiliaria.repository.CategoriaRepository;
import com.carlos.imobiliaria.repository.EstadoRepository;
import com.carlos.imobiliaria.repository.MunicipioRepository;
import com.carlos.imobiliaria.repository.NegocioRepository;
import com.carlos.imobiliaria.repository.UserRepository;
import com.carlos.imobiliaria.service.BairroService;
import com.carlos.imobiliaria.service.ImovelService;
import com.carlos.imobiliaria.service.MunicipioService;
import com.carlos.imobiliaria.service.SenderMailService;

@Controller
@RequestMapping("/imoveis")
public class ImovelController {

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private NegocioRepository negocioRepository;

	@Autowired
	private BairroRepository bairroRepository;

	@Autowired
	private BairroService bairroService;

	@Autowired
	private MunicipioRepository municipioRepository;

	@Autowired
	private MunicipioService municipioService;


	@Autowired
	private ImovelService imovelService;
	
	@Autowired
	private SenderMailService mailSender;
	
	@Autowired
	UserRepository userRepository;

	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable("pageNo") int pageNo, Model model,
			@ModelAttribute("filtro") FilterDesc filtro, @RequestParam(defaultValue = "nome") String radioFilter,
			RedirectAttributes attributes) {
		int pageSize = 3;// Número de registros por página

		Page<Imovel> page = null;

		try {
			page = imovelService.filtrarPage(pageNo, pageSize, filtro, radioFilter);
		} catch (NumberFormatException e) {
			attributes.addFlashAttribute("mensagemIntegridade", "Favor inserir um número na caixa de pesquisa.");
			return "redirect:/imoveis/page/1";
		}

		List<Imovel> listImovel = page.getContent();

		String filterParameters = imovelService.urlParams(filtro, radioFilter);

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("imoveis", listImovel);
		model.addAttribute("filterParameters", filterParameters);

		return "ListaImoveis";

	}

	@GetMapping("/novo")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView("CadastroImovel");
		mv.addObject(new Imovel());
		return mv;
	}

	@PostMapping
	public String salvar(@Validated Imovel imovel, Errors errors, RedirectAttributes attributes,
			@RequestParam(required = false, value = "fileImage") MultipartFile multipartFile) throws IOException, MessagingException {
		
				
		
		if (errors.hasErrors()) {
			return "CadastroImovel";
		}
		if (multipartFile.getOriginalFilename().equals("")) {// Se não receber o arquivo na requisição
			imovelService.salvar(imovel);

		} else {

			imovelService.salvarComArquivo(imovel, multipartFile);

		}
		mailSender.enviar(imovel,multipartFile);//Envio de e-mail
		attributes.addFlashAttribute("mensagem", "Imóvel cadastrado com sucesso!");
		return "redirect:/imoveis/novo";
	}

	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable("codigo") Imovel imovel) {
		ModelAndView mv = new ModelAndView("CadastroImovel");
		mv.addObject(imovel);
		return mv;
	}

	@DeleteMapping("/{codigo}")
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {

		imovelService.excluir(codigo);
		attributes.addFlashAttribute("mensagem", "Imovel excluído com sucesso!");
		return "redirect:/imoveis/page/1";
	}

	@ModelAttribute("categorias")
	public List<Categoria> categorias() {
		return categoriaRepository.findAll();
	}

	@ModelAttribute("negocios")
	public List<Negocio> negocios() {
		return negocioRepository.findAll();
	}

	@ModelAttribute("estados")
	public List<Estado> estados() {
		return estadoRepository.findAll();

	}

	@ModelAttribute("municipios")
	public List<Municipio> municipios() {
		return municipioRepository.findAll(); // Disponibilizando lista de municípios para a View

	}

	@ModelAttribute("bairros")
	public List<Bairro> bairros() {
		return bairroRepository.findAll();
	}

	@GetMapping("/listarMunicipioPorEstado")
	@ResponseBody
	public List<Municipio> listarMunicipioPorEstado(Long codigoEstado) {

		List<Municipio> listaMunicipios = municipioService.encontrarMunicipiosPorEstado(codigoEstado);// Filtro baseando
																										// no select da
																										// View
																										// CadastroImoveis

		return listaMunicipios;
	}

	@GetMapping("/listarBairroPorMunicipio")
	@ResponseBody
	public List<Bairro> listarBairroPorMunicipio(Long codigoMunicipio) {

		List<Bairro> listaBairro = bairroService.encontrarBairroPorMunicipio(codigoMunicipio); // Filtro baseando no
																								// select da View
																								// CadastroImoveis

		return listaBairro;
	}

}
