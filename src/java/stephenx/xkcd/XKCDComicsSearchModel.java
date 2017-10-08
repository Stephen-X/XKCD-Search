package stephenx.xkcd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Model of the XKCD Comics Search app.
 * 
 * @author Stephen Xie &lt;***@andrew.cmu.edu&gt;
 */
public class XKCDComicsSearchModel {
    
    // url address for XKCD archive
    private static final String archiveURL = "https://xkcd.com/archive/";
    // base url for accessing individual comics
    private static final String baseURL = "https://xkcd.com";
    
    // a list containing all the comic titles and their respective URLs as tuples;
    // note that different comics may share the same title.
    // for each tuple, tuple.x = comic title (in lower cases), tuple.y = URL
    private List<Tuple<String, String>> comics;
    private Random r;
    
    
    /**
     * Constructor that fetches all comic information from XKCD.
     * 
     * @throws IOException Error fetching content from site; maybe the site is
     *  down or something's wrong with our server?
     */
    public XKCDComicsSearchModel() throws IOException {
        r = new Random();
        this.comics = extractArchive();
        // simulates content extraction failure
        // throw new IOException("Test failure.");
        System.out.println("XKCD Archive has been loaded to memory.");
    }

    
    /**
     * Search comic archive titles with given keyword.
     * 
     * @param keyword keyword to be searched in comic titles
     * @return a tuple with field x as the number of comics whose title matches
     *  the given keyword, and field y as a randomly picked URL from all found
     *  comics (it will be null if no comic was found)
     */
    public Tuple<Integer, String> searchComics(String keyword) {
        List<String> urlList = new ArrayList<>();
        String keywordUpper = keyword.toLowerCase();
        System.out.println("A new search is initialized: " + keywordUpper);
        
        // search for all matching comics
        for (Tuple<String, String> t : comics) {
            if (t.x.contains(keywordUpper)) {
                urlList.add(t.y);
            }
        }
        
        // return result according to total number of matches
        int count = urlList.size();
        if (count > 1) {
            // randomly pick an URL if count > 1
            return new Tuple<>(count, urlList.get(r.nextInt(count)));
            
        } else if (count == 1)
            return new Tuple<>(1, urlList.get(0));
        
        else
            return new Tuple<>(0, null);
        
    }
    
    
    /**
     * Get total number of comics in XKCD Archive.
     * 
     * @return total number of comics in XKCD Archive
     */
    public int getTotal() {
        return comics.size();
    }
    
    
    /**
     * Get comic title and the picture URL according to its permanent URL.
     * 
     * @param url permanent URL to the comic
     * @return a tuple with field x as the comic title, and field y as the
     *  picture URL
     * @throws IOException Error fetching content from site; maybe the site is
     *  down or something's wrong with our server?
     */
    public static Tuple<String, String> getComic(String url) throws IOException {
        String title, picURL;
        
        // fetch page content
        String raw = fetch(url);
        
        // first get the title; it's enclosed in a pair of <div id="ctitle">
        
        String start = "<div id=\"ctitle\">";
        String end = "</div>";
        // find the starting index of the middleContainer
        int leftIndex = raw.indexOf(start);
        if (leftIndex < 0) {
            System.out.println("ctitle not found.");
            throw new IOException("ctitle not found.");
            
        } else {
            // skip the <div> tag
            leftIndex += start.length();
        }
        // then look for the ending index
        int rightIndex = raw.indexOf(end, leftIndex);
        // title found
        title = raw.substring(leftIndex, rightIndex);
        
        
        // now find the picture URL; it's enclosed in a pair of <div id="comic">;
        // extract "src" attribute inside the <img> tag to get the image
        start = "<div id=\"comic\">";
        leftIndex = raw.indexOf(start);
        if (leftIndex < 0) {
            System.out.println("comic not found.");
            throw new IOException("comic not found.");
            
        } else {
            leftIndex += start.length();
        }
        rightIndex = raw.indexOf(end, leftIndex);
        // use regex to extract valid srcset from <img>
        // Note: try the excellent https://regexr.com/ to test regex!
        // also some good practices: https://www.loggly.com/blog/regexes-the-bad-better-best/ !
        Pattern p = Pattern.compile("<img.+?src=\"([^\"]+?)\".*?>");
        Matcher m = p.matcher(raw.substring(leftIndex, rightIndex));
        if (m.find()) {
            picURL = m.group(1);  // get the URL from the capturing group
        } else {
            System.out.println("valid img not found.");
            throw new IOException("valid img not found.");
        }
        
        
        return new Tuple<>(title, picURL);
        
    }
    
    
    /**
     * <p>Extract all comic titles and addresses from XKCD archive and return
     * the result.</p>
     * <p>XKCD Archive address: <a href="https://xkcd.com/archive/">
     * https://xkcd.com/archive/</a></p>
     * 
     * @return a list of tuples with field x as comic titles and field y as
     * respective URLs
     * @throws IOException Error fetching content from site; maybe the site is
     *  down or something's wrong with our server?
     */
    private static List<Tuple<String, String>> extractArchive() throws IOException {
        // fetch page content
        String raw = fetch(archiveURL);
        
        // all the comic information are contained in a <div> with a
        // "middleContainer" id
        
        String start = "<div id=\"middleContainer\" class=\"box\">";
        String end = "</div>";
        // find the starting index of the middleContainer
        int leftIndex = raw.indexOf(start);
        if (leftIndex < 0) {
            System.out.println("middleContainer not found.");
            throw new IOException("middleContainer not found.");
            
        } else {
            // skip the <div> tag
            leftIndex += start.length();
            
        }
        // then look for the ending index
        int rightIndex = raw.indexOf(end, leftIndex);
        
        
        // now, use regex to extract all substrings that match the link pattern
        // such as '<a href="/1892/" title="2017-9-20">USB Cables</a>'
        
        // Note: try the excellent https://regexr.com/ to test regex!
        // also some good practices: https://www.loggly.com/blog/regexes-the-bad-better-best/ !
        List<Tuple<String, String>> result = new ArrayList<>();
        Pattern p = Pattern.compile("<a.+?href=\"([^\"]+?)\".*?>(.+?)<\\/a>");
        Matcher m = p.matcher(raw.substring(leftIndex, rightIndex));
        while (m.find()) {
            // the address in href is in capturing group 1, and the title in
            // group 2 (count the parenthesis pairs for group #!)
            result.add(new Tuple<>(m.group(2).toLowerCase(), (baseURL + m.group(1))));
            
        }
        
        
        if (result.size() > 0) {
            System.out.println("Total comics loaded to memory: " + result.size());
            return result;

        } else {
            System.out.println("No information found in middleContainer: has the "
                             + "website changed its pattern?");
            throw new IOException("middleContainer is empty.");
        }
        
    }
    
    
    /**
     * Make an HTTP request to a given URL and return raw response data
     * from the HTTP GET.
     * 
     * @param url the given URL to be fetched
     * @return raw response data from the HTTP GET
     * @throws IOException Error fetching content from site; maybe the site is
     *  down or something's wrong with our server?
     */
    private static String fetch(String urlString) throws IOException {
        StringBuilder response = new StringBuilder();
        URL url = new URL(urlString);
        // Create an HttpURLConnection. This is useful for setting headers
        // and for getting the path of the resource that is returned, which
        // may be different than the above URL if redirected.
        // Use HttpsURLConnection instead if required.
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
        // read all text returned by the server
        try (
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"))
        ){
            // then append each line to response
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
                // note that newline is not included in response
            }
            
        }
        
        return response.toString();
    }
    
}
