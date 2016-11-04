package test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;

/**喜欢�?<br/>
 * @author Administrator
 *
 */
public class Like {
	private Integer id;
	private String shop_code;//商品编号
	private Integer user_id;//用户id
	private Double shop_price;//商品价格
	private String show_pic;//商品图片
	private String shop_name;//商品名称
	private Date add_time;//添加时间
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getShop_code() {
		return shop_code;
	}
	public Like() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Like( String shop_code, Integer user_id,
			Double shop_price, String show_pic, String shop_name) {
		super();
		this.shop_code = shop_code;
		this.user_id = user_id;
		this.shop_price = shop_price;
		this.show_pic = show_pic;
		this.shop_name = shop_name;
	}
	public void setShop_code(String shop_code) {
		this.shop_code = shop_code;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public Double getShop_price() {
		return shop_price;
	}
	public void setShop_price(Double shop_price) {
		this.shop_price = shop_price;
	}
	public String getShow_pic() {
		return show_pic;
	}
	public void setShow_pic(String show_pic) {
		this.show_pic = show_pic;
	}
	public String getShop_name() {
		return shop_name;
	}
	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}
	public Date getAdd_time() {
		return add_time;
	}
	public void setAdd_time(Date add_time) {
		this.add_time = add_time;
	}
	
}
