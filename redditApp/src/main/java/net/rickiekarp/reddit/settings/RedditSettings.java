package net.rickiekarp.reddit.settings;

import java.util.ArrayList;
import java.util.Date;

import org.apache.http.client.HttpClient;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.CookieSyncManager;

import net.rickiekarp.reddit.R;
import net.rickiekarp.reddit.common.Constants;
import net.rickiekarp.reddit.common.RedditIsFunHttpClientFactory;
import net.rickiekarp.reddit.common.util.StringUtils;
import net.rickiekarp.reddit.common.util.Util;
import net.rickiekarp.reddit.filters.SubredditFilter;

/**
 * Common settings
 * @author Rickie
 *
 */
public class RedditSettings {

	private static final String TAG = "RedditSettings";

	private String username = null;
	private Cookie redditSessionCookie = null;
	private String modhash = null;
	private String homepage = Constants.FRONTPAGE_STRING;

	private boolean useExternalBrowser = false;
	private boolean showCommentGuideLines = true;
	private boolean confirmQuitOrLogout = true;
	private boolean saveHistory = true;
	private boolean alwaysShowNextPrevious = true;

	private String useragent = Constants.BROWSER_UA_STRING;
	private boolean loadJavascript = true;
	private boolean loadPlugins = true;
	private boolean overWriteUA = false;
	/**
	 * Should the browser attempt to load imgur images directly?
	 */
	private boolean loadImgurImagesDirectly = true;
	private boolean loadVredditLinksDirectly = false;

	private int threadDownloadLimit = Constants.DEFAULT_THREAD_DOWNLOAD_LIMIT;
	private String commentsSortByUrl = Constants.CommentsSort.SORT_BY_BEST_URL;

	private boolean showNSFW = false;

	// --- Themes ---
	private int theme = R.style.Reddit_Dark_Medium;
	private int rotation = -1;  // -1 means unspecified
	private boolean loadThumbnails = true;
	private boolean loadThumbnailsOnlyWifi = false;

	private String mailNotificationStyle = Constants.PREF_MAIL_NOTIFICATION_STYLE_DEFAULT;
	private String mailNotificationService = Constants.PREF_MAIL_NOTIFICATION_SERVICE_OFF;

	private ArrayList<SubredditFilter> filters = new ArrayList<SubredditFilter>();


	//
	// --- Methods ---
	//

	// --- Preferences ---
	public static class Rotation {
		/* From http://developer.android.com/reference/android/R.attr.html#screenOrientation
         * unspecified -1
         * landscape 0
         * portrait 1
         * user 2
         * behind 3
         * sensor 4
         * nosensor 5
         */
		public static int valueOf(String valueString) {
			if (Constants.PREF_ROTATION_UNSPECIFIED.equals(valueString))
				return -1;
			if (Constants.PREF_ROTATION_PORTRAIT.equals(valueString))
				return 1;
			if (Constants.PREF_ROTATION_LANDSCAPE.equals(valueString))
				return 0;
			return -1;
		}
		public static String toString(int value) {
			switch (value) {
				case -1:
					return Constants.PREF_ROTATION_UNSPECIFIED;
				case 1:
					return Constants.PREF_ROTATION_PORTRAIT;
				case 0:
					return Constants.PREF_ROTATION_LANDSCAPE;
				default:
					return Constants.PREF_ROTATION_UNSPECIFIED;
			}
		}
	}

	public void saveRedditPreferences(Context context) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();

		// Session
		if (this.username != null)
			editor.putString("username", this.username);
		else
			editor.remove("username");
		if (this.redditSessionCookie != null) {
			editor.putString("reddit_sessionValue",  this.redditSessionCookie.getValue());
			editor.putString("reddit_sessionDomain", this.redditSessionCookie.getDomain());
			editor.putString("reddit_sessionPath",   this.redditSessionCookie.getPath());
			if (this.redditSessionCookie.getExpiryDate() != null)
				editor.putLong("reddit_sessionExpiryDate", this.redditSessionCookie.getExpiryDate().getTime());
		}
		if (this.modhash != null)
			editor.putString("modhash", this.modhash.toString());

		// Default subreddit
		editor.putString(Constants.PREF_HOMEPAGE, this.homepage.toString());

		// browsersettings
		editor.putBoolean(Constants.PREF_OVERWRITE_UA, this.overWriteUA);
		editor.putString(Constants.BROWSER_UA_STRING, this.useragent.toString());
		editor.putBoolean(Constants.PREF_LOAD_JS, this.loadJavascript);
		editor.putBoolean(Constants.PREF_LOAD_PLUGINS, this.loadPlugins);
		editor.putBoolean(Constants.PREF_IMGUR_DIRECT, this.loadImgurImagesDirectly);
		editor.putBoolean(Constants.PREF_VREDDIT_DIRECT, this.loadVredditLinksDirectly);

