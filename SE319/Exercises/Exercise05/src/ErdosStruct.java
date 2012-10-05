import java.util.Vector;

/**
 * 
 * @author sbasu
 * 	 The tree data structure. Each node in the tree is of type AuthNode.
 *   The root of the tree contains a AuthNode corresponding to "Paul Erdos"
 */
public class ErdosStruct
{
	private AuthNode top = new AuthNode("Paul Erdos");
	
	/**
	 *  User defined structure
	 */
	public void createStruct()
	{
		
		// path to Samik
		top.addCoAuth(new AuthNode("Shlomo Moran"));
		top.addCoAuth(new AuthNode("S. Shelah"));
		AuthNode coAuth = top.getCoAuth(0); // get Moran
		
		// Add to Moran
		coAuth.addCoAuth(new AuthNode("Oscar Ibarra"));
		coAuth.addCoAuth(new AuthNode("Shmuel Zaks"));
		
		// get Oscar
		coAuth = coAuth.getCoAuth(0);
		
		// add to Oscar
		coAuth.addCoAuth(new AuthNode("Tevfik Bultan"));
		
		// get Tevfik
		coAuth = coAuth.getCoAuth(0);
		
		// add to Tevfik
		coAuth.addCoAuth(new AuthNode("Graham Hughes"));
		coAuth.addCoAuth(new AuthNode("Xiang Fu"));
		coAuth.addCoAuth(new AuthNode("Samik Basu"));
		
		// get Samik
		coAuth = coAuth.getCoAuth(2);
		AuthNode samik = coAuth;
		
		// add to Samik
		coAuth.addCoAuth(new AuthNode("Paul Jennings"));
		coAuth.addCoAuth(new AuthNode("Ganesh Ram Santhanam"));
		AuthNode ganesh = coAuth.getCoAuth(1);
		coAuth.addCoAuth(new AuthNode("Zach Oster"));
		AuthNode zach = coAuth.getCoAuth(2);
		
		coAuth.addCoAuth(new AuthNode("Michelle Ruse"));
		coAuth.addCoAuth(new AuthNode("Yuly Suvorov"));
		coAuth.addCoAuth(new AuthNode("Diptikalyan Saha"));
		
		// Another path
		coAuth = top.getCoAuth(1);  // get Shelah
		coAuth.addCoAuth(new AuthNode("Amir Pnueli"));
		coAuth = coAuth.getCoAuth(0);
		
		coAuth.addCoAuth(new AuthNode("Giora Slutzki"));
		coAuth = coAuth.getCoAuth(0);
		
		coAuth.addCoAuth(new AuthNode("Vasant Honavar"));
		coAuth = coAuth.getCoAuth(0);
		
		
		coAuth.addCoAuth(samik);
		coAuth.addCoAuth(ganesh);
		coAuth.addCoAuth(zach);	
	}
	
	/**
	 * 
	 * @return the root element of type AuthNode of the tree data structure
	 */
	public AuthNode getRoot() { return top; }
	
}

/**
 * 
 * @author sbasu
 *   AuthNode structure and the interfaces
 */
class AuthNode
{
	/**
	 * Each AuthNode has a name (i.e., the name the author)
	 *      and a vector of AuthNodes corresponding to co-authors
	 */
	private String name;
	private Vector<AuthNode> coAuths = new Vector<AuthNode>();
	
	/**
	 * Two types of constructor
	 */
	public AuthNode() {}
	public AuthNode(String n) { name = n; }
	
	// Self-explanatory interfaces
	public void setName(String n) { name = n; }
	public String getName() { return name; }
	
	// Understand the usage of toString
	public String toString() { return name; }
	
	public int getCoAuthCount() { return coAuths.size(); }
	
	public void addCoAuth(AuthNode coAuthor) 
	{ 
		coAuths.addElement(coAuthor); 
	}
	
	public AuthNode getCoAuth(int i)
	{
		return (AuthNode)coAuths.get(i);
	}
}