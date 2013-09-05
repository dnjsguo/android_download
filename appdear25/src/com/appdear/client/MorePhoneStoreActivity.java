package com.appdear.client;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;

import com.appdear.bdmap.Location;
import com.appdear.client.commctrls.BaseActivity;
import com.appdear.client.commctrls.BaseGroupActivity;
import com.appdear.client.exception.ApiException;
import com.appdear.client.exception.ServerException;
import com.appdear.client.service.AppContext;
import com.appdear.client.service.Constants;
import com.appdear.client.service.SoftFormTags;
import com.appdear.client.service.api.ApiCityRequest;
import com.appdear.client.service.api.ApiCitylistRequest;
import com.appdear.client.service.api.ApiUserResult;

public class MorePhoneStoreActivity extends BaseGroupActivity{
	/**
	 * 请求城市列表结果
	 */
	ApiCitylistRequest cityResult;
	private LocalActivityManager manager;
//	private String[] channels = {"htc","lenovo","huawei","samsung"};
	private String[] channels = {"HTC","三星","联想","华为"};
	private static HashMap<String,String> map=new HashMap();
	private static HashMap<String,String> productmap=new HashMap();
	String array [] = {"北京","重庆","新疆","广东","浙江","天津","广西","内蒙古","宁夏","江西","贵州","安徽","陕西","辽宁","山西","青海","四川","江苏","河北","西藏","福建","吉林","海南","湖北","云南","上海","甘肃","湖南","山东","河南","黑龙江"};
	String array1[][] = {{"北京"},
			{"重庆"},
			{"阿克苏","博乐","昌吉","哈密","喀什","克拉玛依","库尔勒","奎屯","石河子","塔城","乌鲁木齐","伊犁","伊宁","阿勒泰","吐鲁番"},
			{"广州","佛山","深圳","东莞","汕头","中山","肇庆","惠州","湛江","梅州","揭阳","潮州","阳江","江门","珠海","茂名","清远","河源","汕尾","韶关","云浮","台山","吴川"},
			{"杭州","温州","台州","宁波","金华","绍兴","嘉兴","舟山","丽水","湖州","衢州"},
			{"天津"},
			{"南宁","贵港","玉林","河池","梧州","柳州","桂林","百色","北海","崇左","防城港","贺州","来宾","钦州","白色"},
			{"阿拉善左旗","巴彦淖尔","包头","赤峰","鄂尔多斯","呼和浩特","呼伦贝尔","通辽","乌海","乌兰察布","锡林浩特","兴安盟","锡林郭勒盟"},
			{"银川","固原","吴忠"},
			{"南昌","抚州","萍乡","上饶","宜春","赣州","吉安","景德镇","九江","新余","鹰潭"},
			{"贵阳","铜仁","都匀","兴义","六盘水","毕节","遵义","安顺","凯里","黔南"},
			{"安庆","蚌埠","合肥","宣城","铜陵","宿州","淮南","淮北","阜阳","滁州","亳州","马鞍山","巢湖","池州","黄山","六安","芜湖"},
			{"安康","宝鸡","汉中","商洛","铜川","渭南","西安","咸阳","榆林","延安"},
			{"鞍山","本溪","朝阳","大连","丹东","调兵山","抚顺","阜新","葫芦岛","辽阳","盘锦","沈阳","铁岭","营口","锦州"},
			{"长治","大同","晋城","晋中","临汾","吕梁","朔州","太原","忻州","阳泉","运城"},
			{"西宁","格尔木"},
			{"成都","凉山","自贡","宜宾","遂宁","绵阳","泸州","乐山","攀枝花","南充","广元","眉山","内江","雅安","德阳","达州","巴中","广安","西昌","资阳"},
			{"南京","镇江","盐城","泰州","南通","淮安","连云港","宿迁","无锡","苏州","常州","徐州","扬州"},
			{"保定","沧州","承德","邯郸","衡水","廊坊","秦皇岛","石家庄","唐山","邢台","张家口"},
			{"拉萨"},
			{"福州","龙岩","南平","泉州","厦门","宁德","漳州","莆田","三明"},
			{"白城","白山","长春","吉林","辽源","四平","松原","通化","延边","珲春","梅河口","延吉"},
			{"海口","琼海","万宁","文昌","三亚","儋州"},
			{"武汉","恩施","宜昌","荆门","荆州","十堰","随州","襄樊","仙桃","黄石","孝感","鄂州","黄冈","潜江","咸宁"},
			{"昆明","文山","红河州","玉溪","大理","保山","普洱","楚雄","临沧","昭通","丽江","德宏","曲靖","迪庆州","西双版纳"},
			{"上海"},
			{"白银","定西","金昌","酒泉","兰州","临夏","平凉","天水","陇南","武威","张掖","庆阳"},
			{"长沙","岳阳","株洲","怀化","永州","湘潭","衡阳","邵阳","娄底","益阳","常德","郴州","张家界","吉首"},
			{"济南","青岛","枣庄","潍坊","泰安","日照","临沂","淄博","东营","济宁","聊城","德州","烟台","威海","菏泽","莱芜","滨州"},
			{"洛阳","漯河","南阳","濮阳","平顶山","三门峡","周口","新乡","商丘","许昌","驻马店","安阳","鹤壁","济源","焦作","开封","信阳","郑州"},
			{"大庆","哈尔滨","鹤岗","黑河","鸡西","佳木斯","牡丹江","齐齐哈尔","绥化","双鸭山","七台河","伊春"}};
	static{
		/*map.put("重庆","CIT000000000004");map.put("沈阳","CIT000000000032");map.put("贵阳","CIT000000000249");
		map.put("三明","CIT000000000162");map.put("南平","CIT000000000163");map.put("张家界","CIT000000000185");
		map.put("株洲","CIT000000000187");map.put("湘潭","CIT000000000189");
		map.put("益阳","CIT000000000191");map.put("衡阳","CIT000000000192");
		map.put("郴州","CIT000000000194");map.put("长沙","CIT000000000195");map.put("十堰","CIT000000000108");
		map.put("咸宁","CIT000000000109");map.put("武汉","CIT000000000113");map.put("襄樊","CIT000000000116");
		map.put("台州","CIT000000000152");map.put("嘉兴","CIT000000000153");map.put("宁波","CIT000000000154");
		map.put("杭州","CIT000000000155");map.put("绍兴","CIT000000000158");map.put("信阳","CIT000000000092");
		map.put("商丘","CIT000000000095");map.put("安阳","CIT000000000096");map.put("开封","CIT000000000098");
		map.put("新乡","CIT000000000099");map.put("许昌","CIT000000000104");map.put("郑州","CIT000000000105");map.put("驻马店","CIT000000000106");
		map.put("保定","CIT000000000041");map.put("唐山","CIT000000000042");
		map.put("张家口","CIT000000000044");map.put("沧州","CIT000000000046");map.put("石家庄","CIT000000000047");
		map.put("邢台","CIT000000000050");
		map.put("邯郸","CIT000000000051");
		map.put("九江","CIT000000000172");
		map.put("南昌","CIT000000000173");
		map.put("新余","CIT000000000177");
		map.put("景德镇","CIT000000000178");
		map.put("赣州","CIT000000000180");
		map.put("南通","CIT000000000139");
		map.put("宿迁","CIT000000000140");
		map.put("常州","CIT000000000141");
		map.put("徐州","CIT000000000142");
		map.put("扬州","CIT000000000143");
		map.put("无锡","CIT000000000144");
		map.put("泰州","CIT000000000145");
		map.put("盐城","CIT000000000147");
		map.put("苏州","CIT000000000148");
		map.put("佛山","CIT000000000197");
		map.put("广州","CIT000000000198");
		map.put("河源","CIT000000000205");
		map.put("深圳","CIT000000000206");
		map.put("清远","CIT000000000207");
		map.put("湛江","CIT000000000208");
		map.put("肇庆","CIT000000000211");
		map.put("中山","CIT000000000334");
		map.put("大同","CIT000000000054");
		map.put("太原","CIT000000000055");
		map.put("阳泉","CIT000000000062");
		map.put("东营","CIT000000000063");
		map.put("临沂","CIT000000000064");
		map.put("威海","CIT000000000065");
		map.put("德州","CIT000000000066");
		map.put("日照","CIT000000000067");
		map.put("枣庄","CIT000000000068");
		map.put("泰安","CIT000000000069");
		map.put("济南","CIT000000000070");
		map.put("淄博","CIT000000000071");
		map.put("滨州","CIT000000000072");
		map.put("潍坊","CIT000000000073");
		map.put("烟台","CIT000000000074");
		map.put("聊城","CIT000000000075");
		map.put("莱芜","CIT000000000076");
		map.put("菏泽","CIT000000000077");
		map.put("青岛","CIT000000000078");
		map.put("济宁","CIT000000000335");
		map.put("亳州","CIT000000000121");
		map.put("合肥","CIT000000000123");
		map.put("宣城","CIT000000000125");
		map.put("宿州","CIT000000000126");
		map.put("巢湖","CIT000000000127");
		map.put("池州","CIT000000000128");
		map.put("淮南","CIT000000000130");
		map.put("滁州","CIT000000000131");
		map.put("芜湖","CIT000000000132");
		map.put("蚌埠","CIT000000000133");
		map.put("铜陵","CIT000000000134");
		map.put("马鞍山","CIT000000000136");
		map.put("北京","CIT000000000001");
		map.put("乌兰察布","CIT000000000079");
		map.put("包头","CIT000000000082");
		map.put("呼伦贝尔","CIT000000000083");
		map.put("呼和浩特","CIT000000000084");
		map.put("昆明","CIT000000000238");
		map.put("曲靖","CIT000000000240");
		map.put("楚雄","CIT000000000241");
		map.put("红河","CIT000000000243");
		map.put("上海","CIT000000000002");*/
		productmap.put("三星","samsung");
		productmap.put("联想","samsung");
		productmap.put("华为","huawei");
		productmap.put("HTC","htc");
		map.put("上海","CIT100053");
		map.put("昆明","CIT100040");
		map.put("文山","CIT100041");
		map.put("红河州","CIT100042");
		map.put("玉溪","CIT100043");
		map.put("大理","CIT100044");
		map.put("保山","CIT100045");
		map.put("普洱","CIT100046");
		map.put("楚雄","CIT100047");
		map.put("临沧","CIT100048");
		map.put("昭通","CIT100049");
		map.put("丽江","CIT100050");
		map.put("德宏","CIT100051");
		map.put("曲靖","CIT100052");
		map.put("迪庆州","CIT100307");
		map.put("西双版纳","CIT100308");
		map.put("阿拉善左旗","CIT100255");
		map.put("巴彦淖尔","CIT100256");
		map.put("包头","CIT100257");
		map.put("赤峰","CIT100258");
		map.put("鄂尔多斯","CIT100259");
		map.put("呼和浩特","CIT100260");
		map.put("呼伦贝尔","CIT100261");
		map.put("通辽","CIT100262");
		map.put("乌海","CIT100263");
		map.put("乌兰察布","CIT100264");
		map.put("锡林浩特","CIT100265");
		map.put("兴安盟","CIT100317");
		map.put("锡林郭勒盟","CIT100329");
		map.put("北京","CIT100163");
		map.put("白城","CIT100226");
		map.put("白山","CIT100227");
		map.put("长春","CIT100228");
		map.put("吉林","CIT100229");
		map.put("辽源","CIT100230");
		map.put("四平","CIT100231");
		map.put("松原","CIT100232");
		map.put("通化","CIT100233");
		map.put("延边","CIT100234");
		map.put("珲春","CIT100327");
		map.put("梅河口","CIT100328");
		map.put("延吉","CIT100334");
		map.put("成都","CIT100015");
		map.put("凉山","CIT100016");
		map.put("自贡","CIT100017");
		map.put("宜宾","CIT100018");
		map.put("遂宁","CIT100019");
		map.put("绵阳","CIT100020");
		map.put("泸州","CIT100021");
		map.put("乐山","CIT100022");
		map.put("攀枝花","CIT100023");
		map.put("南充","CIT100024");
		map.put("广元","CIT100025");
		map.put("眉山","CIT100026");
		map.put("内江","CIT100027");
		map.put("雅安","CIT100028");
		map.put("德阳","CIT100029");
		map.put("达州","CIT100030");
		map.put("巴中","CIT100290");
		map.put("广安","CIT100291");
		map.put("西昌","CIT100292");
		map.put("资阳","CIT100293");
		map.put("天津","CIT100294");
		map.put("银川","CIT100266");
		map.put("固原","CIT100330");
		map.put("吴忠","CIT100331");
		map.put("安庆","CIT100000");
		map.put("蚌埠","CIT100001");
		map.put("合肥","CIT100080");
		map.put("宣城","CIT100081");
		map.put("铜陵","CIT100082");
		map.put("宿州","CIT100083");
		map.put("淮南","CIT100084");
		map.put("淮北","CIT100085");
		map.put("阜阳","CIT100086");
		map.put("滁州","CIT100087");
		map.put("亳州","CIT100088");
		map.put("马鞍山","CIT100089");
		map.put("巢湖","CIT100158");
		map.put("池州","CIT100159");
		map.put("黄山","CIT100160");
		map.put("六安","CIT100161");
		map.put("芜湖","CIT100162");
		map.put("济南","CIT100066");
		map.put("青岛","CIT100067");
		map.put("枣庄","CIT100068");
		map.put("潍坊","CIT100069");
		map.put("泰安","CIT100070");
		map.put("日照","CIT100071");
		map.put("临沂","CIT100072");
		map.put("淄博","CIT100073");
		map.put("东营","CIT100074");
		map.put("济宁","CIT100075");
		map.put("聊城","CIT100076");
		map.put("德州","CIT100077");
		map.put("烟台","CIT100078");
		map.put("威海","CIT100079");
		map.put("菏泽","CIT100268");
		map.put("莱芜","CIT100269");
		map.put("滨州","CIT100319");
		map.put("长治","CIT100270");
		map.put("大同","CIT100271");
		map.put("晋城","CIT100272");
		map.put("晋中","CIT100273");
		map.put("临汾","CIT100274");
		map.put("吕梁","CIT100275");
		map.put("朔州","CIT100276");
		map.put("太原","CIT100277");
		map.put("忻州","CIT100278");
		map.put("阳泉","CIT100279");
		map.put("运城","CIT100280");
		map.put("广州","CIT100135");
		map.put("佛山","CIT100136");
		map.put("深圳","CIT100137");
		map.put("东莞","CIT100138");
		map.put("汕头","CIT100139");
		map.put("中山","CIT100140");
		map.put("肇庆","CIT100141");
		map.put("惠州","CIT100142");
		map.put("湛江","CIT100143");
		map.put("梅州","CIT100144");
		map.put("揭阳","CIT100145");
		map.put("潮州","CIT100146");
		map.put("阳江","CIT100147");
		map.put("江门","CIT100148");
		map.put("珠海","CIT100149");
		map.put("茂名","CIT100175");
		map.put("清远","CIT100176");
		map.put("河源","CIT100177");
		map.put("汕尾","CIT100178");
		map.put("韶关","CIT100179");
		map.put("云浮","CIT100180");
		map.put("台山","CIT100312");
		map.put("吴川","CIT100313");
		map.put("南宁","CIT100150");
		map.put("贵港","CIT100151");
		map.put("玉林","CIT100152");
		map.put("河池","CIT100153");
		map.put("梧州","CIT100154");
		map.put("柳州","CIT100155");
		map.put("桂林","CIT100156");
		map.put("百色","CIT100181");
		map.put("北海","CIT100182");
		map.put("崇左","CIT100183");
		map.put("防城港","CIT100184");
		map.put("贺州","CIT100185");
		map.put("来宾","CIT100186");
		map.put("钦州","CIT100187");
		map.put("白色","CIT100322");
		map.put("阿克苏","CIT100295");
		map.put("博乐","CIT100296");
		map.put("昌吉","CIT100297");
		map.put("哈密","CIT100298");
		map.put("喀什","CIT100299");
		map.put("克拉玛依","CIT100300");
		map.put("库尔勒","CIT100301");
		map.put("奎屯","CIT100302");
		map.put("石河子","CIT100303");
		map.put("塔城","CIT100304");
		map.put("乌鲁木齐","CIT100305");
		map.put("伊犁","CIT100306");
		map.put("伊宁","CIT100321");
		map.put("阿勒泰","CIT100332");
		map.put("吐鲁番","CIT100333");
		map.put("南京","CIT100090");
		map.put("镇江","CIT100091");
		map.put("盐城","CIT100092");
		map.put("泰州","CIT100093");
		map.put("南通","CIT100094");
		map.put("淮安","CIT100095");
		map.put("连云港","CIT100096");
		map.put("宿迁","CIT100097");
		map.put("无锡","CIT100098");
		map.put("苏州","CIT100100");
		map.put("常州","CIT100101");
		map.put("徐州","CIT100102");
		map.put("扬州","CIT100103");
		map.put("南昌","CIT100126");
		map.put("抚州","CIT100127");
		map.put("萍乡","CIT100128");
		map.put("上饶","CIT100129");
		map.put("宜春","CIT100130");
		map.put("赣州","CIT100235");
		map.put("吉安","CIT100236");
		map.put("景德镇","CIT100237");
		map.put("九江","CIT100238");
		map.put("新余","CIT100239");
		map.put("鹰潭","CIT100240");
		map.put("保定","CIT100193");
		map.put("沧州","CIT100194");
		map.put("承德","CIT100195");
		map.put("邯郸","CIT100196");
		map.put("衡水","CIT100197");
		map.put("廊坊","CIT100198");
		map.put("秦皇岛","CIT100199");
		map.put("石家庄","CIT100200");
		map.put("唐山","CIT100201");
		map.put("邢台","CIT100202");
		map.put("张家口","CIT100203");
		map.put("洛阳","CIT100003");
		map.put("漯河","CIT100004");
		map.put("南阳","CIT100005");
		map.put("濮阳","CIT100006");
		map.put("平顶山","CIT100007");
		map.put("三门峡","CIT100008");
		map.put("周口","CIT100009");
		map.put("新乡","CIT100010");
		map.put("商丘","CIT100011");
		map.put("许昌","CIT100012");
		map.put("驻马店","CIT100013");
		map.put("安阳","CIT100014");
		map.put("鹤壁","CIT100204");
		map.put("济源","CIT100205");
		map.put("焦作","CIT100206");
		map.put("开封","CIT100207");
		map.put("信阳","CIT100208");
		map.put("郑州","CIT100209");
		map.put("杭州","CIT100054");
		map.put("温州","CIT100055");
		map.put("台州","CIT100057");
		map.put("宁波","CIT100058");
		map.put("金华","CIT100059");
		map.put("绍兴","CIT100060");
		map.put("嘉兴","CIT100061");
		map.put("舟山","CIT100062");
		map.put("丽水","CIT100063");
		map.put("湖州","CIT100064");
		map.put("衢州","CIT100065");
		map.put("海口","CIT100157");
		map.put("琼海","CIT100190");
		map.put("万宁","CIT100191");
		map.put("文昌","CIT100192");
		map.put("三亚","CIT100311");
		map.put("儋州","CIT100324");
		map.put("武汉","CIT100104");
		map.put("恩施","CIT100105");
		map.put("宜昌","CIT100106");
		map.put("荆门","CIT100107");
		map.put("荆州","CIT100108");
		map.put("十堰","CIT100109");
		map.put("随州","CIT100110");
		map.put("襄樊","CIT100111");
		map.put("仙桃","CIT100112");
		map.put("黄石","CIT100113");
		map.put("孝感","CIT100114");
		map.put("鄂州","CIT100220");
		map.put("黄冈","CIT100221");
		map.put("潜江","CIT100222");
		map.put("咸宁","CIT100223");
		map.put("长沙","CIT100115");
		map.put("岳阳","CIT100116");
		map.put("株洲","CIT100117");
		map.put("怀化","CIT100118");
		map.put("永州","CIT100119");
		map.put("湘潭","CIT100120");
		map.put("衡阳","CIT100121");
		map.put("邵阳","CIT100122");
		map.put("娄底","CIT100123");
		map.put("益阳","CIT100124");
		map.put("常德","CIT100125");
		map.put("郴州","CIT100224");
		map.put("张家界","CIT100225");
		map.put("吉首","CIT100326");
		map.put("白银","CIT100166");
		map.put("定西","CIT100167");
		map.put("金昌","CIT100168");
		map.put("酒泉","CIT100169");
		map.put("兰州","CIT100170");
		map.put("临夏","CIT100171");
		map.put("平凉","CIT100172");
		map.put("天水","CIT100173");
		map.put("陇南","CIT100174");
		map.put("武威","CIT100210");
		map.put("张掖","CIT100309");
		map.put("庆阳","CIT100310");
		map.put("福州","CIT100002");
		map.put("龙岩","CIT100056");
		map.put("南平","CIT100099");
		map.put("泉州","CIT100131");
		map.put("厦门","CIT100132");
		map.put("宁德","CIT100133");
		map.put("漳州","CIT100134");
		map.put("莆田","CIT100164");
		map.put("三明","CIT100165");
		map.put("拉萨","CIT100031");
		map.put("贵阳","CIT100033");
		map.put("铜仁","CIT100034");
		map.put("都匀","CIT100035");
		map.put("兴义","CIT100036");
		map.put("六盘水","CIT100037");
		map.put("毕节","CIT100038");
		map.put("遵义","CIT100039");
		map.put("安顺","CIT100188");
		map.put("凯里","CIT100189");
		map.put("黔南","CIT100323");
		map.put("鞍山","CIT100241");
		map.put("本溪","CIT100242");
		map.put("朝阳","CIT100243");
		map.put("大连","CIT100244");
		map.put("丹东","CIT100245");
		map.put("调兵山","CIT100246");
		map.put("抚顺","CIT100247");
		map.put("阜新","CIT100248");
		map.put("葫芦岛","CIT100249");
		map.put("辽阳","CIT100250");
		map.put("盘锦","CIT100251");
		map.put("沈阳","CIT100252");
		map.put("铁岭","CIT100253");
		map.put("营口","CIT100254");
		map.put("锦州","CIT100316");
		map.put("重庆","CIT100032");
		map.put("安康","CIT100281");
		map.put("宝鸡","CIT100282");
		map.put("汉中","CIT100283");
		map.put("商洛","CIT100284");
		map.put("铜川","CIT100285");
		map.put("渭南","CIT100286");
		map.put("西安","CIT100287");
		map.put("咸阳","CIT100288");
		map.put("榆林","CIT100289");
		map.put("延安","CIT100320");
		map.put("西宁","CIT100267");
		map.put("格尔木","CIT100318");
		map.put("大庆","CIT100211");
		map.put("哈尔滨","CIT100212");
		map.put("鹤岗","CIT100213");
		map.put("黑河","CIT100214");
		map.put("鸡西","CIT100215");
		map.put("佳木斯","CIT100216");
		map.put("牡丹江","CIT100217");
		map.put("齐齐哈尔","CIT100218");
		map.put("绥化","CIT100219");
		map.put("双鸭山","CIT100314");
		map.put("七台河","CIT100315");
		map.put("伊春","CIT100325");
	}
	
