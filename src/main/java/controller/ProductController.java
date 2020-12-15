package controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import configuration.PrimitiveNumberEditor;
import model.ProductBean;
import model.ProductService;

@Controller
@RequestMapping("/pages")
public class ProductController {
	@Autowired
	private ProductService productService;
	
	@InitBinder
	public void registEditor(WebDataBinder binder) {
		binder.registerCustomEditor(java.util.Date.class,
				new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
		binder.registerCustomEditor(int.class, new PrimitiveNumberEditor(Integer.class, true));
		binder.registerCustomEditor(double.class, new PrimitiveNumberEditor(Double.class, true));
	}
	
	@RequestMapping(
			path = {"/product.controller"},
			method = {RequestMethod.GET, RequestMethod.POST}
	)
	public String handlerMethod(ProductBean bean, BindingResult bindingResult,
			String prodaction, Model model) {
//接收資料
		Map<String, String> errors = new HashMap<String, String>();
		model.addAttribute("errors", errors);
		
//轉換資料
		if(bindingResult!=null && bindingResult.hasFieldErrors()) {
			if(bindingResult.hasFieldErrors("id")) {
				errors.put("id", "Id must be an integer (form)");
			}
			if(bindingResult.hasFieldErrors("price")) {
				errors.put("price", "Price must be a number (form)");	
			}
			if(bindingResult.hasFieldErrors("make")) {
				errors.put("make", "Make must be a date of YYYY-MM-DD (form)");
			}
			if(bindingResult.hasFieldErrors("expire")) {
				errors.put("expire", "Expire must be an integer (form)");
			}
		}
		
//驗證資料
		if(prodaction!=null) {
			if(prodaction.equals("Insert") || prodaction.equals("Update") || prodaction.equals("Delete")) {
				if(bean==null || bean.getId()==0) {
					errors.put("id", "Please enter Id for "+prodaction+"(form)");
				}
			}
		}
		
		if(errors!=null && !errors.isEmpty()) {
			return "/pages/product";
		}
		
//呼叫Model
//根據Model執行結果導向View
		if(prodaction!=null && prodaction.equals("Select")) {
			List<ProductBean> result = productService.select(bean);
			model.addAttribute("select", result);
			return "/pages/display";
		} else if(prodaction!=null && prodaction.equals("Insert")) {
			ProductBean result = productService.insert(bean);
			if(result==null) {
				errors.put("action", "Insert fail");
			} else {
				model.addAttribute("insert", result);
			}
			return "/pages/product";
		} else if(prodaction!=null && prodaction.equals("Update")) {
			ProductBean result = productService.update(bean);
			if(result==null) {
				errors.put("action", "Update fail");
			} else {
				model.addAttribute("update", result);
			}
			return "/pages/product";
		} else if(prodaction!=null && prodaction.equals("Delete")) {
			boolean result = productService.delete(bean);
			if(!result) {
				model.addAttribute("delete", 0);
			} else {
				model.addAttribute("delete", 1);
			}
			return "/pages/product";
		} else  {
			errors.put("action", "Unknown Action:"+prodaction);
			return "/pages/product";
		}
	}
	@GetMapping("/product")
	public String product() {
		return "/pages/product";
	}
}
