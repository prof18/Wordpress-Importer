import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import objects.Article;
import objects.User;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class XMLFactory {

    public void createXML(User author, ArrayList<Article> articles, boolean isNew) throws Exception {

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            //root elements
            // root elements
            // ########### RSS
            Document doc = docBuilder.newDocument();
            Element rssElement = doc.createElement("rss");
            doc.appendChild(rssElement);

            Attr attr = doc.createAttribute("version");
            attr.setValue("2.0");
            rssElement.setAttributeNode(attr);

            attr = doc.createAttribute("xmlns:excerpt");
            attr.setValue("http://wordpress.org/export/1.2/excerpt/");
            rssElement.setAttributeNode(attr);

            attr = doc.createAttribute("xmlns:content");
            attr.setValue("http://purl.org/rss/1.0/modules/content/");
            rssElement.setAttributeNode(attr);

            attr = doc.createAttribute("xmlns:wfw");
            attr.setValue("http://wellformedweb.org/CommentAPI/");
            rssElement.setAttributeNode(attr);

            attr = doc.createAttribute("xmlns:dc");
            attr.setValue("http://purl.org/dc/elements/1.1/");
            rssElement.setAttributeNode(attr);

            attr = doc.createAttribute("xmlns:wp");
            attr.setValue("http://wordpress.org/export/1.2/");
            rssElement.setAttributeNode(attr);
            // ########### END RSS

            // ########### CHANNEL
            Element channel = doc.createElement("channel");
            rssElement.appendChild(channel);

            Element language = doc.createElement("language");
            language.appendChild(doc.createTextNode("it"));
            channel.appendChild(language);

            Element wxr = doc.createElement("wp:wxr_version");
            wxr.appendChild(doc.createTextNode("1.2"));
            channel.appendChild(wxr);
            // ########### END CHANNEL

            // ########### WP AUTHOR
            Element wpAuthor = doc.createElement("wp:author");
            channel.appendChild(wpAuthor);

            Element authorId = doc.createElement("wp:author_id");
            authorId.appendChild(doc.createTextNode(String.valueOf(author.getId())));
            wpAuthor.appendChild(authorId);

            Element authorLogin = doc.createElement("wp:author_login");
            CDATASection cAuthor = doc.createCDATASection(author.getUsername());
            authorLogin.appendChild(cAuthor);
            wpAuthor.appendChild(authorLogin);

            Element authorEmail = doc.createElement("wp:author_email");
            CDATASection cEmail = doc.createCDATASection(author.getEmail());
            authorEmail.appendChild(cEmail);
            wpAuthor.appendChild(authorEmail);

            Element authorDisplay = doc.createElement("wp:author_display_name");
            CDATASection cAuthorDisplay = doc.createCDATASection(author.getName() + " " + author.getSurname());
            authorDisplay.appendChild(cAuthorDisplay);
            wpAuthor.appendChild(authorDisplay);

            Element authorName = doc.createElement("wp:author_first_name");
            CDATASection cAuthorName = doc.createCDATASection(author.getName());
            authorName.appendChild(cAuthorName);
            wpAuthor.appendChild(authorName);

            Element authorSurname = doc.createElement("wp:author_last_name");
            CDATASection cAuthorSurname = doc.createCDATASection(author.getSurname());
            authorSurname.appendChild(cAuthorSurname);
            wpAuthor.appendChild(authorSurname);
            // ########### END WP AUTHOR

            // ########### GENERATOR
            Element generator = doc.createElement("generator");
            generator.appendChild(doc.createTextNode("https://wordpress.org/?v=4.8.1"));
            channel.appendChild(generator);
            // ########### END GENERATOR

            for (Article article : articles) {

                // ########### ITEM
                Element item = doc.createElement("item");
                channel.appendChild(item);

                Element title = doc.createElement("title");
                title.appendChild(doc.createTextNode(article.getTitle()));
                item.appendChild(title);

                Element pubDate = doc.createElement("pubDate");
                SimpleDateFormat dt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
                String date = dt.format(article.getPubDate());
                pubDate.appendChild(doc.createTextNode(date));
                item.appendChild(pubDate);

                Element creator = doc.createElement("dc:creator");
                creator.appendChild(doc.createCDATASection(author.getVisibleName()));
                item.appendChild(creator);

                Element description = doc.createElement("description");
                description.appendChild(doc.createTextNode(article.getDescription()));
                item.appendChild(description);

                Element content = doc.createElement("content:encoded");
                content.appendChild(doc.createCDATASection(article.getContent()));
                item.appendChild(content);

                Element postId = doc.createElement("wp:post_id");
                postId.appendChild(doc.createTextNode(String.valueOf(article.getId())));
                item.appendChild(postId);

                Element postDate = doc.createElement("wp:post_date");
                SimpleDateFormat dt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date2 = dt2.format(article.getPubDate());
                postDate.appendChild(doc.createCDATASection(date2));
                item.appendChild(postDate);

                Element status = doc.createElement("wp:status");
                status.appendChild(doc.createTextNode("publish"));
                item.appendChild(status);

                Element type = doc.createElement("wp:post_type");
                type.appendChild(doc.createTextNode("post"));
                item.appendChild(type);

                for (String category : article.getCategories()) {

                    Element cat = doc.createElement("category");

                    attr = doc.createAttribute("domain");
                    attr.setValue("category");
                    cat.setAttributeNode(attr);

                    attr = doc.createAttribute("nicename");
                    attr.setValue(category.toLowerCase().replace(" ", "-"));
                    cat.setAttributeNode(attr);

                    cat.appendChild(doc.createCDATASection(category));
                    item.appendChild(cat);

                }

                for (String tag : article.getTag()) {

                    Element tagg = doc.createElement("category");

                    attr = doc.createAttribute("domain");
                    attr.setValue("post_tag");
                    tagg.setAttributeNode(attr);

                    attr = doc.createAttribute("nicename");
                    attr.setValue(tag.toLowerCase());
                    tagg.setAttributeNode(attr);

                    tagg.appendChild(doc.createCDATASection(tag));
                    item.appendChild(tagg);

                }

                // ########### END ITEM
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result;

            Path path = Paths.get("");
            String pathS = path.toAbsolutePath().toString() + File.separator + "parser-output/";
            String authorS = author.getName().toLowerCase() + author.getSurname().toLowerCase();

            if (isNew)
                result = new StreamResult(new File(pathS + authorS + "-new.xml"));
            else
                result = new StreamResult(new File(pathS + authorS + "-old.xml"));

            transformer.transform(source, result);

            System.out.println("File saved for " + authorS);

        } catch (Exception e) {
            throw e;
        }

    }


}