	private LinearLayout bestListLayout; 
	private final static int POINTLISTEND=2;
	private final static int USEREND=1;
	private final static int STROECHANGE=3;
	private final static int INITCITY=4;
	private final static int DEFAULTCITY=5;
	private ApiUserResult result;
	private AutoCompleteTextView s_area,s_area1,s_channel;
	ProgressDialog progress;
	private String text;
	private ImageView button,button1,button2;
	private String s_areaid;
	private  ArrayAdapter adapter,adapter1 ;
	private View pointlistview=null;
	ImageButton btn_return;
	private LinearLayout tab_ll_linear;
	boolean backbutton_is_show;
	private int currentid=0;
	private String currentPro="",currentCity="";
	
	public void onCreate(Bundle b){
		super.onCreate(b); 
		Intent intent =getIntent();
		if(intent!=null){
			String from=intent.getStringExtra("from");
			if(from!=null&&from.equals("moreView")){
				backbutton_is_show=true;
			}else{
				backbutton_is_show=false;
			}
		}
		
		setContentView(R.layout.user_phone_store);
		isShowAlert = false;
      }
	
	@Override
	public void init() {
		
		bestListLayout= (LinearLayout) this.findViewById(
				R.id.common_list_layout);
		manager=getLocalActivityManager();
		 s_area=(AutoCompleteTextView) this.findViewById(R.id.s_area);
		 adapter = new ArrayAdapter(this,R.layout.store, array);
		 
//		 currentPro=array[0];
		 s_area.setAdapter(adapter);
		 s_area.setOnItemClickListener(autoListItemClickListener);
		 
		 s_area1=(AutoCompleteTextView) this.findViewById(R.id.s_area1);
//		 adapter1 = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line, array1[0]);
//		 s_area1.setAdapter(adapter1);
		 s_area1.setOnItemClickListener(autoListItemClickListener);
//		 currentCity=array1[0][0];
		 s_channel=(AutoCompleteTextView) this.findViewById(R.id.s_channel);
		 String tempChannel=getChannel();
		 if(tempChannel.equals("htc")){
			 s_channel.setText(channels[0]);
		 }else if(tempChannel.equals("huawei")){
			 s_channel.setText(channels[3]);
		 }else if(tempChannel.equals("lenovo")){
			 s_channel.setText(channels[2]);
		 }else{
			 s_channel.setText(channels[1]);
		 }
		 
		 s_channel.setAdapter(new ArrayAdapter<String>(MorePhoneStoreActivity.this, R.layout.store, channels));
		 s_channel.setOnItemClickListener(autoListItemClickListener);
		 //adapter = new ArrayAdapter(this,R.layout.store, array);
		 button=(ImageView)this.findViewById(R.id.button);
		 button1=(ImageView)this.findViewById(R.id.button1);
		 button2=(ImageView)this.findViewById(R.id.button2);
		 s_area1.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					currentid=R.id.s_area1;
					s_area1.showDropDown();
				}    	
	        });
		 s_area.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currentid=R.id.s_area;
				s_area.showDropDown();
			}    	
        });
		 s_channel.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					currentid=R.id.s_channel;
					s_channel.showDropDown();
				}    	
	        });
		 tab_ll_linear = (LinearLayout) findViewById(R.id.ll_navigation);
			tab_ll_linear.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		 button.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					currentid=R.id.s_area;
					s_area.showDropDown();
				}    	
	        });
		 button1.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					currentid=R.id.s_area1;
					s_area1.showDropDown();
				}    	
	        });
		 button2.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					currentid=R.id.s_channel;
					s_channel.showDropDown();
				}    	
	        });
		 btn_return = (ImageButton) findViewById(R.id.btn_return);
			btn_return.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
	}
	
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
 
			switch(msg.what){
			case 0:
					progress= ProgressDialog.show(MorePhoneStoreActivity.this, "数据加载中..", "请等待", true, false);
					break;
			case USEREND:
				result=(ApiUserResult)msg.obj;
				break;
			case POINTLISTEND:
				View view=(View)msg.obj;
				DisplayMetrics metrics=com.appdear.client.utility.ServiceUtils.getMetrics(MorePhoneStoreActivity.this.getWindowManager());
				LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
				view.setLayoutParams(params);
				bestListLayout.removeAllViews();
				bestListLayout.addView((View)msg.obj);
				if(progress!=null)progress.dismiss();
				break;
			case STROECHANGE:
				updateUI();
				break;
			case INITCITY:
				if(adapter!=null){
					s_area.setAdapter(adapter);
		    		adapter.notifyDataSetChanged();
				}
				break;
			case DEFAULTCITY:
				handlerStore(msg.obj.toString());
				break;
			default:return;
			}
		}
	};
    @SuppressWarnings("unchecked")
	@Override
	public void initData() {
//    	try {
//    		/**
//    		 * 参数为空，代表 加载所有的城市
//    		 */
//    		 cityResult = ApiCityRequest.citylistRequest("");
//    		 array = cityResult.citys.split(",");
//    		 if(Constants.DEBUG)Log.i("responseStr", "responseStr.citys,"+cityResult.citys);
//    		 if (array.length > 0) {
//	    		 if (array.length == 2) {
//	    			 adapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line, new String [] {array[0]});
//	    		 } else {
//	    			 adapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line, array);
//	    		 }
//    		 } else {
//    			 adapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line, new String []{});
//    			 adapter.notifyDataSetChanged();
//    		 }
//		} catch (ServerException e) {
//			showException(e);
//			e.printStackTrace();
//		} catch (ApiException e) {
//			showException(e);
//			e.printStackTrace();
//		}catch(Exception e){
//			
//		}
//		if(Location.currentCity!=null){
//			String city=Location.currentCity;
//			if(city.endsWith("市")){
//				city=city.substring(0,city.lastIndexOf("市"));
//			}
//			if(map.containsKey(city)){
//				Message msg=handler.obtainMessage();
//				msg.what=DEFAULTCITY;
//				msg.obj=city;
//				handler.sendMessage(msg);
//			}
//		}
    	super.initData();
	}
 
	@Override
	public void updateUI() {
		handler.sendEmptyMessage(INITCITY);
		String city=Location.currentCity;
		if(city!=null&&!city.equals("")){
			city=city.endsWith("市")?city.substring(0,city.lastIndexOf("市")):city;
			if(map.containsKey(city)){
				s_area1.setText(city);
				int opt=handlerCity(city);
				s_area.setText(array[opt]);
				adapter = new ArrayAdapter(this,R.layout.store, array);
				s_area.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				adapter1 = new ArrayAdapter(MorePhoneStoreActivity.this,R.layout.store, array1[opt]);
				s_area1.setAdapter(adapter1);
				handlerStore(city);
			}else{
				defaultUi();
			}
		}else{
			defaultUi();
		}
		super.updateUI();
	}
	public void defaultUi(){
		System.out.println(s_channel.getText().toString()+">>>>>>>>>>>>");
		pointlistview = manager.startActivity(
		           s_area.getText().toString(),
		           new Intent(MorePhoneStoreActivity.this, StoreListActivity.class).
		           putExtra("first",true).putExtra("area", map.get(currentCity.trim())).putExtra("androidchchode", "".equals(s_channel.getText().toString().trim())?getChannel():productmap.get(s_channel.getText().toString()))).getDecorView();//
		Message message=Message.obtain();
		message.what=POINTLISTEND;
		message.obj=pointlistview;	
		handler.sendMessage(message);
	}
	OnItemClickListener autoListItemClickListener =  new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
		  if(currentid==R.id.s_area){
			  
			  if(currentPro!=null&&!currentPro.equals("")&&currentPro.equals(s_area.getText().toString())){
				  return;
			  }
			  currentPro=array[arg2];
			  currentCity=array1[arg2][0];
			  adapter1 = new ArrayAdapter(MorePhoneStoreActivity.this,R.layout.store, array1[arg2]);
			  s_area1.setText(array1[arg2][0]);
			  s_area1.setAdapter(adapter1);
			  handlerStore(null);
		  }else if(currentid==R.id.s_area1){
			  if(currentCity!=null&&!currentCity.equals("")&&currentCity.equals(s_area1.getText().toString())){
				  return;
			  }
			  handlerStore(null);
		 }else if(currentid==R.id.s_channel){
			 System.out.println(s_channel.getId()+"----------"+s_channel.getText().toString());
			  if(currentCity!=null&&!currentCity.equals("")&&currentCity.equals(s_channel.getText().toString())){
				  return;
			  }
			  handlerStore(null);//TODO
		 }
		}
	};
	private void handlerStore(String city){
		 if(city==null){
		  currentCity=s_area1.getText().toString();
		 }else{
			 currentCity=city;
		 }
		  if(Constants.DEBUG)Log.i("MorePhoneStoreActivity", "MorePhoneStoreActivity   updateUI ");
		   handler.sendEmptyMessage(0);//s_channel.getId()==View.NO_ID?"samsung":channels[s_channel.getId()-1]
		   System.out.println(s_channel.getText().toString()+">>>>>>>>>>>>");
		   pointlistview=manager.startActivity( 
             s_area1.getText().toString(),
              new Intent(MorePhoneStoreActivity.this, StoreListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).
              putExtra("area",map.get(currentCity.trim())).putExtra("androidchchode", "".equals(s_channel.getText().toString().trim())?getChannel():productmap.get(s_channel.getText().toString()))
            ).getDecorView();
			Message message=Message.obtain();
			message.what=POINTLISTEND;
			message.obj=pointlistview;	
			handler.sendMessage(message);
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0&&AppContext.userreg==true) {
			AppContext.userreg=false;
			Intent userIntent = new Intent();
			//跳转到MainActivity ，在那个页面根据Flag加载想要的视图
			userIntent.setClass(this, MainActivity.class);
			userIntent.putExtra(SoftFormTags.ACTIVITY_SWITCH_FLAG, SoftFormTags.USER_LIST_CENTER);
			startActivity(userIntent);
            return true;
        }
      

		return super.onKeyDown(keyCode, event);
	}
	
	public int handlerCity(String city){
		for(int i=0;i<array1.length;i++){
			for(int j=0;j<array1[i].length;j++){
				if(array1[i][j].equals(city)){
					return i;
				}
			}
		}
		return -1;
	}
	
	private String getChannel(){
		String result ="";
		String company = android.os.Build.BRAND.toLowerCase();
		if(company.indexOf("htc")!=-1){
			result = "htc";
    	}else if(company.indexOf("huawei")!=-1){
    		result = "huawei";
    	}else if(company.indexOf("lenovo")!=-1){
    		result = "lenovo";
    	}else{
    		result = "samsung";
    	}
		return result;
	}
	
}
