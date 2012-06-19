/**
 * 
 */
package com.comic.facade;

/**
 * 这里定义外部访问接口需要的常量字符串
 * @author lwz
 *
 */
public interface ExtVisitorInterface {
	
	public static final String GETTHUMBNAIL ="getThumbnail";
    
    
    //--------接微博相关方法
    
    //根据code获取access token
    public static final String GETACCESSTOKENBYCODE = "getAccessTokenByCode";
    
    //转发到微博
    public static final String FORWARDTOWEIBO = "forwardToWeibo";
    
    //完善资料(使用weibo账户登录，完善账号密码)
    public static final String IMPROVEWEIBOUSER = "improveWeiboUser";
    
    
}
