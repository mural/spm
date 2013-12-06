package com.spm.common.cookie;

import java.util.List;
import org.apache.http.cookie.Cookie;

/**
 * Repository that handles {@link Cookie}s.
 * 
 * @author Estefania Caravatti
 */
public interface CookieRepository {
	
	/**
	 * Saves a {@link Cookie} into the repository.
	 * 
	 * @param cookie The {@link Cookie} to save.
	 */
	public void save(Cookie cookie);
	
	/**
	 * Saves all the given cookies in the repository.
	 * 
	 * @param cookies The {@link Cookie}s to save.
	 */
	public void saveAll(List<Cookie> cookies);
	
	/**
	 * Removes a {@link Cookie} from the repository.
	 * 
	 * @param cookie The {@link Cookie} to remove.
	 */
	public void remove(Cookie cookie);
	
	/**
	 * Removes all the cookies in the repository.
	 */
	public void removeAll();
	
	/**
	 * Gets all the {@link Cookie}s in the repository.
	 * 
	 * @return All the {@link Cookie}s.
	 */
	public List<Cookie> getAll();
}
