package com.example.demo.app.inquiry;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Inquiry;
import com.example.demo.service.InquiryService;

@Controller
//エンドポイントをinquiryで指定
@RequestMapping("/inquiry")
public class InquiryController {

	private final InquiryService inquiryService;

	@Autowired
	public InquiryController(InquiryService inquiryService){
		this.inquiryService = inquiryService;
	}

	//一覧表示
	@GetMapping
	public String index(Model model){
		List<Inquiry> list = inquiryService.getAll();

		model.addAttribute("inquiryList", list);
		model.addAttribute("title", "お問い合わせ一覧");

		return "inquiry/index";
	}


	//GETリクエスト
	@GetMapping("/form")
	public String form(InquiryForm inquiryForm, Model model,
										@ModelAttribute("complete") String complete) {
		model.addAttribute("title", "お問い合わせ");
		return "inquiry/form";
	}

	//POSTリクエスト
	@PostMapping("/form")
	public String formGoBack(InquiryForm inquiryForm, Model model) {
		model.addAttribute("title", "お問い合わせ");
		return "inquiry/form";
	}

	//確認ページ
	@PostMapping("/confirm")
	public String confirm(@Validated InquiryForm inquiryForm, BindingResult result, Model model){
		if(result.hasErrors()){
			model.addAttribute("title", "フォームページ");
			return "inquiry/form";
		}

		model.addAttribute("title", "確認ページ");
		return "inquiry/confirm";
	}

	//完了ページ
	@PostMapping("/complete")
	public String complete(@Validated InquiryForm inquiryForm, BindingResult result, 
												Model model, RedirectAttributes redirectAttributes){
		if(result.hasErrors()){
			model.addAttribute("title", "フォームページ");
			return "inquiry/form";
		}

		//DB登録
		Inquiry inquiry = new Inquiry();
		inquiry.setName(inquiryForm.getName());
		inquiry.setEmail(inquiryForm.getEmail());
		inquiry.setContents(inquiryForm.getContents());
		inquiry.setCreated(LocalDateTime.now());

		inquiryService.save(inquiry);

		//リダイレクト処理
		redirectAttributes.addFlashAttribute("complete", "Registered!");
		return "redirect:/inquiry/form";
	}
}
