package shypovskih.com.transactions.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import shypovskikh.com.DAO.MP3Dao;
import shypovskikh.com.SQLiteDAO.SQLiteDAO;
import shypovskikh.com.model.Author;
import shypovskikh.com.model.MP3;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
       MP3 firstMP3 = new MP3();
       firstMP3.setName("Lady in Red");
    	
       Author firstAuthor = new Author();
       firstAuthor.setName("Cris De Burge");
       firstMP3.setAuthor(firstAuthor);
       
       ApplicationContext context = new ClassPathXmlApplicationContext("AppContext.xml");
        MP3Dao mp3Dao = (MP3Dao) context.getBean("sqliteDAO");
       System.out.println("Added record quantity: "+mp3Dao.insertMP3(firstMP3));
        
    }
}
