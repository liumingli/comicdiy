/**
 * 
 */
package com.ybcx.comic.facade;

/**
 * 这里定义外部访问接口需要的常量字符串
 * @author lwz
 *
 */
public interface ExtVisitorInterface {
	
	//获取全部可用素材数目
	public static final String GETALLASSETSCOUNT = "getAllAssetsCount";
	
	//分页获取素材
	public static final String GETASSETSBYPAGE = "getAssetsByPage";
	
	//根据标签搜索素材,返回数目 
	public static final String GETSEARCHCOUNTBYLABEL = "getSearchCountByLabel";
	
	//根据标签分页搜索素材
	public static final String SEARCHBYLABELPAGE = "searchByLabelPage";
	
	//根据标签(以空格分隔多个)以及类型(element,theme,scene)搜索素材，返回数目
	public static final String GETSEARCHCOUNTBYLABELANDTYPE = "getSearchCountByLabelAndType";
	
	//根据标签(以空格分隔多个)以及类型(element,theme,scene)搜索素材
	public static final String SEARCHBYLABELANDTYPEPAGE = "searchByLabelAndTypePage";
	
	//根据分类和类型分页查素材
	public static final String  GETBYCATEGORYANDTYPE = "getByCategoryAndType";
	
	//上传一个素材
	public static final String CREATEASSET = "createAsset";
	
	//获取一个素材
	public static final String GETASSETBYID = "getAssetById";
	
	//删除一个素材
	public static final String DELETEASSETBYID = "deleteAssetById";
	
	//更新素材
	public static final String UPDATEASSETBYID = "updateAssetById";

	//获取所有分类
	public static final String GETALLCATEGORY = "getAllCategory";
    
	//自定义创建分类
	public static final String CREATECATEGORY = "createCategory";
	
	//获取素材
	public static final String GETASSETFILE ="getAssetFile";
	
	//获取素材缩略图
	public static final String GETTHUMBNAIL ="getThumbnail";
	
	//新建标签
	public static final String CREATELABEL = "createLabel";
	
	//删除某一标签
	public static final String DELETELABEL = "deleteLabel";
	
	//根据父标签删除其下所有子标签
	public static final String DELETELABELBYPARENT = "deleteLabelByParent";
	
	//获取所有父级标签
	public static final String GETALLPARENTLABEL = "getAllParentLabel";
	
	//根据父标签获取子标签
	public static final String GETLABELBYPARENT = "getLabelByParent";
	
	
	
	//-----------------------------与前台连通测试
	
	//素材接入
	public static final String GETSYSASSETSBY = "getSysAssetsBy";
	
	//动画载入播放
	public static final String GETANIMATIONBY = "getAnimationBy";
	
	//按分类获取素材总数
	public static final String GETASSETCOUNTBY = "getAssetCountBy";
	
	//某用户的所有diy动画
	public static final String GETANIMATIONSOF = "getAnimationsOf";
	
	//上传本地图片
	public static final String UPLOADLOCALIMAGE = "uploadLocalImage";
	
	//动画保存
	public static final String SAVEANIM = "saveAnim";
	
	//动画修改
	public static final String MODIFYANIM = "modifyAnim";
	
	//查看所有动画待审核
	public static final String GETALLANIM = "getAllAnim";
	
	//获取动画总数
	public static final String GETANIMCOUNT = "getAnimCount";
	
	//动画审核删除
	public static final String  EXAMINEANIM = "examineAnim";
	
	//分页取动画
	public static final String GETANIMBYPAGE = "getAnimByPage";
	
	//分页取动画加一个参数callback(给网站用的)
	public static final String GETWEBANIM = "getWebAnim";
	
	//查看所有图片待审核
	public static final String GETALLIMAGE = "getAllImage";
	
	//用户上传的图片审核删除
	public static final String EXAMINEIMAGE = "examineImage";
	
	//获取图片总数
	public static final String GETIMAGECOUNT = "getImageCount";
	
	//分页取图片
	public static final String GETIMAGEBYPAGE = "getImageByPage";
	
	//根据动画名称模糊查询动画数量
	public static final String SEARCHANIM = "searchAnim";
	
	//根据动画名称及分页查询
	public static final String SEARCHANIMBYPAGE = "searchAnimByPage";
	
    
    //--------接微博相关方法
   //操作微博连接上的用户
	public static final String OPERATEWEIBOUSER = "operateWeiboUser";
	
	//根据id获取用户信息
	public static final String GETUSERINFO= "getUserInfo";
	
    //转发到微博
    public static final String FORWARDTOWEIBO = "forwardToWeibo"; 
	
    //分页获取好友列表包括昵称和头像
    public static final String GETFRIENDBYPAGE = "getFriendByPage";
    
    
    //---------购物车相关api
    //添加商品
    public static final String ADDASSETTOCART = "addAssetToCart";
    
    //删除商品
    public static final String DELETEASSETFROMCART = "deleteAssetFromCart";
    
    //查看当前商品的付费状态
    public static final String GETASSETSTATE = "getAssetState";
    
    //获取一组素材的状态（assetIds:以逗号分隔的assetId）
    public static final String GETSTATEBYASSETIDS = "getStateByAssetIds";
    
    //修改付费状态
    public static final String CHANGEASSETSTATE = "changeAssetState";
    
    //查看某人购物车的商品状态(需要付费的)
    public static final String GETUSERCARTSTATE = "getUserCartState";
    
    //修改购物车里所有商品的状态
    public static final String CHANGEUSERCARTSTATE = "changeUserCartState";
    
    
    //----------支付相关api
    //取支付token
    public static final String GETPAYTOKEN = "getPayToken";
    
    //获取订单状态
    public static final String GETORDERSTATUS = "getOrderStatus";
    
    //登录系统
    public static final String LOGINSYSTEM = "loginSystem";
    
    //--------------四格漫画相关
    //上传主动画素材
    public static final String CREATEPRIMARY = "createPrimary";
    
    //上传结局动画素材
    public static final String CREATEENDING = "createEnding";
    
    //获取所有主动画数量
    public static final String GETPRIMARYCOUNT = "getPrimaryCount";
    
    //分页获取主动画
    public static final String GETPRIMARYBYPAGE = "getPrimaryByPage";
    
    //获取某一主动画的所有结局数目
    public static final String GETENDINGCOUNTBYPRIMARY = "getEndingCountByPrimary";
    
    //分页获取结局
    public static final String GETENDINGBYPRIMARYANDPAGE = "getEndingByPrimaryAndPage";
    
    //删除一个主动画
    public static final String DELPRIMARY = "delPrimary";
    
    //删除一个结局
    public static final String DELENDING = "delEnding";
    
    //更新主动画（包括修改名称和帧数）
    public static final String UPDATEPRIMARY = "updatePrimary";
    
    //检验四格漫画名称
    public static final String CHECKYONKOMANAME = "checkYonkomaName";
    
    //发四格漫画到微博，主要是文字及长图片
    public static final String YONKOMATOWEIBO = "yonkomaToWeibo";
    
}
