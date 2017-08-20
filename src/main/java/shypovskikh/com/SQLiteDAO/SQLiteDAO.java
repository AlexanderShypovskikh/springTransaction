package shypovskikh.com.SQLiteDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import shypovskikh.com.DAO.MP3Dao;
import shypovskikh.com.model.Author;
import shypovskikh.com.model.MP3;

@Component("sqliteDAO")
public class SQLiteDAO implements MP3Dao {
	//private static final String mp3Table = "mp3";
	//private static final String mp3View = "mp3_view";
	
	private NamedParameterJdbcTemplate namedJdbcTemplate; 
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertMP3(MP3 mp3) {
		System.out.println(TransactionSynchronizationManager.isActualTransactionActive());
        int idAuth= insertAuthor(mp3.getAuthor());
		
		String sql = "insert into mp3 (mp3_id_auth, mp3_name) values (:idAuth, :mp3Name)";
		MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("idAuth", idAuth);
        params.addValue("mp3Name", mp3.getName());
        
        return namedJdbcTemplate.update(sql, params);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertAuthor(Author author) {
		System.out.println(TransactionSynchronizationManager.isActualTransactionActive());
		
		int index = checkIfAuthorIsExcist(author);
		System.out.println("index = "+index);
		if(index == -1) {
		String sql = "insert into Author(auth_name) values (:authName)";
		MapSqlParameterSource params = new MapSqlParameterSource(); 
		params.addValue("authName", author.getName());
		KeyHolder key = new GeneratedKeyHolder();
		    namedJdbcTemplate.update(sql, params, key);
				   System.out.println("KeyIndex = "+key.getKey().intValue());
		return key.getKey().intValue();
		}
		else 
			return index;
		
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int checkIfAuthorIsExcist(Author author){
		System.out.println(TransactionSynchronizationManager.isActualTransactionActive());
		
		String sql = "Select * from Author where  auth_name like:authName";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("authName", "%"+author.getName().toUpperCase()+"%");
		Author result = null;
		try {
		  result = namedJdbcTemplate.queryForObject(sql, params, new AuthorRowMapper());
		}catch(EmptyResultDataAccessException ex) {
			System.out.println("Таких исполнителей нет");
		}
		if(result == null)
	    	return -1;
	    else return result.getId();
	}
	
	private static final class AuthorRowMapper implements RowMapper<Author>{

		public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            Author author = new Author();
            author.setId(rs.getInt("id_auth"));
            author.setName(rs.getString("auth_name"));
			return author;
		}
	}
	
}
