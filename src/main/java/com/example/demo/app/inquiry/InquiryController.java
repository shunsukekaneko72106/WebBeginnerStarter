package com.example.demo.app.inquiry;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
//エンドポイントをinquiryで指定
@RequestMapping("/inquiry")
public class InquiryController {

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

		redirectAttributes.addFlashAttribute("complete", "Registered!");
		return "redirect:/inquiry/form";
	}
}
