package fr.eni.team42.enchere.filter;

import fr.eni.team42.enchere.bo.Utilisateur;
import fr.eni.team42.enchere.messages.LecteurMessage;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/user/*","/enchere/*"})
public class FilterActiveSession implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        HttpSession session = httpServletRequest.getSession(false);
        Utilisateur utilisateurConnecte = (Utilisateur) session.getAttribute("utilisateurConnecte");

        //S'il n'y a pas de session active, on redirige l'utilisateur vers une 403
        if (utilisateurConnecte == null) {
            RequestDispatcher rd = request.getRequestDispatcher("/accueil");
            request.setAttribute("erreur", LecteurMessage.getMessageErreur(FiltreExceptionCode.USER_NOT_CONNECTED));
            rd.forward(request,response);

            //sinon on laisse passer la requête
        } else {
            chain.doFilter(httpServletRequest, httpServletResponse);
        }
    }
}
