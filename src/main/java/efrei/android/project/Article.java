package efrei.android.project;

import java.util.ArrayList;

public class Article {
	private String titre;
	private String image;
	private String link;
	private ArrayList<String> categories;
	public Article(){
		titre = "";
		image = "";
		categories = new ArrayList<String>();
	}
	public Article(String a, String site){
		String d;
		String temp2;
		String temp3;
		categories = new ArrayList<String>();
		titre = a.substring(a.indexOf("<title>")+7, a.indexOf("</title>"));
		link = a.substring(a.indexOf("<link>")+"<link>".length(), a.indexOf("</link>"));
		if(titre.indexOf("&#")>-1){
			temp2 = titre.substring(titre.indexOf("&#"));
			while(temp2.indexOf("&#")!=-1){
				int pos2 = temp2.indexOf("&#")+2;
				int pos3 = temp2.indexOf(";")+1;
				temp3 = temp2.substring(pos2, pos3);
				temp3 = temp3.substring(0, temp3.length()-1);
				System.out.println(temp3);
				temp2 = temp2.substring(pos3);
				titre = titre.replace("&#"+temp3+";", ""+(char)Integer.parseInt(temp3));
			}
		}
		if(site=="gorafi"){
			image = a.substring(a.indexOf("<thumbnail>")+11, a.indexOf("</thumbnail>"));
			String temp = a.substring(a.indexOf("<category>"));
			while(temp.indexOf("<category><![CDATA[") > -1 && temp.indexOf("]]></category>") > -1){
				categories.add(temp.substring(temp.indexOf("<category><![CDATA[")+"<category><![CDATA[".length(),temp.indexOf("]]></category>")));
				temp = temp.substring(temp.indexOf("]]></category>")+"]]></category>".length());
			}
		}else if(site=="onion")
		{	
			d = a.substring(a.indexOf("<description><![CDATA[<img src="+'"')+"<description><![CDATA[<img src=".length()+1, a.indexOf("</description>"));
			image = d.substring(0, d.indexOf('"'+" />"));
			String temp = a.substring(a.indexOf ("<category domain="+'"'+'"'+">"));
			while(temp.indexOf ("<category domain="+'"'+'"'+">") > -1 && temp.indexOf("</category>") > -1){
				temp2 = temp.substring(temp.indexOf("<category domain="+'"'+'"'+">")+"<category domain=".length()+3,temp.indexOf("</category>"));
				if(temp2.indexOf("vol 54 issue 13")<0){
					categories.add(temp2);
				}
				
				temp = temp.substring(temp.indexOf("</category>")+"</category>".length());
			}
		}
	}
	
	public String getTitre(){
		return titre;
	}
	public String getImage() {
		return image;
	}
	public ArrayList<String> getCategory() {
		return categories;
	}
	public String getLink() {
		return link;
	}
}
