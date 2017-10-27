package weifen.com.eclife.db;


import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/18.
 */
public class ShoppingCom implements Serializable{

    private String id,small_id,big_id,title,money,image_url1,image_url2;
    private String publish_time,public_tel,tel,sale_nums;
    private String content,userid,is_display,distance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSmall_id() {
        return small_id;
    }

    public void setSmall_id(String small_id) {
        this.small_id = small_id;
    }

    public String getBig_id() {
        return big_id;
    }

    public void setBig_id(String big_id) {
        this.big_id = big_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getImage_url1() {
        return image_url1;
    }

    public void setImage_url1(String image_url1) {
        this.image_url1 = image_url1;
    }

    public String getImage_url2() {
        return image_url2;
    }

    public void setImage_url2(String image_url2) {
        this.image_url2 = image_url2;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public String getPublic_tel() {
        return public_tel;
    }

    public void setPublic_tel(String public_tel) {
        this.public_tel = public_tel;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getSale_nums() {
        return sale_nums;
    }

    public void setSale_nums(String sale_nums) {
        this.sale_nums = sale_nums;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getIs_display() {
        return is_display;
    }

    public void setIs_display(String is_display) {
        this.is_display = is_display;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}