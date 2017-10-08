package stephenx.xkcd;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller of the XKCD Comics Search app.
 * 
 * @author Stephen Xie &lt;***@andrew.cmu.edu&gt;
 */
@WebServlet(name = "XKCDComicsSearchServlet",
            urlPatterns = {"/XKCDSearch"})
public class XKCDComicsSearchServlet extends HttpServlet {
    
    private XKCDComicsSearchModel model;
    private boolean isFetchSuccessful;  // has the model been initialized successfully?
    
    
    /**
     * Override servlet initialization to include model instantiation.
     */
    @Override
    public void init() {
        try {
            model = new XKCDComicsSearchModel();
            isFetchSuccessful = true;
            
        } catch (IOException ex) {
            isFetchSuccessful = false;
            Logger.getLogger(XKCDComicsSearchServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // if there's an error connecting to XKCD, abort
        if (!isFetchSuccessful) {
            request.setAttribute("fetchUnsuccessful", "true");
            // forward request & response to the welcome view
            request.getRequestDispatcher("welcome.jsp").forward(request, response);
            
            init();  // try to reload from XKCD
            
            return;
        }
        
        // get keyword for search
        String keyword = request.getParameter("title");
        
        if (keyword != null) {
            if (!keyword.isEmpty()) {
                try {
                    request.setAttribute("keyword", keyword);
                    // begin the search
                    Tuple<Integer, String> result = model.searchComics(keyword);
                    // ...and append result to request
                    request.setAttribute("total", model.getTotal());  // total number of comics in archive
                    request.setAttribute("count", result.x);  // total number of matches
                    if (result.x > 0) {
                        // get info of that random comic
                        Tuple<String, String> comic = XKCDComicsSearchModel.getComic(result.y);
                        request.setAttribute("title", comic.x);
                        request.setAttribute("url", comic.y);
                    }

                    // forward request & response to the result view
                    request.getRequestDispatcher("result.jsp").forward(request, response);
                    
                } catch (Exception e) {  // in case user loses connection to XKCD during search
                    request.setAttribute("fetchUnsuccessful", "true");
                    request.getRequestDispatcher("welcome.jsp").forward(request, response);
                    
                    init();  // try to reload from XKCD
                }

            } else {
                // user supplied an empty string; kindly ask them to retry
                request.setAttribute("keywordIsEmpty", true);
                request.getRequestDispatcher("welcome.jsp").forward(request, response);
            }
            
        } else {
            // user just entered the welcome page
            request.getRequestDispatcher("welcome.jsp").forward(request, response);
        }
        
        
    }

    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet WelcomeServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Access Denied: POST is not allowed at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }


    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "A simple search & web scraping servlet for XKCD Comics Archive.";
    }

}
