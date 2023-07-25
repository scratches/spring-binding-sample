package com.example.demo;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractView;

import com.example.demo.TemplateModel.Form;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@SpringBootApplication
@Controller
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	Todos todos() {
		return new Todos() {
			@Override
			public Todo save(Todo todo) {
				return todo;
			}
		};
	}

}

@Controller
class TodoController {

	private final TemplateModel template;

	public TodoController(TemplateModel template) {
		this.template = template;
	}

	@PostMapping("/")
	View create(@Valid @ModelAttribute Form form, @RequestParam Optional<String> filter) {
		template.save(form);
		return new AbstractView() {
			@Override
			protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
					HttpServletResponse response) throws Exception {
				response.getWriter().println("<form id=\"new-todo\">" + form.title() + "</form>");
			}

		};
	}

}

interface Todos {
	Todo save(Todo todo);
}

record Todo(UUID id, String title, boolean completed) {
}

@Component
class TemplateModel {
	private final Todos todos;

	public TemplateModel(Todos todos) {
		this.todos = todos;
	}

	public void save(Form form) {
		todos.save(new Todo(UUID.randomUUID(), form.title(), false));
	}

	public record Form(@NotBlank String title, String action) {
		Form() {
			this("");
		}
		Form(String title) {
			this(title, null);
		}
	}
}