		// Use external browser instead of BrowserActivity
		editor.putBoolean(Constants.PREF_USE_EXTERNAL_BROWSER, this.useExternalBrowser);

		// Show confirmation dialog when backing out of root Activity
		editor.putBoolean(Constants.PREF_CONFIRM_QUIT, this.confirmQuitOrLogout);

		// Save reddit history to Browser history
		editor.putBoolean(Constants.PREF_SAVE_HISTORY, this.saveHistory);

		// Whether to always show the next/previous buttons, or only at bottom of list
		editor.putBoolean(Constants.PREF_ALWAYS_SHOW_NEXT_PREVIOUS, this.alwaysShowNextPrevious);

		// Comments sort order
		editor.putString(Constants.PREF_COMMENTS_SORT_BY_URL, this.commentsSortByUrl);

		// Theme and text size
		String[] themeTextSize = Util.getPrefsFromThemeResource(this.theme);
		editor.putString(Constants.PREF_THEME, themeTextSize[0]);
		editor.putString(Constants.PREF_TEXT_SIZE, themeTextSize[1]);

		// Comment guide lines
		editor.putBoolean(Constants.PREF_SHOW_COMMENT_GUIDE_LINES, this.showCommentGuideLines);


		// Rotation
		editor.putString(Constants.PREF_ROTATION, RedditSettings.Rotation.toString(this.rotation));

		// Thumbnails
		editor.putBoolean(Constants.PREF_LOAD_THUMBNAILS, this.loadThumbnails);
		editor.putBoolean(Constants.PREF_LOAD_THUMBNAILS_ONLY_WIFI, this.loadThumbnailsOnlyWifi);

		// Notifications
		editor.putString(Constants.PREF_MAIL_NOTIFICATION_STYLE, this.mailNotificationStyle);
		editor.putString(Constants.PREF_MAIL_NOTIFICATION_SERVICE, this.mailNotificationService);

		// Show NSFW
		editor.putBoolean(Constants.PREF_SHOW_NSFW, this.showNSFW);

