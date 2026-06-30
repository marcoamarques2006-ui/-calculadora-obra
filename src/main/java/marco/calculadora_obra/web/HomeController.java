package marco.calculadora_obra.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Redireciona a raiz "/" para a tela inicial JSF.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/index.xhtml";
    }
}
