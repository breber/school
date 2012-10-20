import pagerank
import webapp2
            
class PageRankRequest(webapp2.RequestHandler):
    def get(self):
        self.response.out.write("<form action='/' method='post'>")
        self.response.out.write("<textarea rows='40' cols='20' name='data'></textarea>")
        self.response.out.write("<table>")
        self.response.out.write("<tr><td><b>P-Val: </b></td><td><input type='text' name='pval' value='.85' /></td></tr>")
        self.response.out.write("<tr><td><b>Minimum Difference: </b></td><td><input type='text' name='minDiff' value='.001' /></td></tr>")
        self.response.out.write("<tr><td><b>Default Rank: </b></td><td><input type='text' name='defRank' value='1' /></td></tr>")
        self.response.out.write("</table>")
        self.response.out.write("<br /><input type='submit' value='Submit' />")
        self.response.out.write("</form>")
        
    def post(self):
        pVal = float(self.request.get('pval'))
        minDiff = float(self.request.get('minDiff'))
        defaultRank = float(self.request.get('defRank'))
        val  = pagerank.PageRank(pVal, minDiff, defaultRank)
        data = self.request.get('data')
        processed = val.main(data)
        self.response.out.write("<table>")
        
        for v in processed:
            self.response.out.write("<tr><td>" + str(v.key) + "</td><td>" + str(v.value.getCurrentRank()) + "</td></tr>")
        
        self.response.out.write("</table>")

app = webapp2.WSGIApplication([
    ('/', PageRankRequest)
], debug=True)