		// Filters
		editor.putString(Constants.PREF_REDDIT_FILTERS, getFilterString());
		editor.commit();
	}


	public void loadRedditPreferences(Context context, HttpClient client) {
		// Session
		SharedPreferences sessionPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		this.setUsername(sessionPrefs.getString("username", null));
		this.setModhash(sessionPrefs.getString("modhash", null));
		String cookieValue = sessionPrefs.getString("reddit_sessionValue", null);
		String cookieDomain = sessionPrefs.getString("reddit_sessionDomain", null);
		String cookiePath = sessionPrefs.getString("reddit_sessionPath", null);
		long cookieExpiryDate = sessionPrefs.getLong("reddit_sessionExpiryDate", -1);
		if (cookieValue != null) {
			BasicClientCookie redditSessionCookie = new BasicClientCookie("reddit_session", cookieValue);
			redditSessionCookie.setDomain(cookieDomain);
			redditSessionCookie.setPath(cookiePath);
			if (cookieExpiryDate != -1)
				redditSessionCookie.setExpiryDate(new Date(cookieExpiryDate));
			else
				redditSessionCookie.setExpiryDate(null);
			this.setRedditSessionCookie(redditSessionCookie);
			RedditIsFunHttpClientFactory.getCookieStore().addCookie(redditSessionCookie);
			try {
				CookieSyncManager.getInstance().sync();
			} catch (IllegalStateException ex) {
				if (Constants.LOGGING) Log.e(TAG, "CookieSyncManager.getInstance().sync()", ex);
			}
		}

		// Default subreddit
		String homepage = sessionPrefs.getString(Constants.PREF_HOMEPAGE, Constants.FRONTPAGE_STRING).trim();
		if (StringUtils.isEmpty(homepage))
			this.setHomepage(Constants.FRONTPAGE_STRING);
		else
			this.setHomepage(homepage);

		//Browser Settings
		this.setOverwriteUA(sessionPrefs.getBoolean(Constants.PREF_OVERWRITE_UA, false));
		this.setUseragent(sessionPrefs.getString(Constants.BROWSER_UA,Constants.BROWSER_UA_STRING));
		this.setLoadJS(sessionPrefs.getBoolean(Constants.PREF_LOAD_JS, true));
		this.setLoadPlugins(sessionPrefs.getBoolean(Constants.PREF_LOAD_PLUGINS, true));
		this.setLoadImgurImagesDirectly(sessionPrefs.getBoolean(Constants.PREF_IMGUR_DIRECT, true));
		this.setLoadVredditLinksDirectly(sessionPrefs.getBoolean(Constants.PREF_VREDDIT_DIRECT, false));

		// Use external browser instead of BrowserActivity
		this.setUseExternalBrowser(sessionPrefs.getBoolean(Constants.PREF_USE_EXTERNAL_BROWSER, false));

		// Show confirmation dialog when backing out of root Activity
		this.setConfirmQuitOrLogout(sessionPrefs.getBoolean(Constants.PREF_CONFIRM_QUIT, true));

		// Save reddit history to Browser history
		this.setSaveHistory(sessionPrefs.getBoolean(Constants.PREF_SAVE_HISTORY, true));

		// Whether to always show the next/previous buttons, or only at bottom of list
		this.setAlwaysShowNextPrevious(sessionPrefs.getBoolean(Constants.PREF_ALWAYS_SHOW_NEXT_PREVIOUS, true));

		// Comments sort order
		this.setCommentsSortByUrl(sessionPrefs.getString(Constants.PREF_COMMENTS_SORT_BY_URL, Constants.CommentsSort.SORT_BY_BEST_URL));

		// Theme and text size
		this.setTheme(Util.getThemeResourceFromPrefs(
				sessionPrefs.getString(Constants.PREF_THEME, Constants.PREF_THEME_DARK),
				sessionPrefs.getString(Constants.PREF_TEXT_SIZE, Constants.PREF_TEXT_SIZE_MEDIUM)));

		// Comment guide lines
		this.setShowCommentGuideLines(sessionPrefs.getBoolean(Constants.PREF_SHOW_COMMENT_GUIDE_LINES, true));

		// Rotation
		this.setRotation(RedditSettings.Rotation.valueOf(
				sessionPrefs.getString(Constants.PREF_ROTATION, Constants.PREF_ROTATION_UNSPECIFIED)));

		// Thumbnails
		this.setLoadThumbnails(sessionPrefs.getBoolean(Constants.PREF_LOAD_THUMBNAILS, true));
		// Thumbnails on Wifi
		this.setLoadThumbnailsOnlyWifi(sessionPrefs.getBoolean(Constants.PREF_LOAD_THUMBNAILS_ONLY_WIFI, false));


		// NSFW
		this.setShowNSFW(sessionPrefs.getBoolean(Constants.PREF_SHOW_NSFW, Constants.PREF_SHOW_NSFW_DEFAULT));
		// Notifications
		this.setMailNotificationStyle(sessionPrefs.getString(Constants.PREF_MAIL_NOTIFICATION_STYLE, Constants.PREF_MAIL_NOTIFICATION_STYLE_DEFAULT));
		this.setMailNotificationService(sessionPrefs.getString(Constants.PREF_MAIL_NOTIFICATION_SERVICE, Constants.PREF_MAIL_NOTIFICATION_SERVICE_OFF));
		this.setFilters(sessionPrefs.getString(Constants.PREF_REDDIT_FILTERS, null));

	}



	public int getDialogTheme() {
		if (Util.isLightTheme(theme))
			return R.style.Reddit_Light_Dialog;
		else
			return R.style.Reddit_Dark_Dialog;
	}

	public int getDialogNoTitleTheme() {
		if (Util.isLightTheme(theme))
			return R.style.Reddit_Light_Dialog_NoTitle;
		else
			return R.style.Reddit_Dark_Dialog_NoTitle;
	}

	public boolean isLoggedIn() {
		return username != null;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Cookie getRedditSessionCookie() {
		return redditSessionCookie;
	}

	public void setRedditSessionCookie(Cookie redditSessionCookie) {
		this.redditSessionCookie = redditSessionCookie;
	}

	public String getModhash() {
		return modhash;
	}

	public void setModhash(String modhash) {
		this.modhash = modhash;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}


	public boolean isUseExternalBrowser() {
		return useExternalBrowser;
	}

	public void setUseExternalBrowser(boolean useExternalBrowser) {
		this.useExternalBrowser = useExternalBrowser;
	}

	public boolean isShowCommentGuideLines() {
		return showCommentGuideLines;
	}

	public void setShowCommentGuideLines(boolean showCommentGuideLines) {
		this.showCommentGuideLines = showCommentGuideLines;
	}

	public boolean isConfirmQuitOrLogout() {
		return confirmQuitOrLogout;
	}

	public boolean isSaveHistory() {
		return saveHistory;
	}

	public void setConfirmQuitOrLogout(boolean confirmQuitOrLogout) {
		this.confirmQuitOrLogout = confirmQuitOrLogout;
	}

	public void setSaveHistory(boolean saveHistory) {
		this.saveHistory = saveHistory;
	}

	public boolean isAlwaysShowNextPrevious() {
		return alwaysShowNextPrevious;
	}

	public void setAlwaysShowNextPrevious(boolean alwaysShowNextPrevious) {
		this.alwaysShowNextPrevious = alwaysShowNextPrevious;
	}

	public int getThreadDownloadLimit() {
		return threadDownloadLimit;
	}

	public void setThreadDownloadLimit(int threadDownloadLimit) {
		this.threadDownloadLimit = threadDownloadLimit;
	}

	public String getCommentsSortByUrl() {
		return commentsSortByUrl;
	}

	public void setCommentsSortByUrl(String commentsSortByUrl) {
		this.commentsSortByUrl = commentsSortByUrl;
	}

	public int getTheme() {
		return theme;
	}

	public void setTheme(int theme) {
		this.theme = theme;
	}

	//browsersettings
	public void setUseragent(String ua) {
		this.useragent = ua;
	}

	public String getUseragent() {
		return useragent;
	}

	public void setOverwriteUA(boolean overwriteUA) {
		this.overWriteUA = overwriteUA;
	}

	public void setLoadJS(boolean LoadJS) {
		this.loadJavascript = LoadJS;
	}

	public void setLoadPlugins(boolean LoadPlugins) {
		this.loadPlugins = LoadPlugins;
	}

	public void setLoadImgurImagesDirectly(boolean newLoadImgurImagesDirectly) {
		this.loadImgurImagesDirectly = newLoadImgurImagesDirectly;
	}

	public void setLoadVredditLinksDirectly(boolean loadVredditLinksDirectly) {
		this.loadVredditLinksDirectly = loadVredditLinksDirectly;
	}

	public boolean isOverwriteUA() {
		return overWriteUA;
	}

	public boolean isLoadJavascript() {
		return loadJavascript;
	}

	public boolean isLoadPlugins() {
		return loadPlugins;
	}

	public boolean isLoadImgurImagesDirectly() {
		return this.loadImgurImagesDirectly;
	}

	public boolean isLoadVredditLinksDirectly() {
		return this.loadVredditLinksDirectly;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public boolean isLoadThumbnails() {
		return loadThumbnails;
	}

	public void setLoadThumbnails(boolean loadThumbnails) {
		this.loadThumbnails = loadThumbnails;
	}

	public boolean isLoadThumbnailsOnlyWifi() {
		return loadThumbnailsOnlyWifi;
	}

	public void setLoadThumbnailsOnlyWifi(boolean loadThumbnailsOnlyWifi) {
		this.loadThumbnailsOnlyWifi = loadThumbnailsOnlyWifi;
	}

	public String getMailNotificationStyle() {
		return mailNotificationStyle;
	}

	public void setMailNotificationStyle(String mailNotificationStyle) {
		this.mailNotificationStyle = mailNotificationStyle;
	}

	public String getMailNotificationService() {
		return mailNotificationService;
	}

	public void setMailNotificationService(String mailNotificationService) {
		this.mailNotificationService = mailNotificationService;
	}
	public boolean getShowNSFW() {
		return this.showNSFW;
	}
	public void setShowNSFW(boolean b) {
		this.showNSFW = b;
	}
	private void setFilters(String f) {
		filters = parseFilterString(f);
	}
	public void setFilters(ArrayList<SubredditFilter> f)
	{
		filters = f;
	}
	public ArrayList<SubredditFilter> getFilters() {
		return filters;
	}

	private String getFilterString() {

		String ret = "";
		if(filters == null) return ret;
		for(SubredditFilter s: filters)
		{
			ret += s.toString() + Constants.PREF_FILTER_DELIM;
		}
		return ret;
	}
	private ArrayList<SubredditFilter> parseFilterString(String filterString) {
		ArrayList<SubredditFilter> ret = new ArrayList<SubredditFilter>();
		if(filterString == null) return ret;
		String filt[] = filterString.split(Constants.PREF_FILTER_DELIM);
		for(String s: filt)
		{
			if(!StringUtils.isEmpty(s))
				ret.add(SubredditFilter.fromString(s));
		}
		return ret;
	}
}
